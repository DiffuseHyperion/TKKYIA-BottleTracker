package tk.yjservers.tkkyia_bottletracker.ui.home;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment implements OnMapReadyCallback {

    private String trackername;
    public MapFragment(String trackername) {
        this.trackername = trackername;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(0, 0);
        googleMap.addMarker(new MarkerOptions().position(sydney).title(trackername));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
