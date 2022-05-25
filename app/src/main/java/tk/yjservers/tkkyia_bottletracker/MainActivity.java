package tk.yjservers.tkkyia_bottletracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.Map;

import tk.yjservers.tkkyia_bottletracker.databinding.ActivityMainBinding;
import tk.yjservers.tkkyia_bottletracker.ui.home.MapMethods;

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
                new MapMethods().navToMap(activity, navController, s);
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
}