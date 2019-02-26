package secapstone.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;

public class EditTextSearch extends android.support.v7.widget.AppCompatEditText {

    Activity activity = null;
    Button searchButton = null;

    public EditTextSearch( Context context )
    {
        super( context );
    }

    public EditTextSearch( Context context, AttributeSet attribute_set )
    {
        super( context, attribute_set );
    }

    public EditTextSearch( Context context, AttributeSet attribute_set, int def_style_attribute )
    {
        super( context, attribute_set, def_style_attribute );
    }

    @Override
    public boolean onKeyPreIme( int key_code, KeyEvent event )
    {
        if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP ) {
            this.clearFocus();
            if (searchButton != null) {
                searchButton.setBackgroundResource(R.drawable.search_icon_white);
            }
        }
        if (this.getText().equals("  ") || this.getText().equals(" ")) {
            this.setText("");
        }

        return super.onKeyPreIme(key_code, event);
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        if (activity == null) {
            activity = (Activity) getContext();
        }
        if (searchButton == null && activity != null) {
            searchButton = activity.findViewById(R.id.searchArtisanButton);
        }


        if (this.getText().length() != 0) {
            this.setBackground(getResources().getDrawable(R.drawable.search_background_filled));
            this.setTextColor(getResources().getColor(R.color.white));
            if (searchButton != null) {
                searchButton.setBackgroundResource(R.drawable.icon_close_white);
            }
        } else {
            this.setBackground(getResources().getDrawable(R.drawable.search_background));
            this.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if (searchButton != null) {
                searchButton.setBackgroundResource(R.drawable.search_icon);
            }
        }
    }

    @Override
    public void onFocusChanged(boolean arg0, int arg1, Rect arg2) {
        super.onFocusChanged(arg0, arg1, arg2);

        if (this.hasFocus()) {
            if (this.getText().length() != 0) {
                if (searchButton != null) {
                    searchButton.setBackgroundResource(R.drawable.icon_close_white);
                }
            } else {
                if (searchButton != null) {
                    searchButton.setBackgroundResource(R.drawable.search_icon);
                }
            }
        } else {
            if (this.getText().length() != 0) {
                if (searchButton != null) {
                    searchButton.setBackgroundResource(R.drawable.search_icon_white);
                }
            } else {
                if (searchButton != null) {
                    searchButton.setBackgroundResource(R.drawable.search_icon);
                }
            }
        }
    }

}
