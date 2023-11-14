import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CookieClientHandler implements Runnable {

    private Socket clientSocket;
    private Cookie cookieInstance;

    public CookieClientHandler(Socket clientSocket, Cookie cookieInstance) {
        this.clientSocket = clientSocket;
        this.cookieInstance = cookieInstance;
    }

    @Override
    public void run() {
        try {
            handleClient(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClient(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String request = in.readLine();
            String[] requests = request.split("\\[|\\]");
            if (requests.length == 2 && requests[0].equals("get-cookie")) {
                int count = Integer.parseInt(requests[1]);
                sendCookies(clientSocket, count);
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

    private void sendCookies(Socket clientSocket, int count) throws IOException {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            for (int i = 0; i < count; i++){
                String fortune = cookieInstance.getRandomCookie();
                out.println("cookie-text " + fortune);
            }
        }
    }
}
