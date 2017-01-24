package motacojo.mbds.fr.easyorder30.utils;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cojoc on 26/10/2016.
 */

public class FormValidator {
    private EditText editText;
    private boolean isOK;

    public FormValidator(final EditText editText) {
        this.editText = editText;
        editText.addTextChangedListener(new TextValidator(editText) {
            @Override
            public void validate(TextView textView) {

                Editable text = editText.getText();
                int inputType = editText.getInputType();
                Pattern pattern;
                Matcher matcher;
                switch (inputType) {
                    case InputType.TYPE_CLASS_PHONE :
                        pattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
                        matcher = pattern.matcher(text.toString());
                        if (!matcher.matches()) {
                            textView.setError("Le numéro de téléphone n'est pas valide. Il doit être de la forme +336*********");
                        } else {
                            textView.setError(null);
                        }
                        break;
                    case 33:
                        pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                        matcher = pattern.matcher(text.toString());
                        if (!matcher.matches()) {
                            textView.setError("L'adresse e-mail doit être de la forme exemple@exemple.com.");
                        } else {
                            textView.setError(null);
                        }
                        break;
                    case InputType.TYPE_TEXT_VARIATION_PASSWORD :
                        pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
                        matcher = pattern.matcher(text.toString());
                        if (!matcher.matches()) {
                            textView.setError("Le mot de passe doit avoir au moins 8 caractères, dont au moins un minuscul, un majuscul et un chiffre.");
                        } else {
                            textView.setError(null);
                        }
                        break;
                    default:
                        if (TextUtils.isEmpty(text)) {
                            textView.setError("Ce champ est obligatoire!");
                        } else {
                            textView.setError(null);
                        }
                }

            }
        });
    }

    public void identicalTo(final EditText editTextToCompare) {
        editText.addTextChangedListener(new TextValidator(editText) {
            @Override
            public void validate(TextView textView) {
                Editable text = editText.getText();
                if (!text.toString().equals(editTextToCompare.getText().toString())) {
                    textView.setError("Les mots de passe ne correspondent pas.");
                } else {
                    textView.setError(null);
                }
            }
        });
    }
}
