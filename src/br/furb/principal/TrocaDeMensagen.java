package br.furb.principal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONObject;

import br.furb.conn.Connection;
import br.furb.conn.KeepAlive;

public class TrocaDeMensagen {

	static Connection con = new Connection();

	/**
	 * Obter do servidor, estabelecendo uma conexao a cada 6 segundos, a lista
	 * de usários conectados atraves da seguinte requisicao TCP (porta 1012)
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 */
	public static String getUsers(int userId, String passwd) throws UnknownHostException, IOException {
		String reply = con.tcpMSG("GET USERS " + userId + ":" + passwd);
		String[] parts = reply.split(":");

		JSONArray arr = new JSONArray();
		
		for (int i = 0; i < parts.length-2; i += 3) {
			JSONObject obj = new JSONObject();
			obj.put("id", parts[i]);
			obj.put("name", parts[i+1]);
			obj.put("wins", parts[i+2]);

			arr.put(obj);
		}

		return arr.toString();
	}

	/**
	 * Obter do servidor uma mensagem (a mais antiga) destinada ao usuario
	 * atraves da seguinte requisicao TCP (porta 1012)
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 */
	public static String getMessage(int userId, String passwd) throws UnknownHostException, IOException {
		String str = con.tcpMSG("GET MESSAGE " + userId + ":" + passwd);
		if (str.equals(":")) {			
			str = "Nenhuma mensagem encontrada";
			
			return new JSONObject().put("message", str).toString();
		}
		
		String[] parts = str.split(":");

		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		
		obj.put("user", parts[0]);
		obj.put("message", parts[1]);
		
		arr.put(obj);
		
		return arr.toString();
	}

	/**
	 * Enviar ao servidor uma mensagem destinada a um usuário, ou a todos,
	 * através de uma mensagem UDP (porta 1011)
	 * 
	 * @param userId
	 * @param passwd
	 * @param userId2
	 * @param msg
	 */
	public static String sendMessage(int userId, String passwd, int userId2, String msg) throws Exception {
		if (msg.isEmpty())
			throw new Exception("Mensagem inválida ou vazia");

		con.udpMSG("SEND MESSAGE " + userId + ":" + passwd + ":" + userId + ":" + msg);
		return new JSONObject().put("message", "send message \'" + msg + "\' to user " + userId2).toString();
	}

	/**
	 * obter do servidor a lista de usuários que estão participando do Jogo de
	 * Cartas 21 através da seguinte requisição TCP (porta 1012)
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static String getPlayers(int userId, String passwd) throws UnknownHostException, IOException {
		String str = "ID User\tStatus\n";
		String reply = con.tcpMSG("GET PLAYERS " + userId + ":" + passwd);
		String[] parts = reply.split(":");

		int i = 0;

		for (String string : parts) {
			str += parts[i] + "\t";

			if ((i + 1) % 2 == 0)
				str += "\n";

			i++;
		}

		return str;
	}

	/**
	 * obter do servidor mais uma carta (válida somente para o usuário no status
	 * GETTING) através da seguinte requisição TCP (porta 1012)
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static String getCard(int userId, String passwd) throws UnknownHostException, IOException {
		String str = "";
		String reply = con.tcpMSG("GET CARD " + userId + ":" + passwd);
		if (reply.equals(":"))
			return "Nao e possivel obter uma carta";

		String[] parts = reply.split(":");

		int i = 0;

		for (String string : parts) {
			str += parts[i] + "\t";

			if ((i + 1) % 2 == 0)
				str += "\n";

			i++;
		}

		return str;

	}

	/**
	 * enviar ao servidor uma indicação referente à participação do jogo através
	 * de uma mensagem UDP (porta 1011)
	 * 
	 * @param userId
	 * @param passwd
	 * @param msg
	 * @throws Exception
	 */
	public static void sendGame(int userId, String passwd, String msg) throws Exception {
		if (msg.isEmpty())
			throw new Exception("Mensagem inválida ou vazia");

		con.udpMSG("SEND GAME " + userId + ":" + passwd + ":" + msg);
	}

	public static void main(String[] args) throws Exception {
		// 2832:kxxsm
		int userId = 0;
		String passwd = "";
		String help = "";
		String strSend = "";
		boolean bol = true;

		try {
			System.out.println("Digite o id do seu suario e a senha. Ex: 1234:bcd");
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();

			while (bol) {
				if (s.isEmpty() || s.split(":").length > 2) {
					// if (s.matches("/[0-9]+:[a-z]+/")) {
					System.out.println("\nUsuario invalido!\nDigite o id do seu usuario e a senha. Ex: 1234:bcd");
					bufferRead = new BufferedReader(new InputStreamReader(System.in));
					s = bufferRead.readLine();
				} else {
					System.out.println("Usuario OK!\n");
					String[] parts = s.split(":");
					userId = Integer.parseInt(parts[0]);
					passwd = parts[1];
					bol = false;
				}
			}
			KeepAlive kp = new KeepAlive(userId, passwd);
			kp.start();
			bol = true;

			System.out.println("Comandos disponiveis:\n");
			help = "getUsers\n" + "getMessage\n" + "sendMessage userIdDoDestino mensagem\n" + "getPlayers \n"
					+ "getCard \n" + "sendGame ENTER | STOP | QUIT\n" + "exit\n";
			System.out.println(help);
			while (bol) {
				System.out.print(":~$ ");
				bufferRead = new BufferedReader(new InputStreamReader(System.in));
				s = bufferRead.readLine();
				String[] parts = s.split(" ");

				switch (parts[0]) {
				case "getUsers":
					System.out.println(getUsers(userId, passwd));
					break;

				case "getMessage":
					System.out.println(getMessage(userId, passwd));
					break;

				case "sendMessage":
					for (int i = 2; i < parts.length; i++) {
						strSend += parts[i] + " ";
					}
					sendMessage(userId, passwd, Integer.parseInt(parts[1]), strSend);
					break;

				case "getPlayers":
					System.out.println(getPlayers(userId, passwd));
					break;

				case "getCard":
					System.out.println(getCard(userId, passwd));
					break;

				case "sendGame":
					sendGame(userId, passwd, parts[1]);
					break;

				case "exit":
					bol = false;
					break;

				default:
					System.out.println("Comando invalido!\n" + help);
					break;
				}
			}
			System.out.println("Progama finalizado!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
