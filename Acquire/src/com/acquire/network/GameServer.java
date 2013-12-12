package com.acquire.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.acquire.request.GameHandler;
import com.acquire.request.RequestHandler;

public class GameServer {
	static GameHandler game=new GameHandler();
	public static void main(String[] args) {
		GameServer gameServer = new GameServer();
		

		class ClientConnection implements Runnable {
			private Socket client;
			private GameServer gameServer;

			public ClientConnection(Socket client, GameServer gameServer) {
				this.client = client;
				this.gameServer = gameServer;
			}

			public void run() {
				BufferedReader inFromClient;
				try {
					// Reader for message received from client
					inFromClient = new BufferedReader(new InputStreamReader(
							client.getInputStream()));
					StringBuilder string = new StringBuilder();
					String clientSentence = "";
					while ((clientSentence = inFromClient.readLine()) != null
							&& clientSentence.length() != 0) {

						string.append(clientSentence);
						string.append("\n");
					}
					System.out.println("String recieved from client:" + string);
					game.processRequest(string);

					// is = new ByteArrayInputStream(xml.getBytes());
					// String xml = string.toString();
					// InputSource source = new InputSource(
					// new ByteArrayInputStream(xml.getBytes()));
					// DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					// .newInstance();
					// DocumentBuilder dBuilder =
					// dbFactory.newDocumentBuilder();
					// Document doc = dBuilder.parse(source);
					// doc.getDocumentElement().normalize();

					// Writer for message sent to client
					BufferedWriter bw = new BufferedWriter(
							new OutputStreamWriter(client.getOutputStream()));
					System.out
							.println("Message sent to the client : " + string);
					bw.write(string.toString() + "\n");
					bw.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		ServerSocket server = null;
		try {
			server = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + 4444);
			System.err.println(e);
			System.exit(1);
		}

		Socket client = null;
		while (true) {
			try {
				client = server.accept();
				System.out.println("Client connected");
			} catch (IOException e) {
				System.err.println("Server did not accept client");
				System.err.println(e);
				System.exit(1);
			}
			Thread t = new Thread(new ClientConnection(client, gameServer));
			t.start();
		}

	}

}
