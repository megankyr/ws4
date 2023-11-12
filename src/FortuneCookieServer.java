import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
                CookieClientHandler clientHandler = new CookieClientHandler(clientSocket, cookieInstance);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        }
    }

}