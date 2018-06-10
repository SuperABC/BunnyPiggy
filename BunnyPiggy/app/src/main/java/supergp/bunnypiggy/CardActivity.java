package supergp.bunnypiggy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;

public class CardActivity extends AppCompatActivity {

    public Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg){
            ImageView box = (ImageView)findViewById(R.id.box);
            if (box != null) {
                box.setVisibility(View.INVISIBLE);
            }
            Button luck = (Button)findViewById(R.id.reward);
            if (luck != null) {
                luck.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        RelativeLayout act = (RelativeLayout)findViewById(R.id.cardlayout);
        if (act != null) {
            if(hour < 8 || hour > 20){
                act.setBackgroundResource(R.drawable.facedark);
            }
            else{
                act.setBackgroundResource(R.drawable.facelight);
            }
        }

        final ImageView box = (ImageView)findViewById(R.id.box);
        final Button luck = (Button)findViewById(R.id.reward);
        if (luck != null) {
            luck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (box != null) {
                        box.setVisibility(View.VISIBLE);
                        luck.setEnabled(false);
                        AnimationSet animationSet = new AnimationSet(true);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                        alphaAnimation.setDuration(400);
                        animationSet.addAnimation(alphaAnimation);
                        ScaleAnimation scaleAnimation = new ScaleAnimation(
                                4.f, 1.f,4.f, 1.f,
                                Animation.RELATIVE_TO_SELF,.5f,
                                Animation.RELATIVE_TO_SELF,.5f);
                        scaleAnimation.setDuration(400);
                        animationSet.addAnimation(scaleAnimation);
                        TranslateAnimation translateAnimation = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF,0.f,
                                Animation.RELATIVE_TO_SELF,0.f,
                                Animation.RELATIVE_TO_SELF,-1.5f,
                                Animation.RELATIVE_TO_SELF,0.f);
                        translateAnimation.setDuration(400);
                        animationSet.addAnimation(translateAnimation);
                        box.startAnimation(animationSet);
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Socket socket;
                                        Message m = new Message();
                                        h.sendMessage(m);
                                        try {
                                            socket = new Socket("192.168.1.184", 4497);

                                            String socketData = "card:";
                                            BufferedWriter writer = new BufferedWriter(
                                                    new OutputStreamWriter(socket.getOutputStream()));
                                            writer.write(socketData + "\0");
                                            writer.flush();
                                            BufferedReader reader = new BufferedReader(
                                                    new InputStreamReader(socket.getInputStream()));
                                            String text = "", append;
                                            while((append=reader.readLine())!=null){
                                                text += append + '\n';
                                            }
                                            giveReward(text);
                                            socket.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }, 1600);
                    }
                }
            });
        }

    }

    private void giveReward(String text){
        Looper.prepare();
        switch (text){
            case "0\n":
                Toast.makeText(getApplicationContext(), "木有抽中…",
                        Toast.LENGTH_SHORT).show();
                break;
            case "1\n":
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(CardActivity.this);
                normalDialog.setIcon(R.mipmap.ic_launcher);
                normalDialog.setTitle("兔子好胖胖");
                normalDialog.setMessage("哇，抽中了1级卡片！");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                normalDialog.setNegativeButton("关闭",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                normalDialog.show();
        }
        Looper.loop();
    }
}