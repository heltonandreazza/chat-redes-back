package br.furb.conn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {

	private String server = "";
	private int udpPort;
	private int tcpPort;

	public Connection() {
		setServer("larc.inf.furb.br");
		setUdpPort(1011);
		setTcpPort(1012);
	}

	public String getServer() {
		return server;
	}

	private void setServer(String server) {
		this.server = server;
	}

	public int getUdpPort() {
		return udpPort;
	}

	private void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	private void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	/**
	 * Metodo envia/recebe tcp
	 * 
	 * @param str
	 * @return Mensagem de resposta
	 */
	public String tcpMSG(String str) throws UnknownHostException, IOException {
		String reply = "";

		// Cria um socket TCP para conexao com server
		Socket sock = new Socket(getServer(), getTcpPort());

		// Coloca os dados em um buffer e envia para o servidor
		DataOutputStream d = new DataOutputStream(sock.getOutputStream());
		d.write(str.getBytes("UTF-8"));

		// Prepara um buffer para receber a resposta do servidor
		InputStreamReader s = new InputStreamReader(sock.getInputStream());
		BufferedReader rec = new BufferedReader(s);

		// Le os dados enviados pela aplicacao servidora
		String rBuf = rec.readLine();
		InetAddress ip = sock.getInetAddress();
		int port = sock.getPort();

		// Armazena a resposta recebida
		// reply = ip + ":" + port + ": " + rBuf;
		reply = rBuf;

		// Encerra a conexao com o servidor
		sock.close();

		// Retorna a resposta
		return reply;
	}

	/**
	 * Metodo envia
	 * 
	 * @param str
	 * @return Mensagem de resposta
	 */
	public void udpMSG(String str) throws IOException {
		String reply = "";

		// Coloca os dados em um buffer
		byte[] msg = new byte[str.length()];
		msg = str.getBytes("UTF-8");

		// Prepara um pacote com o buffer e as informacoes do destinatario
		InetAddress ip = InetAddress.getByName(getServer());
		DatagramPacket pack = new DatagramPacket(msg, msg.length, ip, getUdpPort());

		// Cria um socket UDP e envia o pacote para larc.inf.furb.br:1012
		DatagramSocket socket = new DatagramSocket();
		socket.send(pack);

		// Encerra o socket
		socket.close();
	}

	private String receiveUDP() throws IOException {
		String str = "";

		// Prepara um buffer para receber dados
		byte[] r = new byte[1024];
		DatagramPacket pack = new DatagramPacket(r, r.length);

		// Cria um socket UDP para receber dados escutando a porta 1011
		DatagramSocket socket = new DatagramSocket(getUdpPort());

		// Le os dados enviados pela aplicacao
		socket.receive(pack);

		// Armazena os dados recebidos
		String buf = new String(pack.getData(), 0, pack.getLength());
		InetAddress ip = pack.getAddress();
		int port = pack.getPort();

		str = ip + ":" + port + ": " + buf;

		return str;
	}

}