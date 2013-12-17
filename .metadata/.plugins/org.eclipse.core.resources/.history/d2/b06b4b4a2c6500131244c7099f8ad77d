package com.graph.operations;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientService {
	public static void main(String argv[]) throws Exception {
		Socket sock = new Socket("127.0.0.1", 5432);
		BufferedReader fromUser = new BufferedReader(new InputStreamReader(
				System.in));
		OutputStream ostream = sock.getOutputStream();
		PrintWriter pwrite = new PrintWriter(ostream, true);
		InputStream istream = sock.getInputStream();
		BufferedReader receiveRead = new BufferedReader(new InputStreamReader(
				istream));
		System.out.println("Enter the XML: ");
		String receiveMessage, sendMessage;
		StringBuilder string = new StringBuilder();
		String str = fromUser.readLine();
		while (true) {
			if (str.equals("")) {
				pwrite.println(string.toString());
				System.out.flush();
				if ((receiveMessage = receiveRead.readLine()) != null) {
					System.out.println(receiveMessage);
				}
				string.setLength(0);
			}
			string.append(str);
			string.append(" ");
			str = fromUser.readLine();
			
			
		}
	}
}
