package com.mychat.webservices;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

	public int id;
	public String password;
	public int sendId;
	public String message;
	
	public User() {};
	
	public User(int id, String password, int sendId, String message) {
		this.id = id;
		this.password = password;
		this.sendId= sendId;
		this.message = message;
	}	
	
	
}
