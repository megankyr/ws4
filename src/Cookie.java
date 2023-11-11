import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cookie {

    private List<String> cookies;

    public Cookie(String cookieFile) throws IOException {
        openCookieFile(cookieFile);
    }

    public void openCookieFile(String cookieFile) throws IOException {
        cookies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cookieFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cookies.add(line);
            }
        }
    }

    public String getRandomCookie() {
        Random random = new Random();
        int index = random.nextInt(cookies.size());
        return cookies.get(index);
    }

    public void closeCookieFile(String cookieFile) throws IOException{
        System.out.println("Closing the cookie file");
    }
}
