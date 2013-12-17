package com.acquire.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.acquire.exception.AcquireException;
import com.acquire.request.RequestHandler;

public class GameClient {
	public static void main(String args[]) throws AcquireException {
		RequestHandler requestHandler = new RequestHandler();
		String sentence;
		String modifiedSentence;
		try {
			BufferedReader inFromUser = new BufferedReader(
					new InputStreamReader(System.in));

			Socket clientSocket = new Socket(args[0], Integer.parseInt(args[1]));

			while (true) {

				DataOutputStream outToServer = new DataOutputStream(
						clientSocket.getOutputStream());

				BufferedReader inFromServer = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));

				modifiedSentence = inFromServer.readLine();

				System.out.println("FROM SERVER: " + modifiedSentence);

				sentence = requestHandler.start(modifiedSentence, args[2]);
				outToServer.writeBytes(sentence + '\n');

				// if(modifiedSentence.equals("quit\n")) clientSocket.close();

				// sock = new Socket("127.0.0.1", 5432);

				// BufferedReader fromUser = new BufferedReader(new
				// InputStreamReader(
				// System.in));
				// OutputStream ostream = sock.getOutputStream();
				// PrintWriter pwrite = new PrintWriter(ostream, true);
				// InputStream istream = sock.getInputStream();
				// BufferedReader receiveRead = new BufferedReader(
				// new InputStreamReader(istream));
				// // System.out.println("Enter the XML: ");
				// String receiveMessage, sendMessage;
				// StringBuilder string = new StringBuilder();
				// RequestHandler requestHandler = new RequestHandler();
				// String str = "";
				// while (true) {
				// // if (str.equals("")) {
				// pwrite.println(string.toString() + "\n");
				// System.out.flush();
				// if ((receiveMessage = receiveRead.readLine()) != null) {
				// System.out.println(receiveMessage);
				// }
				//
				// str = requestHandler.start(receiveMessage);
				// if (str.equals("")) {
				//
				// }
				// System.out.println("echo:" + str);
				// string.append(str);
				// string.append(" ");
				// pwrite.println(string.toString() + "\n");
				// System.out.flush();
				//
				// string.setLength(0);
				//
				// }
				// } catch (UnknownHostException e) {
				//
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			}
		} catch (UnknownHostException e) {

			try {
				requestHandler.writeError("Unknown Host");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			try {
				requestHandler.writeError("Format error in xml input");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (SAXException e) {
			try {
				requestHandler.writeError("Format error in xml input");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (TransformerException e) {
			try {
				requestHandler.writeError("Format error in xml input");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			try {
				requestHandler
						.writeError("An error occured while processing request");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (NumberFormatException e) {
			try {
				requestHandler
						.writeError("Port number should be a number");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
}