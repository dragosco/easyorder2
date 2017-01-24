package motacojo.mbds.fr.easyorder30.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by cojoc on 26/10/2016.
 */

public abstract class TextValidator implements TextWatcher {

    private final TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView);

    @Override
    final public void afterTextChanged(Editable s) {
        validate(textView);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}
