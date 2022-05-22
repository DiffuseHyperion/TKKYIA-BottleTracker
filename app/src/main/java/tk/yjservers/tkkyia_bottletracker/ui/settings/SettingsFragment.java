package tk.yjservers.tkkyia_bottletracker.ui.settings;

import static tk.yjservers.tkkyia_bottletracker.MainActivity.preference;

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

import tk.yjservers.tkkyia_bottletracker.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences(preference, Context.MODE_PRIVATE).edit();

        binding.cleardata.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("Are you sure?");
            builder.setMessage("This action cannot be reversed...");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        editor.clear();
                        editor.apply();
                        Toast.makeText(getContext(), "All data has been cleared.", Toast.LENGTH_SHORT).show();
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return binding.getRoot();
    }
}