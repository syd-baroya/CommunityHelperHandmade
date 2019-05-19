package secapstone.helper.pages.custom_ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.TimeZone;

public class MyEditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    CustomTextField editText;
    private Context context;

    private int day;
    private int month;
    private int year;

    public MyEditTextDatePicker(Context context, CustomTextField editText) {
        this.context = context;
        this.editText = editText;
        this.editText.setOnClickListener(this);

        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        updateHint();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;
        updateDisplay();
    }
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    private void updateDisplay() {
        editText.setText(new StringBuilder()
                .append(month + 1).append("/").append(day).append("/").append(this.year).append(" "));
    }

    private void updateHint() {
        editText.setHint(new StringBuilder()
                .append(month + 1).append("/").append(day).append("/").append(this.year).append(" "));
    }
}