package com.acquire.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Hotel;
import com.acquire.board.Tile;
import com.acquire.config.Config.Strategy;
import com.acquire.exception.AcquireException;
import com.acquire.factory.BoardFactory;
import com.acquire.factory.PlayerFactory;
import com.acquire.game.tree.action.PlayerProxy;
import com.acquire.game.tree.action.PlayerTreeInspector;
import com.acquire.game.tree.state.StateClient;
import com.acquire.player.Player;
import com.acquire.player.Share;
import com.acquire.player.strategy.LargestAlphaStrategy;
import com.acquire.player.strategy.PlayerStrategy;
import com.acquire.player.strategy.RandomPlayerStrategy;
import com.acquire.player.strategy.SequentialPlayerStrategy;
import com.acquire.player.strategy.SmallestAntiStrategy;

public class RequestHandler {
	PlayerTreeInspector playerTreeInspector = new PlayerProxy();
	
	public String start(String string, String strategy)
			throws ParserConfigurationException, UnsupportedEncodingException,
			SAXException, IOException, AcquireException, TransformerException {
		PlayerStrategy playerStrategy = new SequentialPlayerStrategy();
		if (strategy.equals("random")) {
			playerStrategy = Strategy.RANDOM.getMove();
		} else if (strategy.equals("sequential")) {
			playerStrategy = Strategy.SEQ.getMove();
		} else if (strategy.equals("smallest-anti")) {
			playerStrategy = Strategy.SMALLESTANTI.getMove();
		} else if (strategy.equals("largest-alpha")) {
			playerStrategy = Strategy.LARGESTALPHA.getMove();
		} else if (strategy.equals("worth")) {
			playerStrategy = Strategy.WORTH.getMove();
		} else if (strategy.equals("shares")) {
			playerStrategy = Strategy.SHARES.getMove();
		}
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder;

		builder = builderFactory.newDocumentBuilder();

		Document doc = builder.newDocument();

		doc = builder.parse(new InputSource(new ByteArrayInputStream(string
				.toString().getBytes("utf-8"))));
		doc.getDocumentElement().normalize();
		Board board = getBoard(doc);
		Player player = getPlayer(doc);
		setShares(player, doc);
		List<String> hotelList = getHotels(doc);

		StateClient root = new StateClient();
		root.getState().setBoard(board);
		root.setNextPlayer(player);
		root.getState().setShareCombinations();
		root.getState().setHotels(hotelList);

		StateClient state = root;
		root.setPath(new ArrayList<String>());

		root = generateChildren(state, player, 0, board.getEmptyTiles());

		StateClient node = playerTreeInspector.pickState(root, player);
		
		return writeResponse(node.getTile(), node.getHotel(), node.getShares());

	}

	public Board getBoard(Document document) {
		Board board = Board.getInstance();

		new Chain();
		NodeList tiles = document.getElementsByTagName("tile");
		for (int i = 0; i < tiles.getLength(); i++) {
			Element element = (Element) tiles.item(i);
			Node node = element.getParentNode();
			Hotel hotel = new Hotel();
			if (element.getParentNode().getNodeName().equals("hotel")) {
				hotel.setLabel(element.getParentNode().getAttributes()
						.getNamedItem("name").getNodeValue());
				board.getBoard().put(
						element.getAttribute("column")
								+ element.getAttribute("row"), hotel);
				Chain.setChain(
						element.getParentNode().getAttributes()
								.getNamedItem("name").getNodeValue(),
						element.getAttribute("column")
								+ element.getAttribute("row"));
			} else if (element.getParentNode().getNodeName().equals("board")) {
				hotel.setLabel("singleton");
				board.getBoard().put(
						element.getAttribute("column")
								+ element.getAttribute("row"), hotel);

			}
		}
		return board;
	}

