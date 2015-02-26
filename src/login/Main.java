package login;

public class Main {

	public static void main(String[] args) 
	{
		try{
			LoginManager manager = new LoginManager();
			manager.login();
			
			String plan = manager.getPlanHtml();
			
			PageParser parse = new PageParser(plan);
			parse.getAvailableDates(false);
			//parse
		}
		catch(Exception exc){
			System.err.println("Fehler");
			//System.err.print(exc.getMessage());
			exc.printStackTrace();
		}
		
	}

}
