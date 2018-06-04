package supergp.bunnypiggy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class LoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lover);
        onRefresh();

        final TextView dateText;
        final int year,month,day;
        Calendar cal;

        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DAY_OF_MONTH);

        dateText=(TextView) findViewById(R.id.date);
        if (dateText != null) {
            dateText.setText(String.valueOf(year) + '-' +
                    String.valueOf(month + 1) + '-' + String.valueOf(day));
            dateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker arg0, int year, int month, int day) {
                            dateText.setText(year+"-"+(++month)+"-"+day);
                            onRefresh();
                        }
                    };
                    DatePickerDialog dialog=new DatePickerDialog(LoverActivity.this, 0,listener,year,month,day);
                    dialog.show();
                }
            });
        }
    }

    void onRefresh(){

    }
}
