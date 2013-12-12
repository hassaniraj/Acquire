package com.acquire.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.acquire.board.Board;
import com.acquire.board.Chain;
import com.acquire.board.Hotel;
import com.acquire.exception.AcquireException;
import com.acquire.factory.BoardFactory;
import com.acquire.factory.PlayerFactory;
import com.acquire.player.Player;
import com.acquire.player.Share;
import com.acquire.player.strategy.LargestAlphaStrategy;
import com.acquire.player.strategy.RandomPlayerStrategy;
import com.acquire.player.strategy.SequentialPlayerStrategy;
import com.acquire.player.strategy.SmallestAntiStrategy;

public class InputGameHandler {

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
		// Added an attribute name for a player
		player.setName(element.getAttribute("name"));
		//Added a strategy for a player based on the name
		if(player.getName().equalsIgnoreCase("random")){
			player.setStrategy(new RandomPlayerStrategy());
		}
		else if(player.getName().equalsIgnoreCase("sequential")){
			player.setStrategy(new SequentialPlayerStrategy());
		}
		else if(player.getName().equalsIgnoreCase("smallest-anti")){
			player.setStrategy(new SmallestAntiStrategy());
		}
		else if(player.getName().equalsIgnoreCase("largest-alpha")){
			player.setStrategy(new LargestAlphaStrategy());
		}
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
					throw new AcquireException("The hotel "
							+ element.getAttribute("label")
							+ " already has a chain");
				} else {
					hotelList.add(element.getAttribute("label"));
				}
		}
		return hotelList;
	}

}
