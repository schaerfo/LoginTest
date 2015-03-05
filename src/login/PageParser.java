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
 * 
 * Purpose: Parses http://asgspez.de/index.php/schueler-intern/vertretungsplan.html
 * (HTML code to be provided as String) and extracts relevant Information
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
	 * Initializes DOM and extracts relevant HTML tags
	 * @param content HTML code to be processed
	 */
	public PageParser(String content){
		htmlContent = content;
		htmlDoc = Parser.parse(content, PLAN_URL);
		
		// The <div id="main" role="main"> tag is closest one to the
		// relevant <article> tag which has an id
		Element divIdMain = htmlDoc.body().getElementById("main");
		contentTag = divIdMain.getElementsByTag("article").first();
	}
	
	public VPlan extractPlanInfo() throws Exception{
		VPlan ret = new VPlan();
		ret.date = extractCurrentDate();
		
		Elements strongTags = contentTag.getElementsByTag("strong");
		for(Element currElem : strongTags){
			String contentText = currElem.nextSibling().toString();
			
			if (currElem.ownText().equals("Abwesende Lehrer:"))
				ret.absentTeachers = contentText;
			else if (currElem.ownText().equals("Abwesende Klassen:"))
				ret.absentClasses = contentText;
			else if (currElem.ownText().equals("Lehrer mit Änderung:"))
				ret.teachersWithChange = contentText;
			else if (currElem.ownText().equals("Klassen mit Änderung:"))
				ret.classesWithChange = contentText;
			else if (currElem.ownText().equals("Zusätzliche Informationen:")){
				
				// In this section there are <br> tags and text nodes alternating
				// until a <strong> tag
				String infoText = "";
				Node infoElem = currElem.nextSibling();
				while (true){
					String currNodeName = infoElem.nodeName();
					if (currNodeName.equals("#text")){
						infoText += infoElem.toString() + "\n";
					}
					else if (currNodeName.equals("strong")){
						break;
					}
					
					infoElem = infoElem.nextSibling();
				}
				ret.additionalInfo = infoText;
			}
		}
		
		Element planTable = contentTag.getElementsByTag("table").first();
		// The first row contains the header and is ignored
		Element currTableRow = planTable.child(0).child(0);
		while(true){
			// Throws NullPointerException after having reached last sibling			
			try{
				currTableRow = currTableRow.nextElementSibling();
				
				String form = currTableRow.child(0).ownText();
				String lesson = currTableRow.child(1).ownText();
				String subject = currTableRow.child(2).ownText();
				String teacher = currTableRow.child(3).ownText();
				String room = currTableRow.child(4).ownText();
				String remark = currTableRow.child(5).ownText();
				
				ret.entries.add(new Event(form, lesson, subject, teacher, room, remark));
			}
			catch (NullPointerException exc){
				break;
			}
		}
		
		return ret;
	}

	/**
	 * Extracts dates referenced by current page
	 * @param includeCurrentDate Specifies whether to include date currently fetched
	 * @return All available dates, each in pattern dd.mm.yyyy
	 * @throws Exception 
	 */
	public List<String> getAvailableDates(boolean includeFetchedPlan) throws Exception{
		List<String> ret = new ArrayList<String>();
		
		// Read all <a ...> Tags except last one
		Elements aTags = contentTag.getElementsByTag("a");
		for (int i=0;i<aTags.size()-1;i++){
			Element currTag = aTags.get(i);
			String currDate = currTag.ownText();
			
			if(!includeFetchedPlan){
				/*try {
					String textDate = dateToText(currDate);
					String headline = contentTag.getElementsByTag("h2").get(0).ownText();
					
					if (!headline.contains(textDate)){
						ret.add(currDate);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}*/
				String fetchedPlanDate = extractCurrentDate();
				if (!fetchedPlanDate.equals(currDate))
					ret.add(currDate);
			}
			else
				ret.add(currDate);
		}
		
		return ret;
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
	
	/**
	 * @return Date of current plan as dd.mm.yyyy
	 * @throws Exception 
	 */
	private String extractCurrentDate() throws Exception{
		String headline = contentTag.getElementsByTag("h2").get(0).ownText();
		String [] headlineWords = headline.split(Pattern.quote(" "));
		
		final int l = headlineWords.length;
		String day = headlineWords[l-3];
		String monthText = headlineWords[l-2];
		
		String month = "";
		switch (monthText){
		case "Januar": month = "01"; break;
		case "Februar": month = "02"; break;
		case "März": month = "03"; break;
		case "April": month = "04"; break;
		case "Mai": month = "05"; break;
		case "Juni": month = "06"; break;
		case "Juli": month = "07"; break;
		case "August": month = "08"; break;
		case "September": month = "09"; break;
		case "Oktober": month = "10"; break;
		case "November": month = "11"; break;
		case "Dezember": month = "12"; break;
		default: throw new Exception("Unbekannter Monat: " + monthText);
		}
		
		String year = headlineWords[l-1];
		return day + month + "." + year;
	}
}
