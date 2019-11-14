package jwebserver;

import java.util.Date;

public class Response {
	private String response = null;
	
	public Response(String response) {
		Date date = new Date();
		String statusLine = "HTTP/1.1 200 OK\r\n";
		String header = "Date: "+date.toString()+"\r\n";
		header += "Server: jwebserver\r\n";
		header += "Content-Type: text/html\r\n";
		header += "Content-length: "+response.length()+"\r\n\r\n";
		this.response = statusLine + header + response;
	}
	
	public String toString() {
		return response;
	}
}
