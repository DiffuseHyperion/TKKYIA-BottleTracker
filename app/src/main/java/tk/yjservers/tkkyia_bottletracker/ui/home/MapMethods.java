package tk.yjservers.tkkyia_bottletracker.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.NavController;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tk.yjservers.tkkyia_bottletracker.MainActivity;
import tk.yjservers.tkkyia_bottletracker.R;

public class MapMethods {
    // didnt feel like making that much static methods, so here i am

    private Pair<Boolean, Double> latitude;
    private Pair<Boolean, Double> longitude;

    public MapMethods() {
        this.latitude = null;
        this.longitude = null;
    }
    public void navToMap(Activity activity, NavController navController, String sharedprefskey) {

        View view = activity.findViewById(android.R.id.content);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(MainActivity.preference, Context.MODE_PRIVATE);
        String ip = sharedPreferences.getString(sharedprefskey, "error");
        if (ip.equals("error")) {
            Snackbar.make(view, "No saved data could be found on this bottle. Try resetting your saved data.", BaseTransientBottomBar.LENGTH_SHORT).show();
        } else {
            String latURL = "http://" + ip + "/LAT";
            String longURL = "http://" + ip + "/LONG";
            CompletableFuture<Pair<Boolean, Double>> futurelat = getRESTApi(latURL);
            CompletableFuture<Pair<Boolean, Double>> futurelong = getRESTApi(longURL);

            CountDownLatch latch = new CountDownLatch(2);
            futurelat.whenComplete((objects, throwable) -> {
                latitude = objects;
                latch.countDown();
            });
            futurelong.whenComplete((objects, throwable) -> {
                longitude = objects;
                latch.countDown();
            });
            try {
                latch.await();
                if (!latitude.getValue0() && !longitude.getValue0()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", sharedprefskey);
                    bundle.putDouble("lat", latitude.getValue1());
                    bundle.putDouble("long", longitude.getValue1());
                    navController.navigate(R.id.nav_map, bundle);
                } else {
                    Snackbar.make(view, "Something went wrong while contacting the bottle.", BaseTransientBottomBar.LENGTH_SHORT)
                            .addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    Snackbar.make(view, "Response codes (latitude, longitude): " + latitude.getValue1().intValue() + " | " +  longitude.getValue1().intValue(), BaseTransientBottomBar.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public CompletableFuture<Pair<Boolean, Double>> getRESTApi(String url) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture<Pair<Boolean, Double>> futurepair = new CompletableFuture<>();
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert connection != null;
            try {
                if (connection.getResponseCode() == 200) {
                    InputStream responseBody = connection.getInputStream();
                    Double i = Double.parseDouble(readInputStream(responseBody));
                    futurepair.complete(new Pair<>(false, i));
                } else {
                    Double i = (double) connection.getResponseCode();
                    futurepair.complete(new Pair<>(true, i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return futurepair;
    }

    public String readInputStream(InputStream stream) {
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
