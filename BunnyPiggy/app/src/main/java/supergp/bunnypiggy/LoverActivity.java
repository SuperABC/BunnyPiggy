package supergp.bunnypiggy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket;
                try {
                    socket = new Socket("192.168.1.184", 4497);

                    String socketData = "lover:";
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(socketData + "\0");
                    writer.flush();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String text = "", append;
                    while((append=reader.readLine())!=null){
                        text += append;
                    }
                    TextView content = (TextView)findViewById(R.id.text);
                    if (content != null) {
                        content.setText(text);
                    }
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
