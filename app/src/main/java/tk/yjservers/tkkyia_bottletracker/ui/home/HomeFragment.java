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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Arrays;
import java.util.Map;

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
        NavController navController = NavHostFragment.findNavController(this);
        if (sharedPreferences.getAll().isEmpty()) {
            FragmentHomeNosavedBinding binding = (FragmentHomeNosavedBinding) setLayoutVisibility(Layouts.NOSAVED);
            binding.setup.setOnClickListener(v -> navController.navigate(R.id.nav_setup));
        } else {
            FragmentHomeSavedBinding binding = (FragmentHomeSavedBinding) setLayoutVisibility(Layouts.SAVED);
            Map<String, ?> set = sharedPreferences.getAll();
            String[] list = set.keySet().toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.fragment_home_saved_textview, list);
            binding.list.setAdapter(adapter);
            binding.list.setClickable(true);
            binding.list.setOnItemClickListener((parent, view, position, id) -> new MapMethods().navToMap(requireActivity(), navController, Arrays.asList(list).get(position)));
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