package com.acquire.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.acquire.board.Board;
import com.acquire.board.Tile;
import com.acquire.exception.AcquireException;
import com.acquire.player.Player;
import com.acquire.proxy.AdminController;
import com.acquire.proxy.AdminProxy;
import com.acquire.proxy.PlayerController;
import com.acquire.proxy.PlayerProxy;

public class GameHandler {
	Board inputBoard = null;
	PlayerInputHandler inputHandler = new PlayerInputHandler();
	AdminGameHandler adminHandler = new AdminGameHandler();
	PlayerController playerController = new PlayerProxy();
	AdminController adminController = new AdminProxy();
	Board adminBoard = null;

	public void processRequest(StringBuilder string) {
		try {
			if (string == null) {
				System.out.println("Start of game");
			} else {
				String xml = string.toString();
				InputSource source = new InputSource(new ByteArrayInputStream(
						xml.getBytes()));
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(source);
				doc.getDocumentElement().normalize();

				// Initialise the game on the admin side
				// adminBoard=adminHandler.initializeGame();

				// Parsed components from xml for comparison with the admin
				// state
				inputBoard = inputHandler.getBoard(doc);
				Player player = inputHandler.getPlayer(doc);
				inputHandler.setShares(player, doc);
				List<String> hotelList = inputHandler.getHotels(doc);

				// Write a method to compare the xml state and the state stored
				// at admin's end

				List<Object> response = playerController.playPlace(player,
						adminController.getHotels());
				if (response.isEmpty())
					// return error
					if (adminController.checkIfEnd()) {
						if (playerController.askEndGame()) {
							// End game
						}

					}

				Tile t = (Tile) response.get(0);
				adminController.playerPlaceMove((Tile) response.get(0),
						response.get(1).toString());
				Tile tile = (Tile) response.get(0);
				List<String> hotels = playerController.playBuy(player);

				for (String hotel : hotels) {
					adminController.playerBuyMove(hotel);
				}

				adminController.playerDone();
			}
		} catch (SAXException sax) {
			sax.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (AcquireException ace) {
			ace.printStackTrace();
		}
	}

}