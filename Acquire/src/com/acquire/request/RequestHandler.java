package com.acquire.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
import com.acquire.exception.AcquireException;
import com.acquire.factory.BoardFactory;
import com.acquire.factory.PlayerFactory;
import com.acquire.player.Player;
import com.acquire.player.Share;
import com.acquire.player.strategy.LargestAlphaStrategy;
import com.acquire.player.strategy.PlayerStrategy;
import com.acquire.player.strategy.RandomPlayerStrategy;
import com.acquire.player.strategy.SequentialPlayerStrategy;
import com.acquire.player.strategy.SmallestAntiStrategy;

public class RequestHandler {
	public void start(String string) {
		try {
			RequestHandler requestHandler = new RequestHandler();
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;

			builder = builderFactory.newDocumentBuilder();

			Document doc = builder.newDocument();

			doc = builder.parse(new InputSource(new ByteArrayInputStream(string
					.toString().getBytes("utf-8"))));
			doc.getDocumentElement().normalize();
			getBoard(doc);
			Player player = getPlayer(doc);
			setShares(player, doc);
			List<String> hotelList = getHotels(doc);
			System.out
					.println("Enter the strategy you wish to take: \n1. Sequential \n2. Random \n Enter selection:");
			Scanner s = new Scanner(System.in);
			List<Object> place;
			List<String> hotels;
			if (s.next().equals("1")) {
				PlayerStrategy sequentialPlayerStrategy = new LargestAlphaStrategy();
				place = sequentialPlayerStrategy.playTile(player, hotelList);
				hotels = sequentialPlayerStrategy.buyShare();
			} else {
				PlayerStrategy randomPlayerStrategy = new RandomPlayerStrategy();
				place = randomPlayerStrategy.playTile(player, hotelList);
				hotels = randomPlayerStrategy.buyShare();
			}
			writeResponse(place, hotels);
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
		} catch (UnsupportedEncodingException e) {
			try {
				this.writeError("Format error in xml input");
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
		} catch (AcquireException e) {
			try {
				this.writeError(e.getMessage());
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

	public Board getBoard(Document document) {
		Board board = Board.getInstance();
		board.clear();
		board = Board.getInstance();
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
				throw new AcquireException("The hotel " + element.getAttribute("label") + " already has a chain");
			} else {
				hotelList.add(element.getAttribute("label"));
			}
		}
		return hotelList;
	}

	public void writeResponse(List<Object> place, List<String> hotels)
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
		if (!place.isEmpty()) {
			Element placeElement = doc.createElement("place");
			Tile tile = (Tile) place.get(0);
			placeElement.setAttribute("row", tile.getRow());
			placeElement.setAttribute("column", tile.getColumn());
			if (place.size() > 1)
				placeElement.setAttribute("hotel", place.get(1).toString());
			root.appendChild(placeElement);
		}
		doc.appendChild(root);

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source;

		source = new DOMSource(doc);
		StreamResult result = new StreamResult(new StringWriter());
		result = new StreamResult(new StringWriter());
		transformer.transform(source, result);
		System.out.println(result.getWriter().toString());
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
}
