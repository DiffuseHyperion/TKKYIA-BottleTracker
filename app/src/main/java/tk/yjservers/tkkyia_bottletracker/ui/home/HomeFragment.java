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

import java.util.Map;

import tk.yjservers.tkkyia_bottletracker.R;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(preference, Context.MODE_PRIVATE);
        if (sharedPreferences.getAll().isEmpty()) {
            binding.lay1.getRoot().setVisibility(View.GONE);
            binding.lay2.getRoot().setVisibility(View.VISIBLE);
            binding.lay2.setup.setOnClickListener(v -> {
                NavHostFragment.findNavController(this).navigate(R.id.nav_setup);
            });
        } else {
            binding.lay1.getRoot().setVisibility(View.VISIBLE);
            binding.lay2.getRoot().setVisibility(View.GONE);
            Map<String, ?> set = sharedPreferences.getAll();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.fragment_home_saved_textview, set.keySet().toArray(new String[0]));
            binding.lay1.homeList.setAdapter(adapter);
        }

        return binding.getRoot();
    }
}