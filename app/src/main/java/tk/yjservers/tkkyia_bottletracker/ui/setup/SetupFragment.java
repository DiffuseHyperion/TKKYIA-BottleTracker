package tk.yjservers.tkkyia_bottletracker.ui.setup;

import static tk.yjservers.tkkyia_bottletracker.MainActivity.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.HashSet;

import tk.yjservers.tkkyia_bottletracker.R;
import tk.yjservers.tkkyia_bottletracker.databinding.FragmentSetupBinding;

public class SetupFragment extends Fragment {

    private FragmentSetupBinding binding;

    public static HashSet<String> setDef = new HashSet<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSetupBinding.inflate(inflater, container, false);

        EditText IP1 = setupIP(binding.editIP1);
        EditText IP2 = setupIP(binding.editIP2);
        EditText IP3 = setupIP(binding.editIP3);
        EditText IP4 = setupIP(binding.editIP4);

        binding.save.setOnClickListener(v -> {
            EditText editName = binding.editName;
            if (editName.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Name cannot be empty!", Toast.LENGTH_SHORT).show();
            } else if (edittextempty(IP1) || edittextempty(IP2) || edittextempty(IP3) || edittextempty(IP4)) {
                Toast.makeText(getContext(), "All four numbers must be filled!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = requireActivity().getSharedPreferences(preference, Context.MODE_PRIVATE).edit();

                editor.putString(editName.getText().toString(), IP1.getText().toString() + "." + IP2.getText().toString() + "." + IP3.getText().toString() + "." + IP4.getText().toString());
                editor.apply();

                NavHostFragment.findNavController(this).navigate(R.id.nav_home);
            }
        });
        return binding.getRoot();
    }

    public boolean edittextempty (EditText txt) {
        return txt.getText().toString().isEmpty();
    }

    public EditText setupIP(EditText IP) {
        /*
        IP.setFilters(new InputFilter[]{ new SetupIPFilter(0, 255, getContext())});
        IP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                View view = requireActivity().getCurrentFocus();
                TextView text = (TextView) view;
                if (count > before) {
                    // adding text
                    if (text != null && text.length() > 2) {
                        View next = text.focusSearch(View.FOCUS_RIGHT);
                        if (next != null) {
                            next.requestFocus();
                        }
                    }
                } else if (count < before) {
                    // removing text
                    if (text != null && text.length() < 1) {
                        View next = text.focusSearch(View.FOCUS_LEFT);
                        if (next != null && next.getId() != R.id.editName) {
                            next.requestFocus();
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
         */
        return IP;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}