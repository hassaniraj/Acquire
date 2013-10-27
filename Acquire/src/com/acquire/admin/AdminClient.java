package com.acquire.admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Game;
import com.acquire.board.Hotel;
import com.acquire.board.Labels;
import com.acquire.board.Tile;
import com.acquire.factory.PlayerFactory;
import com.acquire.player.Player;

public class AdminClient {
	private static Administrator administrator;
	private Board board;
	private Game game;
	private String currentStage;
	private boolean isSetupDone = false;

	public void start(String string) {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.newDocument();

			doc = builder.parse(new InputSource(new ByteArrayInputStream(string
					.toString().getBytes("utf-8"))));
			doc.getDocumentElement().normalize();
			
			administrator = IAdministrator.getInstance();
			this.getElements(doc);
		} catch (IOException e) {
			try {
				this.writeError("Format error in xml input");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			try {
				this.writeError("Format error in xml input");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (NullPointerException e) {
			try {
				this.writeError("Error in performing action.");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (SAXException e) {
			try {
				this.writeError("Format error in xml input");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (TransformerException e) {
			try {
				this.writeError("Format error in xml input");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void getElements(Document doc) throws ParserConfigurationException,
			TransformerException, IOException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		try {
		if (doc.getElementsByTagName("setup").getLength() != 0) {
			Element element = (Element) doc.getElementsByTagName("setup").item(
					0);
			List<Player> playersList = new ArrayList<>();
			if (element.hasAttribute("player1")) {
				Player player = PlayerFactory.getInstance();
				player.setName(element.getAttribute("player1"));
				playersList.add(player);
			}
			if (element.hasAttribute("player2")) {
				Player player = PlayerFactory.getInstance();
				player.setName(element.getAttribute("player2"));
				playersList.add(player);
			}
			if (element.hasAttribute("player3")) {
				Player player = PlayerFactory.getInstance();
				player.setName(element.getAttribute("player3"));
				playersList.add(player);
			}
			if (element.hasAttribute("player4")) {
				Player player = PlayerFactory.getInstance();
				player.setName(element.getAttribute("player4"));
				playersList.add(player);
			}
			if (element.hasAttribute("player5")) {
				Player player = PlayerFactory.getInstance();
				player.setName(element.getAttribute("player5"));
				playersList.add(player);
			}
			if (element.hasAttribute("player6")) {
				Player player = PlayerFactory.getInstance();
				player.setName(element.getAttribute("player6"));
				playersList.add(player);
			}
			game = Game.getInstance();
			board = administrator.startGame(game, playersList);
			doc = builder.newDocument();
			Element root = doc.createElement("state");
			doc = writeBoard(doc, root, board);

			doc = writePlayer(doc, root, game, board);
			doc.appendChild(root);
			writeDoc(doc);
			isSetupDone = true;
			currentStage = "setup";
		} else if (doc.getElementsByTagName("place").getLength() != 0) {
			if ((isSetupDone || currentStage.equals("done")) && !currentStage.equals("buy") && !currentStage.equals("place")) {
			Element element = (Element) doc.getElementsByTagName("place").item(
					0);
			Tile tile = new Tile();
			tile.setRow(element.getAttribute("row"));
			tile.setColumn(element.getAttribute("column"));
			NodeList playerNodeList = element.getElementsByTagName("player");
			for (int i = 0; i < playerNodeList.getLength(); i++) {
				Element node = (Element) playerNodeList.item(i);
				NodeList tileNodeList = node.getElementsByTagName("tile");
				for (int j = 0; j < tileNodeList.getLength(); j++) {
					Element tileNode = (Element) tileNodeList.item(j);
					String row = tileNode.getAttribute("row");
					String column = tileNode.getAttribute("column");
					if (row.equals(element.getAttribute("row"))
							&& column.equals(element.getAttribute("column"))) {
						List<Player> players = game.getGame(board);

//						for (Player p : players) {
//							if (p.getName().equals(node.getAttribute("name"))
//									&& p.getCash() == Integer.parseInt(node
//											.getAttribute("cash"))) {
//								administrator.setTile(board, p, tile,
//										element.getAttribute("hotel"));
//								break;
//							}
//						}
						administrator.setTile(board, administrator.getCurrentPlayer(), tile,
								element.getAttribute("hotel"));
					}
				}
			}
			doc = builder.newDocument();
			Element root = doc.createElement("state");
			doc = writeBoard(doc, root, board);

			doc = writePlayer(doc, root, game, board);
			doc.appendChild(root);
			writeDoc(doc);
			currentStage = "place";
			} else {
				writeError("Perform setup first and follow sequence place - buy - done");
			}
		} else if (doc.getElementsByTagName("buy").getLength() != 0) {
			if (isSetupDone && currentStage.equals("place")) {
			Element element = (Element) doc.getElementsByTagName("buy").item(0);
			Player player = administrator.getCurrentPlayer();
			
			if (element.hasAttribute("share1")) {
				if (Chain.getChain(element.getAttribute("share1")) != null && !Chain.getChain(element.getAttribute("share1")).isEmpty()) {
				if (Labels.getLabels().contains(element.getAttribute("share1"))) {
					administrator.getHotelShares(board, player,
							element.getAttribute("share1"));
				}
				}
				else 
					writeError("The chain " + element.getAttribute("share1") + " does not exist");
			}
			if (element.hasAttribute("share2")) {
				if (Chain.getChain(element.getAttribute("share2")) != null && !Chain.getChain(element.getAttribute("share2")).isEmpty()) {
				if (Labels.getLabels().contains(element.getAttribute("share2"))) {
					administrator.getHotelShares(board, player,
							element.getAttribute("share2"));
				}
				} else 
					writeError("The chain " + element.getAttribute("share2") + " does not exist");
			}
			if (element.hasAttribute("share3")) {
				if (Chain.getChain(element.getAttribute("share3")) != null && !Chain.getChain(element.getAttribute("share3")).isEmpty()) {
				if (Labels.getLabels().contains(element.getAttribute("share3"))) {
					administrator.getHotelShares(board, player,
							element.getAttribute("share3"));
				}
				} else 
					writeError("The chain " + element.getAttribute("share3") + " does not exist");
			}
			doc = builder.newDocument();
			Element root = doc.createElement("state");
			doc = writeBoard(doc, root, board);

			doc = writePlayer(doc, root, game, board);
			doc.appendChild(root);
			writeDoc(doc);
			currentStage = "buy";
			} else {
				writeError("Perform place before buy");
			}
		} else if (doc.getElementsByTagName("done").getLength() != 0) {
			if (isSetupDone && (currentStage.equals("buy") || currentStage.equals("place"))) {
			administrator.done(administrator.getCurrentPlayer(), board);
			doc = builder.newDocument();
			Element root = doc.createElement("state");
			doc = writeBoard(doc, root, board); 

			doc = writePlayer(doc, root, game, board);
			doc.appendChild(root);
			writeDoc(doc);
			currentStage = "done";
			} else {
				writeError("Perform place or buy before done");
			}
		} else {
			writeError("No such operation.");
		}
		}catch (NullPointerException e) {
			try {
				this.writeError("Please start the game using setup and perform action in right sequence place - buy - done");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IllegalArgumentException e) {
			try {
				this.writeError("Please start the game using setup and perform action in right sequence place - buy - done");
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	Document writeBoard(Document doc, Element root, Board board)
			throws ParserConfigurationException, TransformerException {
		Map<String, Hotel> tileList = board.getOccupiedTiles();
		Element rootElement = doc.createElement("board");
		for (String tile : tileList.keySet()) {
			Element tileElement = doc.createElement("tile");
			tileElement.setAttribute("row",
					String.valueOf(tile.substring(tile.length() - 1)));
			tileElement.setAttribute("column",
					tile.substring(0, tile.length() - 1));
			rootElement.appendChild(tileElement);

		}

		Map<String, Set<String>> hotelTiles = board.getHotelTiles();
		for (String hotel : hotelTiles.keySet()) {
			Element hotelElement = doc.createElement("hotel");
			hotelElement.setAttribute("label", hotel);
			Set<String> tiles = hotelTiles.get(hotel);
			for (String tile : tiles) {
				Element tileElement = doc.createElement("tile");
				tileElement.setAttribute("row",
						String.valueOf(tile.substring(tile.length() - 1)));
				tileElement.setAttribute("column",
						tile.substring(0, tile.length() - 1));
				hotelElement.appendChild(tileElement);

			}
			rootElement.appendChild(hotelElement);
		}
		root.appendChild(rootElement);
		// writeDoc(doc);
		return doc;
	}

	Document writePlayer(Document doc, Element root, Game game, Board board)
			throws ParserConfigurationException, TransformerException {

		List<Player> players = game.getGame(board);
		Player currentPlayer = administrator.getCurrentPlayer();
		int index = players.indexOf(currentPlayer);
		List <Player> playerClone = new ArrayList<>(players);
		
		Collections.rotate(playerClone, -index);
		
		for (Player player : playerClone) {
			Element rootElement = doc.createElement("player");
			rootElement.setAttribute("name", player.getName());
			rootElement.setAttribute("cash", String.valueOf(player.getCash()));

			Map<String, Integer> shares = player.getShares();
			for (String share : shares.keySet()) {
				Element shareElement = doc.createElement("share");
				shareElement.setAttribute("name", share);
				shareElement.setAttribute("count",
						String.valueOf(shares.get(share)));
				rootElement.appendChild(shareElement);
			}
			List<Tile> tiles = player.getTile();
			for (Tile tile : tiles) {
				Element tileElement = doc.createElement("tile");
				tileElement.setAttribute("column", tile.getColumn());
				tileElement.setAttribute("row", tile.getRow());
				rootElement.appendChild(tileElement);
			}
			root.appendChild(rootElement);
		}

		// writeDoc(doc);
		return doc;
	}

	void writeDoc(Document doc) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new StringWriter());
		transformer.transform(source, result);
		String msg = result.getWriter().toString();
		System.out.println(msg);
	}

	String writeError(String msg) throws ParserConfigurationException,
			TransformerConfigurationException, IOException {
		StreamResult result = new StreamResult(new StringWriter());
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc;
		// doc = docBuilder.newDocument();
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source;

		try {
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("error");
			doc.appendChild(rootElement);
			rootElement.setAttribute("msg", msg);

			source = new DOMSource(doc);
			result = new StreamResult(new StringWriter());
			transformer.transform(source, result);
			System.out.println(result.getWriter().toString());
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getCurrentStage() {
		return currentStage;
	}
}
