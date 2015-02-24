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
	
	public static final String LOGIN_URL = "http://asgspez.de/";
	
	private URL mLoginUrl;
	
	private String mCookie;
	private String mSecurityTokenReturn;
	private String mSecurityTokenValue1;

	public void login() throws IOException{
		mLoginUrl = new URL(LOGIN_URL);
		
		this.fetchCookeAndToken();
	}

	public void fetchCookeAndToken() throws IOException {
		HttpURLConnection httpConn = (HttpURLConnection) mLoginUrl.openConnection();
		
		try{
			String cookieRaw = httpConn.getHeaderField("Set-Cookie");
			mCookie = cookieRaw.split(Pattern.quote(";"))[0];
			System.out.println(mCookie);
			
			String seite = readHttpConnection(httpConn);
			this.extractSecurityTokens(seite);
			
			System.out.println(mSecurityTokenReturn);
			System.out.println(mSecurityTokenValue1);
		}
		finally{
			httpConn.disconnect();	
		}

	}
	
	public static String readHttpConnection(HttpURLConnection conn) throws IOException{
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
	
	private void extractSecurityTokens(String htmlPage){
		Document doc = Parser.parse(htmlPage, LOGIN_URL);
		Element body = doc.body();
		Elements hiddenFields = body.getElementsByAttributeValue("type", "hidden");
		for (Element currField : hiddenFields){
			if (currField.hasAttr("name") && currField.hasAttr("value")){
				
				/*if (currField.attr("name") == "return")
					mSecurityTokenReturn = currField.attr("value");
				
				if (currField.attr("value") == "1")
					mSecurityTokenValue1 = currField.attr("name");*/
				
				System.out.println(currField.attr("name"));
				System.out.println(currField.attr("value"));
				System.out.println();
			}
		}
	}
}
