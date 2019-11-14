package jwebserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

/**
 * jwebserver for CST2335
 * Author: Vaughan Alexander
 * Operation: Specify port in main method of Server
 * Usage: In client browser enter serverIP:port
 * Caveat: Make sure you terminate program correctly as it may not unbind from port
 * */

public class Server {
	private ServerSocket server;
	private Socket client;
	
	public Server(int port) throws IOException {
		server = new ServerSocket(port);
		System.out.println("Server Listening at Port: "+ Integer.toString(port));
		client = server.accept();
		// Shutdown hook so ports close when program exits unexpectedly (CTRL + C) 
		//Runtime.getRuntime().addShutdownHook(new ShutdownThread(server,client));
		System.out.println("Client Connection Recieved\n");
		handleRequest(client.getInputStream());
		client.close();
		server.close();
	}
	
	private void handleRequest(InputStream inputStream) throws IOException {
		    sendResponse(getResponse(buildRequest(inputStream)));
		    inputStream.close();
	}

	private Request buildRequest(InputStream inputStream) throws IOException{
		int byteData;
        String req = "";
        do {
            byteData = inputStream.read();
            req+=(char)byteData;
        } while(inputStream.available()>0);
        return new Request(req);
	}
	
	private void sendResponse(Response response) throws IOException{
		OutputStream out = client.getOutputStream();
	    out.write(response.toString().getBytes());
	}

	private Response getResponse(Request req) throws UnknownHostException, SocketException{
		
		if(req.getMethod().equals("GET") && req.getUrl().equals("/")) {
			return new Response("<html>" 
								+ "<head>" 
								+ "<title>jwebserver</title>"
								+ "</head>" 
								+ "<body>"
								+ "<h1>Client IP: " + client.getInetAddress().getHostAddress() + "</h1>"  
								+ "<h1>Server IP: " + getServerIP()+ "</h1>"
								+ "</body>"
								+ "</html>"
								);
		}
		else {
			return new Response("<html>"
					+ "<body>"
					+ "<font color='red' size='2'>"
					+ "Invalid URL/method"
					+ "</font>"
					+ "<br>URL: "+ req.getUrl() 
					+ "<br>method: "+ req.getMethod() 
					+ "</body>"
					+ "</html>");
		}
	}
	
	
	private String getServerIP() throws SocketException {
		// Grab network interfaces (this method was recommended here: https://stackoverflow.com/a/9482369)
		Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
		// Iterate over network interfaces until local network address found ex: 192.xxx.x.x
		while(nis.hasMoreElements()) {
			List<InterfaceAddress> interfaceAddresses = nis.nextElement().getInterfaceAddresses();
			for(InterfaceAddress ia: interfaceAddresses) {
				String address = ia.getAddress().getHostAddress();
				if(address.startsWith("192")) return address;
			}
		}
		return "Local Network Address not found";
	}
	

	public static void main(String[] args) {
		while(true) {
			try {
				new Server(8000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
