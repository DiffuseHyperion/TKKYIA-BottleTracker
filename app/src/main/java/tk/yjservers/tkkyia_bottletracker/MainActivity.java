package tk.yjservers.tkkyia_bottletracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.Map;
import java.util.Random;

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
                Bundle bundle = new Bundle();

                Random r = new Random();
                int rangeMin1 = -180;
                int rangeMax1 = 180;
                int rangeMin2 = -90;
                int rangeMax2 = 90;
                double randomValue1 = rangeMin1 + (rangeMax1 - rangeMin1) * r.nextDouble();
                double randomValue2 = rangeMin2 + (rangeMax2 - rangeMin2) * r.nextDouble();
                bundle.putDouble("long", randomValue1);
                bundle.putDouble("lat", randomValue2);
                bundle.putString("name", s);
                navController.navigate(R.id.nav_map, bundle);
                return true;
            });
        }
    }
}