package com.graph.operations;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

/**
 * 
 * Reads XML input and parse and create graph
 * 
 */

public class ServerService {
	public static void main(String[] args) {
		ServerService serverService = new ServerService();
		class ClientConnection implements Runnable {
			private Socket client;
			private ServerService serverService;

			public ClientConnection(Socket client, ServerService serverService) {
				this.client = client;
				this.serverService = serverService;
			}

			public void run() {
				BufferedReader inFromClient;
				try {
					inFromClient = new BufferedReader(new InputStreamReader(
							client.getInputStream()));

					DataOutputStream outToClient = new DataOutputStream(
							client.getOutputStream());
					while (true) {
						String clientSentence = inFromClient.readLine();
						if (clientSentence != null)
							serverService.parseInput(clientSentence,
									outToClient);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		ServerSocket server = null;
		try {
			server = new ServerSocket(5432);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + 5432);
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
			Thread t = new Thread(new ClientConnection(client, serverService));
			t.start();
		}
	}

	Graph create(Graph g, String u, String v, String w) {
		GraphOperations graphOperations = new iGraphOperations();
		g = graphOperations.addEdge(g, u, v, Double.parseDouble(w));
		// g.insertEdge(u, v, Double.parseDouble(w));
		return g;
	}

	Graph createGraph(Graph g, double low, double high) {
		g.low = low;
		g.high = high;
		return g;
	}

	void parseInput(String input, DataOutputStream outToClient) {
		try {
			StreamResult result = new StreamResult(new StringWriter());
			System.out.println("Received: " + input);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new InputSource(
					new ByteArrayInputStream(input.getBytes("utf-8"))));
			// doc = docBuilder.newDocument();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source;

			if (doc.getElementsByTagName("new").getLength() != 0) {
				NodeList newGraphNodeList = doc.getElementsByTagName("new");

				Node newNode = newGraphNodeList.item(0);
				Element element = (Element) newNode;

				if (element.hasAttribute("low") && element.hasAttribute("high")) {
					Graph g = this.createGraph(new Graph(),
							Double.parseDouble(element.getAttribute("low")),
							Double.parseDouble(element.getAttribute("high")));
					doc = docBuilder.newDocument();
					Element rootElement = doc.createElement("graph");
					doc.appendChild(rootElement);
					rootElement.setAttribute("low", Double.toString(g.low));
					rootElement.setAttribute("high", Double.toString(g.high));
					source = new DOMSource(doc);
					StringWriter writer = new StringWriter();
					result = new StreamResult(writer);
					transformer.transform(source, result);
					String msg = writer.toString();
					outToClient.writeBytes(writer.toString() + "\n");
				} else
					this.writeError("Attributes low or high missing in xml",
							outToClient);
			} else if (doc.getElementsByTagName("add").getLength() != 0) {

				Element add = (Element) doc.getElementsByTagName("add").item(0);
				if (doc.getElementsByTagName("graph").getLength() != 0) {
					NodeList nodeList = doc.getElementsByTagName("graph");
					Node node = nodeList.item(0);
					Graph g = this.getGraph(node, doc);
					if (g == null) {
						this.writeError("Error while creating graph.",
								outToClient);
					} else {
						if (add.hasAttribute("from") && add.hasAttribute("to")
								&& add.hasAttribute("cost")) {
							GraphOperations graphOperations = new iGraphOperations();
							g = graphOperations.addEdge(g, add
									.getAttribute("from"), add
									.getAttribute("to"), Double.parseDouble(add
									.getAttribute("cost")));
							doc = docBuilder.newDocument();
							Element rootElement = doc.createElement("graph");
							doc.appendChild(rootElement);
							rootElement.setAttribute("low",
									Double.toString(g.low));
							rootElement.setAttribute("high",
									Double.toString(g.high));
							for (String key : g.adj.keySet()) {
								for (Graph edgeList : g.adj.get(key)) {
									Element edge = doc.createElement("edge");
									edge.setAttribute("from", key);
									edge.setAttribute("to", edgeList.v);
									edge.setAttribute("cost",
											Double.toString(edgeList.w));
									rootElement.appendChild(edge);
								}
							}
							source = new DOMSource(doc);
							result = new StreamResult(new StringWriter());
							transformer.transform(source, result);
							String msg = result.getWriter().toString();
							outToClient.writeBytes(msg + "\n");
						} else
							this.writeError("Attributes missing in xml",
									outToClient);
					}
				} else
					this.writeError("Graph missing in xml", outToClient);
			} else if (doc.getElementsByTagName("join").getLength() != 0) {
				Element add = (Element) doc.getElementsByTagName("join")
						.item(0);
				if (doc.getElementsByTagName("graph").getLength() != 0) {
					NodeList nodeList = doc.getElementsByTagName("graph");
					List<Graph> graphList = new ArrayList<>();
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						Graph g = this.getGraph(node, doc);
						graphList.add(g);
					}
					if (graphList.size() == 0) {
						this.writeError("Error while creating graph.",
								outToClient);
					} else if (graphList.size() != 2)
						this.writeError(
								"Enter 2 graphs for join operation. Missing graph.",
								outToClient);
					else {
						GraphOperations graphOperations = new iGraphOperations();
						Graph g = graphOperations.join(graphList.get(0),
								graphList.get(1));
						doc = docBuilder.newDocument();
						Element rootElement = doc.createElement("graph");
						doc.appendChild(rootElement);
						rootElement.setAttribute("low", Double.toString(g.low));
						rootElement.setAttribute("high",
								Double.toString(g.high));
						for (String key : g.adj.keySet()) {
							for (Graph edgeList : g.adj.get(key)) {
								Element edge = doc.createElement("edge");
								edge.setAttribute("from", key);
								edge.setAttribute("to", edgeList.v);
								edge.setAttribute("cost",
										Double.toString(edgeList.w));
								rootElement.appendChild(edge);
							}
						}
						source = new DOMSource(doc);
						result = new StreamResult(new StringWriter());
						transformer.transform(source, result);
						String msg = result.getWriter().toString();
						outToClient.writeBytes(msg + "\n");
					}
				} else
					this.writeError("Graph missing in xml", outToClient);
			} else if (doc.getElementsByTagName("nodes").getLength() != 0) {
				Element add = (Element) doc.getElementsByTagName("nodes").item(
						0);
				if (doc.getElementsByTagName("graph").getLength() != 0) {
					NodeList nodeList = doc.getElementsByTagName("graph");
					Node node = nodeList.item(0);
					Graph g = this.getGraph(node, doc);
					if (g == null) {
						this.writeError("Error while creating graph.",
								outToClient);
					} else {
						doc = docBuilder.newDocument();
						Element rootElement = doc.createElement("nodes");
						doc.appendChild(rootElement);
						for (String key : g.adj.keySet()) {
							Element nodeName = doc.createElement("node");
							nodeName.setAttribute("name", key);
							rootElement.appendChild(nodeName);
						}
						source = new DOMSource(doc);
						result = new StreamResult(new StringWriter());
						transformer.transform(source, result);
						String msg = result.getWriter().toString();
						outToClient.writeBytes(msg + "\n");
					}
				} else
					this.writeError("Graph missing in xml", outToClient);
			} else if (doc.getElementsByTagName("edges").getLength() != 0) {
				Element add = (Element) doc.getElementsByTagName("edges").item(
						0);
				if (doc.getElementsByTagName("graph").getLength() != 0) {
					NodeList nodeList = doc.getElementsByTagName("graph");
					Node node = nodeList.item(0);
					Graph g = this.getGraph(node, doc);
					if (g == null) {
						this.writeError("Error while creating graph.",
								outToClient);
					} else {
						doc = docBuilder.newDocument();
						Element rootElement = doc.createElement("edges");
						doc.appendChild(rootElement);
						for (String key : g.adj.keySet()) {
							for (Graph edgeList : g.adj.get(key)) {
								System.out.println(g.adj.get(key));
								Element edge = doc.createElement("edge");
								edge.setAttribute("from", key);
								edge.setAttribute("to", edgeList.v);
								edge.setAttribute("cost",
										Double.toString(edgeList.w));
								rootElement.appendChild(edge);
							}
						}
						source = new DOMSource(doc);
						result = new StreamResult(new StringWriter());
						transformer.transform(source, result);
						String msg = result.getWriter().toString();
						outToClient.writeBytes(msg + "\n");
					}
				} else
					this.writeError("Graph missing in xml", outToClient);
			} else if (doc.getElementsByTagName("path").getLength() != 0) {
				Element add = (Element) doc.getElementsByTagName("path")
						.item(0);
				if (add.hasAttribute("from") && add.hasAttribute("to")) {
					if (doc.getElementsByTagName("graph").getLength() != 0) {
						NodeList nodeList = doc.getElementsByTagName("graph");
						Node node = nodeList.item(0);
						Graph g = this.getGraph(node, doc);
						if (g == null) {
							this.writeError("Error while creating graph.",
									outToClient);
						} else {
							doc = docBuilder.newDocument();
							GraphOperations graphOperations = new iGraphOperations();
							List<Object> path = graphOperations.isPath(g,
									add.getAttribute("from"),
									add.getAttribute("to"));
							if (path == null) {
								Element rootElement = doc
										.createElement("false");
								doc.appendChild(rootElement);
							} else {
								Element rootElement = doc.createElement("path");
								rootElement.setAttribute("cost", path.get(0)
										.toString());
								doc.appendChild(rootElement);
								LinkedHashMap<List<String>, Double> list = (LinkedHashMap<List<String>, Double>) path.get(1);
								for (List<String> key : list.keySet()) {
									// for (Graph edgeList : g.adj.get(key)) {
									Element edge = doc.createElement("edge");
									edge.setAttribute("from", key.get(0));
									edge.setAttribute("to", key.get(1));
									edge.setAttribute("cost", list.get(key)
											.toString());
									rootElement.appendChild(edge);
									// }
								}
							}
							source = new DOMSource(doc);
							StringWriter writer = new StringWriter();
							result = new StreamResult(writer);
							transformer.transform(source, result);
							outToClient.writeBytes(writer.toString() + "\n");

						}
					} else
						this.writeError("Graph missing in xml", outToClient);
				} else {
					this.writeError("Attribute to or from missing in xml", outToClient);
				}
			} else {
				this.writeError("No such operation", outToClient);
			}
		} catch (IOException e) {
			try {
				this.writeError("Format error in xml input", outToClient);
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (SAXException e) {
			try {
				this.writeError("Format error in xml input", outToClient);
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			try {
				this.writeError("Format error in xml input", outToClient);
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (TransformerException e) {
			try {
				this.writeError("Format error in xml input", outToClient);
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (NullPointerException e) {
			try {
				this.writeError("Error in creating graph.", outToClient);
			} catch (TransformerConfigurationException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	Graph getGraph(Node node, Document doc) {
		NodeList nodeList = doc.getElementsByTagName("graph");
		Element element = (Element) node;
		NodeList elementNodeList = element.getElementsByTagName("edge");
		Graph g = new Graph();
		g.high = Double.parseDouble(element.getAttribute("high"));
		g.low = Double.parseDouble(element.getAttribute("low"));
		for (int j = 0; j < elementNodeList.getLength(); j++) {
			Element elements = (Element) elementNodeList.item(j);
			g = this.create(g, elements.getAttribute("from"),
					elements.getAttribute("to"), elements.getAttribute("cost"));
		}
		return g;
	}

	String writeError(String msg, DataOutputStream outToClient)
			throws ParserConfigurationException,
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
			outToClient.writeBytes(result.getWriter().toString() + "\n");
			// return result.getWriter().toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}
}
