package tk.yjservers.tkkyia_bottletracker.ui.setup;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

public class SetupIPFilter implements InputFilter {

    private final int min;
    private final int max;
    public Context context;

    public SetupIPFilter(int min, int max, Context context) {
        this.min = min;
        this.max = max;
        this.context = context;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        Toast.makeText(context, "Number must be within 0-255!", Toast.LENGTH_SHORT).show();
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}