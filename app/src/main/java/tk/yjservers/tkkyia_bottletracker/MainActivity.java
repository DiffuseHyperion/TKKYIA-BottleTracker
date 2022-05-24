package tk.yjservers.tkkyia_bottletracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import tk.yjservers.tkkyia_bottletracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    public static final String preference = "tkkyia_savedservers";
    public static SubMenu savedbottlesmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tk.yjservers.tkkyia_bottletracker.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_setup, R.id.nav_settings)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Menu menu = binding.navView.getMenu();
        savedbottlesmenu = menu.addSubMenu(Menu.NONE, Menu.NONE, Menu.CATEGORY_CONTAINER, "Saved bottles:");
        updateList(this, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static void updateList(Activity activity, NavController navController) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Map<String, ?> set = sharedPreferences.getAll();
        String[] list = set.keySet().toArray(new String[0]);

        savedbottlesmenu.clear();

        for (String s : list) {
            MenuItem item = savedbottlesmenu.add(s);
            item.setOnMenuItemClickListener(item1 -> {
                navToMap(activity.getCurrentFocus(), navController, s);
                return true;
            });
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static boolean navToMap(View view, NavController navController, String name) {
        Bundle bundle = new Bundle();

        CountDownLatch latch = new CountDownLatch(2);
        Pair<Boolean, Double> latitude = null;
        Pair<Boolean, Double> longitude = null;
        URLThread threadLat = new URLThread("https://rest-test-api.diffusehyperion.repl.co/api/lat", latch, latitude);
        URLThread threadLong = new URLThread("https://rest-test-api.diffusehyperion.repl.co/api/long", latch, longitude);
        threadLat.start();
        threadLong.start();
        try {
            latch.await();
            if (!latitude.first && !longitude.first) {
                bundle.putString("name", name);
                bundle.putDouble("lat", latitude.second);
                bundle.putDouble("long", latitude.second);
                navController.navigate(R.id.nav_map, bundle);
                return true;
            } else {
                Snackbar.make(view, "Something went wrong while contacting the bottle.", BaseTransientBottomBar.LENGTH_SHORT)
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                Snackbar.make(view, "Response codes (latitude, longitude): " + latitude.second + longitude.second, BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        }).show();
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}