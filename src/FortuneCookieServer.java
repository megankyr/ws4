import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;

public class FortuneCookieServer {

    private static Cookie cookieInstance;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: java -cp fortunecookie.jar fc.Server 12345 cookie_file.txt to proceed");
            return;
        }

        int port = Integer.parseInt(args[0]);
        String cookieFile = (args[1]);

        try {
            cookieInstance = new Cookie(cookieFile);
            startServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void startServer(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is now running on " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String request = in.readLine();
            if (request.equals("get-cookie")) {
                sendCookies(clientSocket);
            } else if (request.equals("stop")) {
                System.out.println("Client has requested to close the connection");
                clientSocket.close();
            } else {
                System.out.println("Invalid request");
            }
        } finally {
            clientSocket.close();
        }
    }

    private static void sendCookies(Socket clientSocket) throws IOException {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String fortune = cookieInstance.getRandomCookie();
            out.println("cookie-text " + fortune);
        }
    }
}