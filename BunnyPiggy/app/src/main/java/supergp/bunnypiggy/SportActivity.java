package supergp.bunnypiggy;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SportActivity extends AppCompatActivity {

    private int totalTime = 0;
    private boolean running = false;
    private TextView timeText;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(running) {
                totalTime++;
                timeText.setText("运动中：" + totalTime / 3600 + ":" + (totalTime / 60) % 60 + ":" + totalTime % 60);
                handler.postDelayed(this, 1000);
            }
        }
    };

    void setType(int t){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        LinearLayout act = (LinearLayout)findViewById(R.id.sportlayout);
        if (act != null) {
            if(hour < 8 || hour > 20){
                act.setBackgroundResource(R.drawable.facedark);
            }
            else{
                act.setBackgroundResource(R.drawable.facelight);
            }
        }

        Spinner type = (Spinner)findViewById(R.id.type);
        if (type != null) {
            type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setType(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        timeText = (TextView)findViewById(R.id.time);
        final Button tick = (Button)findViewById(R.id.tick);
        if (tick != null) {
            tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!running) {
                        tick.setText("结束");
                        timeText.setText("运动中：0:0:0");
                        handler.postDelayed(runnable, 1000);
                        running = true;
                    }
                    else{
                        tick.setText("打卡");
                        running = false;
                        totalTime = 0;
                    }
                }
            });
        }
    }
}