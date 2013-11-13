package com.akeinhell.portcheck;

public class Address {

	String IP;
	int port;
	int lastcheck;
	int image;
	boolean active;

	Address(String _IP, int _port, int _lastcheck,  boolean _active) {
		IP 			= _IP;
		port 		= _port;
		lastcheck 	= _lastcheck;
		active 		= _active;
		image 		= android.R.drawable.presence_offline;
		
		 
	}
	public String getFS(){
		return IP+":"+Integer.toString(port);
		
	}

}
