import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FortuneCookieClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -cp fortunecookie.jar fc.Client localhost:12345 get-cookie");
            return;
        }

        String[] hostPort = args[0].split(":");
        String host = hostPort[0];
        int port = Integer.parseInt(hostPort[1]);
        String[] cookieNumber = args[1].split("\\[|\\]");
        int count = Integer.parseInt(cookieNumber[1]);

        try {
            requestFortuneCookie(host, port, count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void requestFortuneCookie(String host, int port, int count) throws IOException {
        try (Socket clientSocket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("get-cookie[" + count + "]");

            String response;
            while ((response = in.readLine()) != null) {
                if (response != null && response.startsWith("cookie-text")) {
                    String fortune = response.substring(12);
                    System.out.println(fortune);
                } else {
                    System.out.println("Unexpected response from server " + response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
