package login;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage for single plan, parsing is done by PageParser
 * @author christian
 *
 */
public class VPlan {
	// All members are public since this class
	// no functionality, it simply stores information
	
	// dd.mm.yyyy
	public String date;
	// dd.mm.yyyy hh:ii
	public String lastChange;
	
	public String absentTeachers;
	public String absentClasses;
	public String teachersWithChange;
	public String classesWithChange;
	
	public String additionalInfo;
	public List<Event> entries;
	
	/**
	 * Initialize all member variables
	 */
	public VPlan(){
		date = "";
		absentClasses = "";
		absentTeachers = "";
		teachersWithChange = "";
		classesWithChange = "";
		additionalInfo = "";
		entries = new ArrayList<>();
	}
}
