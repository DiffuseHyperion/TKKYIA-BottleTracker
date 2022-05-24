package tk.yjservers.tkkyia_bottletracker;

import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class URLThread extends Thread {

    private final String url;
    private final CountDownLatch latch;
    private Pair<Boolean, Double> pair;

    public URLThread(String url, CountDownLatch latch, Pair<Boolean, Double> pair) {
        this.url = url;
        this.latch = latch;
        this.pair = pair;
    }

    public void run() {
        final Pair<Boolean, Double>[] response = new Pair[]{null};
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) new URL(url).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert connection != null;
            connection.setRequestProperty("User-Agent", "App-Client");
            try {
                if (connection.getResponseCode() == 200) {
                    InputStream responseBody = connection.getInputStream();
                    Double i = Double.parseDouble(readInputStream(responseBody));
                    response[0] = new Pair<>(false, i);
                } else {
                    Double i = (double) connection.getResponseCode();
                    response[0] = new Pair<>(true, i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        latch.countDown();
        pair = response[0];
    }

    public static String readInputStream(InputStream stream) {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (stream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textBuilder.toString();
    }
}
