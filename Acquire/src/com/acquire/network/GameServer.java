package com.acquire.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.acquire.board.Board;
import com.acquire.board.Game;
import com.acquire.config.Config;
import com.acquire.config.Config.Strategy;
import com.acquire.game.tree.action.AdminProxy;
import com.acquire.game.tree.action.AdminTreeInspector;
import com.acquire.game.tree.action.AdminTreeInspectorImpl;
import com.acquire.game.tree.action.GameTreeExecutor;
import com.acquire.game.tree.action.GameTreeExecutorImpl;
import com.acquire.game.tree.action.PlayerTreeInspector;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;
import com.acquire.request.AdminInputHandler;

public class GameServer {
	private static int playerCount = 0;
	private static AdminTreeInspector adminTreeInspector;
	private static PlayerTreeInspector playerTreeInspector;
	private static List<Player> players;
	private static Player currentPlayer;

	Board board;

	public static void main(String[] args) throws IOException {
		GameServer gameServer = new GameServer();
		players = new ArrayList<>();
		currentPlayer = new Player();
		adminTreeInspector = new AdminProxy();

		System.out.println("Started game server... waiting for connection... ");
		class ClientConnection implements Runnable {
			private Socket client;
			private GameServer gameServer;

			public ClientConnection(Socket client, GameServer gameServer) {
				this.client = client;
				this.gameServer = gameServer;
			}

			public void run() {
				BufferedReader inFromClient;
				AdminInputHandler adminInputHandler = new AdminInputHandler();
				try {

					inFromClient = new BufferedReader(new InputStreamReader(
							client.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(
							client.getOutputStream());
					while (true) {
						System.out.println(currentPlayer.getName());
						String clientSentence = inFromClient.readLine();
						adminInputHandler.parseInput(
								adminTreeInspector.getRoot(), clientSentence,
								adminTreeInspector, currentPlayer);
						if (adminTreeInspector.isEnd())
							break;
						gameServer.rotate(adminTreeInspector.getRoot()
								.getState().getBoard());
						// gameServer.generateGameTree(adminTreeInspector.getRoot().getState().getBoard());
						adminInputHandler.writeResponse(
								adminTreeInspector.getRoot(), currentPlayer,
								null, null);
						// if (clientSentence != null)
						// game.processRequest(new
						// StringBuilder(clientSentence));
					}
					String winner = adminTreeInspector
							.getWinner(adminTreeInspector.getRoot().getState()
									.getBoard());
					System.out.println("The winner is :" + winner);
				} catch (IOException e) {
					try {
						adminInputHandler
								.writeError("Format error in xml input");
					} catch (TransformerConfigurationException e1) {
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (ParserConfigurationException e) {
					try {
						adminInputHandler
								.writeError("Format error in xml input");
					} catch (TransformerConfigurationException e1) {
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (TransformerException e) {
					try {
						adminInputHandler
								.writeError("Format error in xml input");
					} catch (TransformerConfigurationException e1) {
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (SAXException e) {
					try {
						adminInputHandler
								.writeError("Format error in xml input");
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
		// }
		ServerSocket server = null;
		try {
			server = new ServerSocket(Integer.parseInt(args[0]));
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + args[0]);
			System.err.println(e);
			System.exit(1);
		} catch (NumberFormatException e) {
			System.err.println("Could not listen on port: " + args[0]);
			System.err.println(e);
			System.exit(1);
		}

		Socket client = null;
		while (true) {
			try {
				client = server.accept();
			} catch (IOException e) {
				System.err.println("Server did not accept client");
				System.err.println(e);
				System.exit(1);
			}
			if (playerCount < Config.MAX_PLAYERS) {
				System.out.println("Connected " + (playerCount + 1)
						+ " players");
				playerCount++;
				Player player = new Player();
				player.setName("player" + playerCount);
				player.setClientID(client);
				players.add(player);
				currentPlayer = players.get(0);
				Thread t = new Thread(new ClientConnection(client, gameServer));
				t.start();
			}
			if (playerCount == Config.MAX_PLAYERS) {

				Board board = gameServer.initialize();
				gameServer.generateGameTree(board);
				playerCount++;
			}
		}
		// adminTreeInspector.init(players);
		// currentPlayer = players.get(0);
		// System.out.println("initialzed");
	}

	public void rotate(Board board) {
		players.remove(0);
		players.add(currentPlayer);
		currentPlayer = players.get(0);
	}

	public void generateGameTree(Board board) throws IOException {
		GameTreeExecutor gameTreeExecutor = new GameTreeExecutorImpl();
		gameTreeExecutor.setUpIterator(players);
		gameTreeExecutor.setAllTiles(board);
		AdminInputHandler adminInputHandler = new AdminInputHandler();
		StateClient stateClient = gameTreeExecutor.setupTree();

		for (Player player : players)
			stateClient.getState().setPlayerList(player, player.getTile());

		stateClient = gameTreeExecutor.generateTree(board, stateClient,
				players, adminTreeInspector.emptyTiles());
		adminTreeInspector.setRoot(stateClient);
		stateClient.getState().setBoard(board);
		currentPlayer = players.get(0);

		try {
			adminInputHandler.writeResponse(stateClient, currentPlayer, null,
					null);

		} catch (ParserConfigurationException e) {
			try {
				adminInputHandler.writeError("Format error in xml input");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Board initialize() {
		return adminTreeInspector.init(players);
	}
}
