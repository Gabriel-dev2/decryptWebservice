package com.teste.imp;

import javax.xml.ws.Endpoint;

public class CryptographerServerPublisher {
	
	public static void main(String[] args)
	  {
	    Endpoint.publish("http://127.0.0.1:9876/com.teste.imp",
	    new CryptographerServerImpl());
	  }
}
