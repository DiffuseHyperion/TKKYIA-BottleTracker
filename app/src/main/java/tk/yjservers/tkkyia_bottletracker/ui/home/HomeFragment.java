package tk.yjservers.tkkyia_bottletracker.ui.home;

import static tk.yjservers.tkkyia_bottletracker.MainActivity.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import tk.yjservers.tkkyia_bottletracker.R;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentHomeBinding;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentHomeNosavedBinding;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentHomeSavedBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding rootbinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootbinding = FragmentHomeBinding.inflate(inflater, container, false);
        setLayoutVisibility(Layouts.SAVED);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(preference, Context.MODE_PRIVATE);
        if (sharedPreferences.getAll().isEmpty()) {
            FragmentHomeNosavedBinding binding = (FragmentHomeNosavedBinding) setLayoutVisibility(Layouts.NOSAVED);
            binding.setup.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.nav_setup));
        } else {
            FragmentHomeSavedBinding binding = (FragmentHomeSavedBinding) setLayoutVisibility(Layouts.SAVED);
            Map<String, ?> set = sharedPreferences.getAll();
            String[] list = set.keySet().toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.fragment_home_saved_textview, list);
            binding.list.setAdapter(adapter);
            binding.list.setClickable(true);
            binding.list.setOnItemClickListener((parent, view, position, id) -> {
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
                bundle.putString("name", Arrays.asList(list).get(position));
                NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.nav_map, bundle);
            });
        }

        return rootbinding.getRoot();
    }

    private enum Layouts {
        SAVED,
        NOSAVED,
    }

    private Object setLayoutVisibility(Layouts layout) {
        Object returnlayout = rootbinding.lay1;
        switch (layout) {
            case SAVED:
                rootbinding.lay1.getRoot().setVisibility(View.VISIBLE);
                rootbinding.lay2.getRoot().setVisibility(View.GONE);
                returnlayout = rootbinding.lay1;
                break;
            case NOSAVED:
                rootbinding.lay1.getRoot().setVisibility(View.GONE);
                rootbinding.lay2.getRoot().setVisibility(View.VISIBLE);
                returnlayout = rootbinding.lay2;
                break;
        }
        return returnlayout;
    }
}