package jwebserver;

import java.net.MalformedURLException;
import java.util.HashMap;

public class Request {
	private String method;
	private String url;
	private String version;
	private HashMap<String, String> headers;
	private String body;
	
	public Request(String raw) throws MalformedURLException {	
		String[] req = raw.split("\r\n");
		String[] startLine = req[0].split(" ");
		method = startLine[0];
		url = startLine[1];
		version = startLine[2];
		headers = new HashMap<String, String>();
		if(method.equals("POST")) {
			int bodyIndex = 0;
			for(int i =0; i<req.length; i++) {
				if(req[i].equals("")) { bodyIndex = i + 1; break;}
				else {
					String[] tmp = req[i].split(":");
					headers.put(tmp[0], tmp[1]);
				}
			}
			body = req[bodyIndex];
			for(int i = bodyIndex + 1; i < req.length; i++) {
				body += req[i] + "\r\n";
			}
		}
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	
	

}
