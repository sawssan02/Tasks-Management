package Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ServiceLogin {
    private static final String API_KEY = "8b255f39430a4a928a5319eda47c60fe";

    public boolean verifyEmail(String email) throws IOException {
        String encodedEmail = URLEncoder.encode(email, "UTF-8");
        URL url = new URL("https://api.zerobounce.net/v2/validate?api_key=" + API_KEY + "&email=" + encodedEmail);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        System.out.println("API Response: " + response.toString());
        return response.toString().contains("\"status\":\"valid\"");
    }
}