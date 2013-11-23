package com.acquire.request;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.acquire.admin.AdminClient;
import com.acquire.board.Board;

public class TestHarness {
	static Board board;

	public static void main(String[] args) throws Exception {
		BufferedReader fromUser = new BufferedReader(new InputStreamReader(
				System.in));

		StringBuilder string = new StringBuilder();
		RequestHandler adminClient = new RequestHandler();
		String str = "";
		System.out.println("\n Enter XML: ");
		while (true) {

			str = fromUser.readLine();
			if (str.equals("")) {
				adminClient.start(string.toString());

				string.setLength(0);
				System.out.println("\n Enter XML: ");
			}

			string.append(str);
			string.append(" ");

		}

	}
}
