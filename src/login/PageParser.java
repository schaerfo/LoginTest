package login;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.parser.*;
import org.jsoup.select.Elements;
import org.jsoup.nodes.*;

/**
 * 
 * @author christian
 * Zweck: Parst die http://asgspez.de/index.php/schueler-intern/vertretungsplan.html
 * und extrahiert die Tage der verfügbaren anderen Pläne und den gezeigten als 
 */
public class PageParser {
	// Gibt es das nicht auch schon anderswo?
	public static final String PLAN_URL = "http://asgspez.de/index.php/schueler-intern/vertretungsplan.html";
	
	private String htmlContent;
	private Document htmlDoc;
	
	// Der <article class="item-page"> Tag auf der Seite,
	// der alle Informationen enthält
	private Element contentTag;
	
	/**
	 * 
	 * @param includeCurrentDate Specifies whether to include date currently fetched
	 * @return 
	 */
	public List<String> getAvailableDates(boolean includeFetchedPlan){
		List<String> ret = new ArrayList<String>();
		
		// Read all <a ...> Tags except last one
		Elements aTags = contentTag.getElementsByTag("a");
		for (int i=0;i<aTags.size()-1;i++){
			Element currTag = aTags.get(i);
			String currDate = currTag.ownText();
			
			if(!includeFetchedPlan){
				try {
					String textDate = dateToText(currDate);
					String headline = contentTag.getElementsByTag("h2").get(0).ownText();
					
					if (!headline.contains(textDate)){
						ret.add(currDate);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
				ret.add(currDate);
		}
		
		return ret;
	}
	
	public PageParser(String content){
		htmlContent = content;
		htmlDoc = Parser.parse(content, PLAN_URL);
		
		// Der <div id="main" role="main"> Tag
		// nächster Verwandter des relevanten <article>-Tag, der eine id hats
		Element divIdMain = htmlDoc.body().getElementById("main");
		contentTag = divIdMain.getElementsByTag("article").first();
	}
	
	/**
	 * Converts dd.mm.yyyy to dd. Month yyyy
	 * @throws Exception 
	 */
	public static String dateToText(String input) throws Exception{
		String[] dateArray = input.split(Pattern.quote("."));
		
		String monthText = "";
		String monthNumber = dateArray[1];
		
		switch(monthNumber){
		case "01": monthText = "Januar"; break;
		case "02": monthText = "Februar"; break;
		case "03": monthText = "März"; break;
		case "04": monthText = "April"; break;
		case "05": monthText = "Mai"; break;
		case "06": monthText = "Juni"; break;
		case "07": monthText = "Juli"; break;
		case "08": monthText = "August"; break;
		case "09": monthText = "September"; break;
		case "10": monthText = "Oktober"; break;
		case "11": monthText = "November"; break;
		case "12": monthText = "Dezember"; break;
		default: throw new Exception("Unknown month");
		}
		
		String ret = dateArray[0] + ". " + monthText + " " + dateArray[2];
		return ret;
	}
}
