package com.akeinhell.portcheck;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	ArrayList<Address> addresses = new ArrayList<Address>();
	AddrAdapter addrAdapter;
	// TextView tvHelloWorld;
	ListView lvAddr;
	MyTask mt;
	addrBase sqh;
	SQLiteDatabase sqdb;

	// Custom Dialog
	Button cust;
	Dialog custom;
	EditText fIP;
	EditText fPort;
	TextView txt;
	Button savebtn;
	Button canbtn;
	// End custom Dialog

	SharedPreferences sPref, sp;
	final String sCOUNT = "count";
	final String sIP = "IP";
	final String sPORT = "port";
	static String info = "INFO";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		sPref = getPreferences(MODE_PRIVATE);
		Log.i(info, "Запуск");
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		Log.i(info, "Устанавливаем параметры окна");
		// создаем адаптер
		fillData();

		Log.i(info, "Загружаем адаптер списка");
		addrAdapter = new AddrAdapter(this, addresses);

		// настраиваем список
		Log.i(info, "Подгружаем переменные");
		lvAddr = (ListView) findViewById(R.id.lvAddr);
		// tvHelloWorld = (TextView) findViewById(R.id.textView1);
		lvAddr.setAdapter(addrAdapter);

		Log.i(info, "Добавляем меню");
		registerForContextMenu(lvAddr);


		/*
		 * sqh = new addrBase(this);
		 * 
		 * // База нам нужна для записи и чтения sqdb =
		 * sqh.getWritableDatabase();
		 */
	}

	void saveData() {
		Log.i(info, "Сохраняем данные");
		sPref = getPreferences(MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putInt(sCOUNT, addresses.size());
		Log.i(info, "Количество данных" + Integer.toString(addresses.size()));
		if (addresses.size() > 0) {
			Log.i(info, "Больше нуля запускаем сохранение");
			for (int i = 1; i <= addresses.size(); i++) {
				Log.i(info,
						">" + Integer.toString(i) + "# "
								+ addresses.get(i - 1).IP + ":"
								+ Integer.toString(addresses.get(i - 1).port));
				ed.putString(sIP + Integer.toString(i), addresses.get(i - 1).IP);
				ed.putInt(sPORT + Integer.toString(i),
						addresses.get(i - 1).port);
			}
		}
		Log.i(info, "COMMIT сохранения");
		ed.commit();

	}

	void fillData() {
		Log.i(info, "Загружаем данные");
		int count = sPref.getInt(sCOUNT, 0);

		if (count > 0) {
			Log.i("DATA", "Получено " + Integer.toString(count) + " записей");
			for (int i = 1; i <= count; i++) {

				String IP = sPref.getString(sIP + Integer.toString(i),
						"0.0.0.0");
				int port = sPref.getInt(sPORT + Integer.toString(i), 0);

				Log.i("DATA", "#" + Integer.toString(i));

				Log.i("DATA", "IP:" + IP);
				Log.i("DATA", "port:" + Integer.toString(port));
				addresses.add(new Address(IP, port, 0, false));
			}
		} else {
			Log.i("DATA", "Пустая БД. Вставляем новую запись...");
			addresses.add(new Address("80.67.50.20", 1126, 0, false));
			Editor ed = sPref.edit();
			ed.putInt(sCOUNT, 1);
			ed.putString(sIP + "1", "80.67.50.20");
			ed.putInt(sPORT + "1", 1126);
			ed.commit();

		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.i(info, "Обработка выбора пункта контекстного меню");
		AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
				.getMenuInfo();

		Log.i(info, "выбран пункт меню " + item.getTitle());
		switch (item.getItemId()) {
		case R.id.mcAdd:
			Log.i(info, "Пункт меню -- добавление");
			AddAddr();
			break;
		case R.id.mcEdit:
			Log.i(info, "Пункт меню -- редактирование");
			EditAddr(acmi.position);
			break;
		case R.id.mcDelete:
			Log.i(info, "Пункт меню -- удаление");
			Log.i(info,
					"Пробуем удалить запись №"
							+ Integer.toString(acmi.position));
			addresses.remove(acmi.position);
			addrAdapter.notifyDataSetChanged();
			saveData();
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.i(info, "--Обработка создания меню--");
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(info, "--Обработка выбора пункта меню--");
		if (item.getItemId() == R.id.mAdd) {
			AddAddr();

		} /*else if (item.getItemId() == R.id.mAdvCheck) {
			Toast.makeText(getApplicationContext(), "advChk",
					Toast.LENGTH_SHORT).show();
		} else if (item.getItemId() == R.id.action_settings) {
			Intent settingsActivity = new Intent(getBaseContext(),
					PrefActivity.class);
			startActivity(settingsActivity);
		}*/
		return true;

	}

	public void EditAddr(final int pos) {
		Log.i(info, "Добавляем новый адрес");
		// TODO Auto-generated method stub
		custom = new Dialog(Main.this);
		custom.setContentView(R.layout.dialog);
		fIP = (EditText) custom.findViewById(R.id.eIP);
		fPort = (EditText) custom.findViewById(R.id.ePort);
		savebtn = (Button) custom.findViewById(R.id.savebtn);
		canbtn = (Button) custom.findViewById(R.id.canbtn);

		fIP.setText(addresses.get(pos).IP);
		fPort.setText(Integer.toString(addresses.get(pos).port));

		custom.setTitle(addresses.get(pos).getFS());
		savebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Log.i(info, "Тыкнули ОК");
				// TODO Auto-generated method stub
				String ip = fIP.getText().toString();
				int port = Integer.parseInt(fPort.getText().toString());
				// addresses.add(new Address(ip, port, 0, false));

				addresses.set(pos, new Address(ip, port, 0, false));
				Log.i(info, "Добавляем новый адрес");
				saveData();
				addrAdapter.notifyDataSetChanged();
				Log.i(info, "Обновляем список");
				custom.dismiss();
				Log.i(info, "Закрываем диалог");
			}

		});
		canbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Log.i(info, "Тыкнули Отмена");
				// TODO Auto-generated method stub
				custom.dismiss();

			}
		});
		Log.i(info, "Показываем диалог");
		custom.show();

	}

	// EditAddr end

	public void AddAddr() {
		Log.i(info, "Добавляем новый адрес");
		// TODO Auto-generated method stub
		custom = new Dialog(Main.this);
		custom.setContentView(R.layout.dialog);
		fIP = (EditText) custom.findViewById(R.id.eIP);
		fPort = (EditText) custom.findViewById(R.id.ePort);
		savebtn = (Button) custom.findViewById(R.id.savebtn);
		canbtn = (Button) custom.findViewById(R.id.canbtn);
		custom.setTitle("Новый адрес");
		savebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Log.i(info, "Тыкнули ОК");
				// TODO Auto-generated method stub
				String ip = fIP.getText().toString();
				int port = Integer.parseInt(fPort.getText().toString());
				addresses.add(new Address(ip, port, 0, false));
				Log.i(info, "Добавляем новый адрес");
				saveData();
				addrAdapter.notifyDataSetChanged();
				Log.i(info, "Обновляем список");
				custom.dismiss();
				Log.i(info, "Закрываем диалог");
			}

		});
		canbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Log.i(info, "Тыкнули Отмена");
				// TODO Auto-generated method stub
				custom.dismiss();

			}
		});
		Log.i(info, "Показываем диалог");
		custom.show();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		Log.i(info, "--Обработка создания контекстного меню--");
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	public static boolean available(Address addr) {
		try {
			Log.i(info, "Проверка порта на доступность");
			// Try to create the Socket on the given port.
			String txt = "CHECK - " + addr.IP + ":"
					+ Integer.toString(addr.port);

			Log.d("CHK", txt);
			Socket socket = new Socket();
			SocketAddress sa = new InetSocketAddress(addr.IP, addr.port);
			socket.connect(sa, 500);
			// addr.active = true;
			socket.close();
			// If we arrive here, the port is open!
			Log.d("CHK", addr.IP + ":" + Integer.toString(addr.port)
					+ " active");
			return true;
			// Don't forget to close it

		} catch (IOException e) {
			// addr.active = false;
			Log.d("CHK", addr.IP + ":" + Integer.toString(addr.port)
					+ " NOT active");
			return false;
			// Failed to open the port. Booh.
		}
	}

	public void onClickCheck(View v) {
		// Или false, чтобы скрыть
		Log.i(info, "Начинаем проверку");
		mt = new MyTask();
		mt.execute();
		/*
		 * for (int i = 0; i <= addresses.size()-1; i++) {
		 * 
		 * Log.d("chk", Integer.toString(i)); Address a = addresses.get(i);
		 * String txt = "CHECK - "+a.IP+":"+Integer.toString(a.port);
		 * Toast.makeText(getApplicationContext(),txt,
		 * Toast.LENGTH_LONG).show(); //a.active = available(a);
		 * //addresses.set(i, a); }
		 */

	}

	class MyTask extends AsyncTask<Void, Integer, Void> {
		private ProgressDialog pdia;

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			Log.i(info, "Обновление прогресса");
			super.onProgressUpdate(values);

			pdia.setProgress(values[0]);
		}

		@Override
		protected void onPreExecute() {
			Log.i(info, "Подготовка к проверке");
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
			pdia = new ProgressDialog(Main.this);
			pdia.setMessage("Loading...");
			pdia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// устанавливаем максимум
			pdia.setMax(addresses.size());
			pdia.show();
			// tvHelloWorld.setText("Begin");
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.d("tst", Integer.toString(addresses.size()));

			for (int i = 0; i <= addresses.size() - 1; i++) {

				Address a = addresses.get(i);

				String txt = "CHECK - " + a.IP + ":" + Integer.toString(a.port);
				Log.d("chk", txt);

				// tvHelloWorld.setText(txt);
				a.active = available(a);
				publishProgress(i + 1);
				// addresses.set(i, a);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(info, "Обработка после проверки");
			super.onPostExecute(result);
			pdia.dismiss();
			setProgressBarIndeterminateVisibility(false);

			// tvHelloWorld.setText("End");
			addrAdapter.notifyDataSetChanged();
		}
	}

}
