package com.acquire.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.acquire.exception.AcquireException;

public class GameClient {
	public static void main(String argv[]) throws AcquireException {
		while (true) {
			try {
				// Bind the socket
				Socket sock = new Socket("127.0.0.1", 4444);
				
				// Read from input
				BufferedReader fromUser = new BufferedReader(
						new InputStreamReader(System.in));
				
				System.out.println("Enter the XML: ");
				StringBuilder sentString = new StringBuilder();
				String line = "";
				while ((line = fromUser.readLine()) != null
						&& line.length() != 0) {

					sentString.append(line);
					sentString.append("\n");
				}

				// Writer for writing to socket
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						sock.getOutputStream()));
				bw.write(sentString.toString() + "\n");
				bw.flush();
				System.out.println("String entered by user:" + sentString);
				
				StringBuilder receivedString=new StringBuilder();
				// Reader for reading from socket
				BufferedReader br = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				while ((line = br.readLine()) != null && line.length() != 0) {

					receivedString.append(line);
					receivedString.append("\n");
				}

				String serverMessage = receivedString.toString();
				System.out.println("Message received from the server : "
						+ serverMessage);

			} catch (IOException io) {
				throw new AcquireException("IO Error");
			}
		}
	}
}
