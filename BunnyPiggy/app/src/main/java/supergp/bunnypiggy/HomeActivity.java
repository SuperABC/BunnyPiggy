package supergp.bunnypiggy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText input = new EditText(HomeActivity.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("兔子通知").setIcon(android.R.drawable.ic_input_get)
                            .setView(input).setNegativeButton("取消", null);
                    builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Socket socket;
                                    try {
                                        socket = new Socket("192.168.1.184", 4497);

                                        String socketData = "fab:" + input.getText().toString();
                                        BufferedWriter writer = new BufferedWriter(
                                                new OutputStreamWriter(socket.getOutputStream()));
                                        writer.write(socketData + "\0");
                                        writer.flush();
                                        socket.close();
                                        Looper.prepare();
                                        Toast.makeText(getApplicationContext(), "哼唧哼唧，猪猪收到了，我也爱你~",
                                                Toast.LENGTH_SHORT).show();
                                        Looper.loop();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Looper.prepare();
                                        Toast.makeText(getApplicationContext(), "哼唧哼唧，出问题了…",
                                                Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }
                            }).start();
                        }
                    });
                    builder.show();
                }
            });
        }

        Button dailyMsg = (Button) findViewById(R.id.daily_msg);
        if (dailyMsg != null) {
            dailyMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, DailyActivity.class);
                    startActivity(intent);
                }
            });
        }
        Button loverMsg = (Button) findViewById(R.id.lover_msg);
        if (loverMsg != null) {
            loverMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,LoverActivity.class);
                    startActivity(intent);
                }
            });
        }
        Button cardMsg = (Button) findViewById(R.id.card_msg);
        if (cardMsg != null) {
            cardMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,CardActivity.class);
                    startActivity(intent);
                }
            });
        }
        Button jokeMsg = (Button) findViewById(R.id.joke_msg);
        if (jokeMsg != null) {
            jokeMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,JokeActivity.class);
                    //startActivity(intent);
                }
            });
        }
        Button novelMsg = (Button) findViewById(R.id.novel_msg);
        if (novelMsg != null) {
            novelMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,NovelActivity.class);
                    //startActivity(intent);
                }
            });
        }
        Button secretMsg = (Button) findViewById(R.id.secret_msg);
        if (secretMsg != null) {
            secretMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,SecretActivity.class);
                    //startActivity(intent);
                }
            });
        }

        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);

        BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    /*Toast.makeText(getApplicationContext(), "屏幕亮起",
                            Toast.LENGTH_SHORT).show();*/
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    protected void onResume(){
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket;
                try {
                    socket = new Socket("192.168.1.184", 4497);

                    String socketData = "board:";
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
                    if(text.length()>1){
                        Looper.prepare();
                        new  AlertDialog.Builder(HomeActivity.this).
                                setTitle("猪猪的公告").setMessage(text).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Socket socket;
                                        try {
                                            socket = new Socket("192.168.1.184", 4497);
                                            String socketData = "boardok:";
                                            BufferedWriter writer = new BufferedWriter(
                                                    new OutputStreamWriter(socket.getOutputStream()));
                                            writer.write(socketData + "\0");
                                            writer.flush();
                                            socket.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).show();
                        Looper.loop();

                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_promise) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
