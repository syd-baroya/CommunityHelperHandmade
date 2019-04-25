package secapstone.helper.pages.custom_ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;

import secapstone.helper.R;


public class CustomTextField extends androidx.appcompat.widget.AppCompatEditText {

    enum State
    {
        filled, empty;
    }

    public State state;

    public CustomTextField( Context context )
    {
        super( context );
    }

    public CustomTextField( Context context, AttributeSet attribute_set )
    {
        super( context, attribute_set );
    }

    public CustomTextField( Context context, AttributeSet attribute_set, int def_style_attribute )
    {
        super( context, attribute_set, def_style_attribute );
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        state = State.empty;

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
            setFilledState();
        } else {
            setEmptyState();
        }
    }

    @Override
    public void onFocusChanged(boolean arg0, int arg1, Rect arg2) {
        super.onFocusChanged(arg0, arg1, arg2);

//        if (this.hasFocus()) {
//            if (this.getText().length() != 0) {
//                setFilledClearState();
//            } else {
//                setEmptySearchState();
//            }
//        } else {
//            if (this.getText().length() != 0) {
//                setFilledSearchState();
//            } else {
//                setEmptySearchState();
//            }
//        }
    }

    public void setFilledState() {
        this.setBackground(getResources().getDrawable(R.drawable.background_search_filled));
        this.setTextColor(getResources().getColor(R.color.white));

        state = State.filled;
    }

    public void setEmptyState() {
        this.setBackground(getResources().getDrawable(R.drawable.background_search));
        this.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        state = State.empty;
    }

    public static final Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

}
