package secapstone.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;


enum SearchState
{
    FilledSearch, FilledClear, EmptySearch;
}

public class EditTextSearch extends android.support.v7.widget.AppCompatEditText {

    private Activity activity;
    private Button searchButton;
    public SearchState state;

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
    protected int[] onCreateDrawableState(int extraSpace) {
        activity = (Activity) getContext();
        searchButton = activity.findViewById(R.id.searchArtisanButton);
        state = SearchState.EmptySearch;

        return super.onCreateDrawableState(extraSpace);
    }

    @Override
    public boolean onKeyPreIme( int key_code, KeyEvent event ) {
        if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP ) {
            this.clearFocus();
        }

        return super.onKeyPreIme(key_code, event);
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        if (this.getText().length() != 0) {
            setFilledClearState();
        } else {
            setEmptySearchState();
        }
    }

    @Override
    public void onFocusChanged(boolean arg0, int arg1, Rect arg2) {
        super.onFocusChanged(arg0, arg1, arg2);

        if (this.hasFocus()) {
            if (this.getText().length() != 0) {
                setFilledClearState();
            } else {
                setEmptySearchState();
            }
        } else {
            if (this.getText().length() != 0) {
                setFilledSearchState();
            } else {
                setEmptySearchState();
            }
        }
    }

    public void setFilledSearchState() {
        this.setBackground(getResources().getDrawable(R.drawable.search_background_filled));
        this.setTextColor(getResources().getColor(R.color.white));
        if (searchButton != null) {
            searchButton.setBackgroundResource(R.drawable.search_icon_white);
        }

        state = SearchState.FilledSearch;
    }

    public void setFilledClearState() {
        this.setBackground(getResources().getDrawable(R.drawable.search_background_filled));
        this.setTextColor(getResources().getColor(R.color.white));
        if (searchButton != null) {
            searchButton.setBackgroundResource(R.drawable.icon_close_white);
        }

        state = SearchState.FilledClear;
    }

    public void setEmptySearchState() {
        this.setBackground(getResources().getDrawable(R.drawable.search_background));
        this.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        if (searchButton != null) {
            searchButton.setBackgroundResource(R.drawable.search_icon);
        }

        state = SearchState.EmptySearch;
    }

}
