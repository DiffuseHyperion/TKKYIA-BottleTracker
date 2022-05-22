package tk.yjservers.tkkyia_bottletracker.ui.home;

import static tk.yjservers.tkkyia_bottletracker.MainActivity.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Arrays;
import java.util.Map;

import tk.yjservers.tkkyia_bottletracker.R;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentHomeBinding;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentHomeMapBinding;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentHomeNosavedBinding;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentHomeSavedBinding;

public class HomeFragment extends Fragment{

    private FragmentHomeBinding rootbinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootbinding = FragmentHomeBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(preference, Context.MODE_PRIVATE);
        if (sharedPreferences.getAll().isEmpty()) {
            FragmentHomeNosavedBinding binding = (FragmentHomeNosavedBinding) setLayoutVisibility(Layouts.NOSAVED);
            binding.setup.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.nav_setup));
        } else {
            FragmentHomeSavedBinding binding = (FragmentHomeSavedBinding) setLayoutVisibility(Layouts.SAVED);
            Map<String, ?> set = sharedPreferences.getAll();
            String[] list = set.keySet().toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.fragment_home_saved_textview, list);
            binding.homeList.setAdapter(adapter);
            binding.homeList.setClickable(true);
            binding.homeList.setOnItemClickListener((parent, view, position, id) -> {
                FragmentHomeMapBinding binding1 = (FragmentHomeMapBinding) setLayoutVisibility(Layouts.MAP);
                binding1.mapView.onCreate(savedInstanceState);
                binding1.mapView.onResume();
                String name = Arrays.asList(list).get(position);
                binding1.mapView.getMapAsync(new MapFragment(name));
                Toast.makeText(getContext(), (String) set.get(name), Toast.LENGTH_SHORT).show();
            });
        }

        return rootbinding.getRoot();
    }

    private enum Layouts {
        SAVED,
        NOSAVED,
        MAP
    }

    private Object setLayoutVisibility(Layouts layout) {
        Object returnlayout = rootbinding.lay1;
        switch (layout) {
            case SAVED:
                rootbinding.lay1.getRoot().setVisibility(View.VISIBLE);
                rootbinding.lay2.getRoot().setVisibility(View.GONE);
                rootbinding.lay3.getRoot().setVisibility(View.GONE);
                returnlayout = rootbinding.lay1;
                break;
            case NOSAVED:
                rootbinding.lay1.getRoot().setVisibility(View.GONE);
                rootbinding.lay2.getRoot().setVisibility(View.VISIBLE);
                rootbinding.lay3.getRoot().setVisibility(View.GONE);
                returnlayout = rootbinding.lay2;
                break;
            case MAP:
                rootbinding.lay1.getRoot().setVisibility(View.GONE);
                rootbinding.lay2.getRoot().setVisibility(View.GONE);
                rootbinding.lay3.getRoot().setVisibility(View.VISIBLE);
                returnlayout = rootbinding.lay3;
        }
        return returnlayout;
    }
}