	public Player getPlayer(Document doc) throws AcquireException {
		Board board = BoardFactory.getBoard();
		Element element = (Element) doc.getElementsByTagName("player").item(0);
		Player player = PlayerFactory.getInstance();
		player.setCash(Integer.parseInt(element.getAttribute("cash")));
		NodeList shares = element.getElementsByTagName("share");
		for (int i = 0; i < shares.getLength(); i++) {
			Element share = (Element) shares.item(i);
			if (!share.getAttribute("name").equals(""))
				player.setShare(share.getAttribute("name"),
						Integer.parseInt(share.getAttribute("count")));
		}
		NodeList nodeList = doc.getElementsByTagName("tile");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element tileElement = (Element) nodeList.item(i);
			Node node = tileElement.getParentNode();
			if (tileElement.getParentNode().getNodeName().equals("take-turn")) {
				if (board.getBoard().get(
						tileElement.getAttribute("column")
								+ tileElement.getAttribute("row")) == null)
					player.setTile(tileElement.getAttribute("row"),
							tileElement.getAttribute("column"));
				else {
					throw new AcquireException("Repeated tiles in player");
				}
			}
		}
		return player;
	}

	public void setShares(Player player, Document doc) throws AcquireException {
		NodeList shares = doc.getElementsByTagName("share");
		new Share();
		for (int i = 0; i < shares.getLength(); i++) {
			Element element = (Element) shares.item(i);
			if (element.getParentNode().getNodeName().equals("take-turn")) {
				if (player.getShares()
						.containsKey(element.getAttribute("name"))) {
					if (player.getShare(element.getAttribute("name"))
							+ Integer.parseInt(element.getAttribute("count")) > 25) {
						throw new AcquireException("Share value for hotel "
								+ element.getAttribute("name")
								+ " is incorrect");
					} else {
						Share.setShare(element.getAttribute("name"),
								Integer.parseInt(element.getAttribute("count")));
					}
				}

			}
		}
	}

	public List<String> getHotels(Document doc) throws AcquireException {
		NodeList hotels = doc.getElementsByTagName("hotel");
		List<String> hotelList = new ArrayList<>();
		for (int i = 0; i < hotels.getLength(); i++) {
			Element element = (Element) hotels.item(i);
			if (!element.getAttribute("label").equals(""))
				if (!Chain.getChain(element.getAttribute("label")).isEmpty()) {
					throw new AcquireException("The hotel "
							+ element.getAttribute("label")
							+ " already has a chain");
				} else {
					hotelList.add(element.getAttribute("label"));
				}
		}
		return hotelList;
	}

	public String writeResponse(Tile place, String shotel, List<String> hotels)
			throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element root = doc.createElement("action");
		if (!hotels.isEmpty()) {
			for (int j = 0; j < hotels.size(); j++) {
				root.setAttribute("hotel" + (j + 1), hotels.get(j));
			}
		}
		// if (!place.isEmpty()) {
		Element placeElement = doc.createElement("place");
		// Tile tile = (Tile) place.get(0);
		placeElement.setAttribute("row", place.getRow());
		placeElement.setAttribute("column", place.getColumn());

		placeElement.setAttribute("hotel", shotel);
		root.appendChild(placeElement);
		// }
		doc.appendChild(root);
		System.out.println("Place tile " + place.getColumn()+place.getRow());
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source;

		source = new DOMSource(doc);
		StreamResult result = new StreamResult(new StringWriter());
		result = new StreamResult(new StringWriter());
		transformer.transform(source, result);
		return result.getWriter().toString();
		// System.out.println(result.getWriter().toString());
	}

	public String writeError(String msg) throws ParserConfigurationException,
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

	public StateClient generateTree(Board board, StateClient stateClient,
			Player player, List<String> remainingTiles, StateClient root) {
		root = stateClient;
		stateClient.getState().setBoard(board);
		StateClient state = root;
		root.setPath(new ArrayList<String>());
		// for (Player name: players) {
		// System.out.println(name.getName());
		// }
		// List<Player> players = Game.getInstance().getGame(board);
		root = generateChildren(state, player, 0, remainingTiles);
		return root;
		// for (int i = 0; i < players.size(); i++) {

		// }
	}

	public StateClient generateChildren(StateClient state, Player player,
			int playerCount, List<String> remainingTiles) {
		if (playerCount == 2) {
			return state;
		}

		// for (Tile tile : tiles) {
		// System.out.print(tile.getColumn()+tile.getRow() + " ");
		// }

		// playerCount++;
		if (playerCount < 1) {
			state.setNextPlayer(player);
			state.setChildren(new ArrayList<StateClient>());
			List<Tile> tiles = state.getNextPlayer().getTile();
			List<Tile> t = new ArrayList<>(tiles);
			for (Tile tile : t) {
				if (!state.getPath().contains(
						tile.getTileLabel(tile.getRow(), tile.getColumn()))) {
					// System.out.println("\nPlayer " + playerCount);
					//
					// System.out.println("Current tile: " + tile.getColumn() +
					// tile.getRow());
					Map<String, Object> result = state.generate(state
							.getState().getBoard(), tile.getRow(), tile
							.getColumn());

					StateClient child = new StateClient();
					child.setMove(result.get("move").toString());

					child.setTile((Tile) result.get("tile"));
					state.getChildren().add(child);
					child.setPlayer(player);
					List<String> newPath = new ArrayList<>(state.getPath());
					if (state.getTile() != null)
						newPath.add(state.getTile().getTileLabel(
								state.getTile().getRow(),
								state.getTile().getColumn()));
					else
						newPath.add("root");
					child.setPath(newPath);
					// System.out.println(child.getPath());

					generateChildren(child, player, playerCount + 1,
							remainingTiles);
					// System.out.println("\nPlayer Count:"+playerCount);
				}
			}
		} else {
			List<Tile> remaining = getAllTiles(state.getState().getBoard(),
					player.getTile());
			for (Tile tile : remaining) {

				if (!state.getPath().contains(
						tile.getTileLabel(tile.getRow(), tile.getColumn()))) {
					// System.out.println("\nPlayer " + playerCount);
					//
					// System.out.println("Current tile: " + tile.getColumn() +
					// tile.getRow());
					Map<String, Object> result = state.generate(state
							.getState().getBoard(), tile.getRow(), tile
							.getColumn());

					StateClient child = new StateClient();
					child.setMove(result.get("move").toString());

					child.setTile((Tile) result.get("tile"));
					state.getChildren().add(child);
					child.setPlayer(player);
					List<String> newPath = new ArrayList<>(state.getPath());
					if (state.getTile() != null)
						newPath.add(state.getTile().getTileLabel(
								state.getTile().getRow(),
								state.getTile().getColumn()));
					else
						newPath.add("root");
					child.setPath(newPath);
					// System.out.println(child.getPath());

					generateChildren(child, player, playerCount + 1,
							remainingTiles);
					// System.out.println("\nPlayer Count:"+playerCount);
				}
			}
		}
		return state;
	}

	public List<Tile> getAllTiles(Board board, List<Tile> playerTiles) {
		List<Tile> tiles = new ArrayList<>();
		for (String tile : board.getEmptyTiles()) {
			Tile t = new Tile();
			t.setColumn(tile.substring(0, tile.length() - 1));
			t.setRow(tile.substring(tile.length() - 1));
			tiles.add(t);
		}
		return tiles;
	}

}
