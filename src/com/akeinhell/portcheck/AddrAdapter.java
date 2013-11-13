package com.akeinhell.portcheck;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddrAdapter extends BaseAdapter {
	Context ctx;
	LayoutInflater lInflater;
	ArrayList<Address> objects;

	AddrAdapter(Context context, ArrayList<Address> products) {
		ctx = context;
		objects = products;
		lInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*--------------------------------------------------------------*/
	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ���������� ���������, �� �� ������������ view
	    View view = convertView;
	    if (view == null) {
	      view = lInflater.inflate(R.layout.item, parent, false);
	    }

	    Address p = getAddr(position);

	    // ��������� View � ������ ������ ������� �� �������: ������������, ����
	    // � ��������
	    ((TextView) view.findViewById(R.id.iIP)).setText(p.IP+":"+Integer.toString(p.port));
	    //((TextView) view.findViewById(R.id.tvPrice)).setText(p.price + "");
	    if (p.active){
	    	((ImageView) view.findViewById(R.id.iStatus)).setImageResource(android.R.drawable.presence_online);
	    	view.setBackgroundColor(0xC5E26D);
	    }else{
	    	((ImageView) view.findViewById(R.id.iStatus)).setImageResource(android.R.drawable.presence_busy);
	    	view.setBackgroundColor(0xFF7979);
	    }
	    
	    

	    /*CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
	    // ����������� �������� ����������
	    cbBuy.setOnCheckedChangeListener(myCheckChangList);
	    // ����� �������
	    cbBuy.setTag(position);
	    // ��������� ������� �� �������: � ������� ��� ���
	    cbBuy.setChecked(p.box);*/
	    return view;
	}
	
	Address getAddr(int position) {
	    return ((Address) getItem(position));
	  }

}
