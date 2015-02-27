package login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    public static String getWebPageAsString(String urlString, String cookie) throws IOException
    {
        //Open connection to webpage
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Cookie", cookie);
        InputStream is = connection.getInputStream();

        //Add data to string
        BufferedReader reader = null;
        String result;

        try
        {
            reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
            }

            result = stringBuilder.toString();
        }

        catch (Exception e)
        {
            result = "";
        }

        connection.disconnect();

        return result;

    }
    
    public static String getResponse(InputStream is) throws IOException
    {

        //Add data to string
        BufferedReader reader = null;
        String result;

        try
        {
            reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
            }

            result = stringBuilder.toString();
        }

        catch (Exception e)
        {
            result = "";
        }

        return result;

    }
}
