package br.furb.principal;

import java.io.IOException;
import java.net.UnknownHostException;

public class teste {

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println(TrocaDeMensagen.getUsers(8443, "mwryx"));
	}
}
