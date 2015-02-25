package login;

public class Main {

	public static void main(String[] args) 
	{
		try{
			LoginManager manager = new LoginManager();
			manager.login();
		}
		catch(Exception exc){
			System.err.println("Fehler");
			//System.err.print(exc.getMessage());
			exc.printStackTrace();
		}
		
	}

}
