import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class SmsDemo {
    public static void main(String[] args) {
        try {
            String userName = "lsgy";
            String password = "VWjFR";
            long timestamp = System.currentTimeMillis();
            
            // Calculate Sign: md5(userName + timestamp + md5(password))
            String sign = md5(userName + timestamp + md5(password));
            
            // Construct JSON Payload (Manual construction for no-dependency demo)
            String json = String.format(
                "{\"userName\": \"%s\", \"timestamp\": %d, \"sign\": \"%s\", \"messageList\": [" +
                "{\"phone\": \"15011111111\", \"content\": \"[Signature] SMS Content 1\"}," +
                "{\"phone\": \"15022222222\", \"content\": \"[Signature] SMS Content 2\"}" +
                "]}", userName, timestamp, sign);

            URL url = new URL("https://sdksms.com/api/sendMessageOne");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                System.out.println(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String md5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(s.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) { return ""; }
    }
}
