package tk.yjservers.tkkyia_bottletracker.ui.settings;

import static tk.yjservers.tkkyia_bottletracker.MainActivity.preference;
import static tk.yjservers.tkkyia_bottletracker.MainActivity.updateList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import tk.yjservers.tkkyia_bottletracker.R;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FragmentSettingsBinding binding = FragmentSettingsBinding.inflate(inflater, container, false);

        binding.cleardata.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setCancelable(true);
            builder.setTitle("Are you sure?");
            builder.setMessage("This action cannot be reversed...");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        SharedPreferences.Editor editor = requireActivity().getSharedPreferences(preference, Context.MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(getContext(), "All data has been cleared.", Toast.LENGTH_SHORT).show();

                        NavController navController = NavHostFragment.findNavController(this);
                        NavHostFragment.findNavController(this).navigate(R.id.nav_home);
                        updateList(requireActivity(), navController);
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return binding.getRoot();
    }
}