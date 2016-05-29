package br.furb.conn;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class KeepAlive extends Thread {
	int user;
	String pass = "";
	Connection con;

	public KeepAlive(int user, String pass) {
		setUser(user);
		setPass(pass);
		this.con = new Connection();
	}

	public void setUser(int userId) {
		this.user = userId;
	}

	private int getUser() {
		return user;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	private String getPass() {
		return pass;
	}

	public void stayConnected(String str) throws UnknownHostException, IOException {
		// Cria um socket TCP para conexao com server
		Socket sock = new Socket(con.getServer(), con.getTcpPort());

		// Coloca os dados em um buffer e envia para o servidor
		DataOutputStream d = new DataOutputStream(sock.getOutputStream());
		d.write(str.getBytes("UTF-8"));

		// Encerra a conexao com o servidor
		sock.close();
	}

	public void run() {

		while (true) {
			try {
				stayConnected("GET USERS " + getUser() + ":" + getPass());
				Thread.sleep(6000);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
