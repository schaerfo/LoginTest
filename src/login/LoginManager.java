package login;

import java.net.*;
import java.util.regex.Pattern;
import java.io.*;

// Ich wär generell dafür die hier zu nehmen und nicht alles
// von org.w3c.*, java.xml.* usw. zusammenzusuchen
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class LoginManager {
	/*
     * Strategy:
     * 1. Load index.php, save cookie, extract "security token" (field "return")
     * 2. Post login data from user together with cookie and security token to index.php
     */
	
	public static final String LOGIN_URL = "http://asgspez.de/index.php/anmelden.html";
	
	private static final String LOGIN_FORM_ACTION = "/index.php/anmelden.html?task=user.login";
	
	private URL loginUrl;
	
	private String cookie;
	private String securityTokenReturn;
	private String securityTokenValue;

	public void login() throws IOException
	{
		loginUrl = new URL(LOGIN_URL);
		
		this.fetchCookieAndToken();
	}

	public void fetchCookieAndToken() throws IOException 
	{
		HttpURLConnection httpConn = (HttpURLConnection) loginUrl.openConnection();
		
		try{
			String cookieRaw = httpConn.getHeaderField("Set-Cookie");
			cookie = cookieRaw.split(Pattern.quote(";"))[0];
			System.out.println(cookie);
			
			String site = readHttpConnection(httpConn);
			this.extractSecurityTokens(site);
			
			System.out.println("Security token:");
			System.out.println(securityTokenReturn);
			System.out.println(securityTokenValue);
		}
		finally{
			httpConn.disconnect();	
		}

	}
	
	public static String readHttpConnection(HttpURLConnection conn) throws IOException
	{
		String ret = "";
		InputStream is = conn.getInputStream();
		BufferedReader reader = null;
		
		try{
            reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }

            ret = stringBuilder.toString();
        }

        catch (Exception e){                                                                                                           
            ret = "";
        }
		
		return ret;
	}
	
	private void extractSecurityTokens(String htmlPage)
	{
		Document doc = Parser.parse(htmlPage, LOGIN_URL);
		Element body = doc.body();
		
		Elements loginForm = body.getElementsByAttributeValue("action", LOGIN_FORM_ACTION);
		Elements hiddenFields = loginForm.get(0).getElementsByAttributeValue("type", "hidden");
		for (Element currNode:hiddenFields)
		{
			if (currNode.attr("name").equals("return"))
				securityTokenReturn = currNode.attr("value");
			if (currNode.attr("value").equals("1"))
				securityTokenValue = currNode.attr("name");
		}

	}
}
