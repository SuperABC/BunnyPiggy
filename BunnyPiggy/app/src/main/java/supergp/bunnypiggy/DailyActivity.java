package supergp.bunnypiggy;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyActivity extends AppCompatActivity{
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;

    private static int SUB_PICTURE = 0;
    private static int SUB_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        LinearLayout act = (LinearLayout)findViewById(R.id.dailylayout);
        if (act != null) {
            if(hour < 8 || hour > 20){
                act.setBackgroundResource(R.drawable.facedark);
            }
            else{
                act.setBackgroundResource(R.drawable.facelight);
            }
        }

        gridView = (GridView) findViewById(R.id.gridview);
        dataList = new ArrayList<>();
        String name[]={
                "早安猪猪","一起吃早饭呀","每日一爱",
                "小树参天","美食博主","午觉打卡",
                "发呆想猪","运动一刻","日常怼猪",
                "痴兔幻想","小兔崽子","早点睡啦"};
        int icon[]={
                R.drawable.morning,
                R.drawable.breakfast,
                R.drawable.love,
                R.drawable.tree,
                R.drawable.meal,
                R.drawable.noon,
                R.drawable.idle,
                R.drawable.sport,
                R.drawable.anger,
                R.drawable.dream,
                R.drawable.child,
                R.drawable.night,
        };
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", icon[i]);
            map.put("text", name[i]);
            dataList.add(map);
        }
        String[] from={"img", "text"};
        int[] to={R.id.daily_img, R.id.daily_text};
        adapter=new SimpleAdapter(this, dataList, R.layout.content_daily, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                switch (arg2){
                    case 0:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Socket socket;
                                try {
                                    socket = new Socket(HomeActivity.host, 4497);

                                    String socketData = "daily0:";
                                    BufferedWriter writer = new BufferedWriter(
                                            new OutputStreamWriter(socket.getOutputStream()));
                                    writer.write(socketData + "\0");
                                    writer.flush();
                                    BufferedReader reader = new BufferedReader(
                                            new InputStreamReader(socket.getInputStream()));
                                    String text=reader.readLine();
                                    socket.close();
                                    Looper.prepare();
                                    if(text.startsWith("success"))
                                        Toast.makeText(getApplicationContext(), "早上好呀小兔子~",
                                                Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getApplicationContext(), "已经早安过了呀~",
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
                        break;
                    case 1:
                        showPopupPic();
                        break;
                    case 2:
                        final EditText loveInput = new EditText(DailyActivity.this);
                        AlertDialog.Builder loveBuilder = new AlertDialog.Builder(DailyActivity.this);
                        loveBuilder.setTitle("每天爱一次").setIcon(android.R.drawable.ic_input_get)
                                .setView(loveInput).setNegativeButton("取消", null);
                        loveBuilder.setPositiveButton("我爱猪猪", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Socket socket;
                                        try {
                                            socket = new Socket(HomeActivity.host, 4497);

                                            String socketData = "daily2:" + loveInput.getText().toString();
                                            BufferedWriter writer = new BufferedWriter(
                                                    new OutputStreamWriter(socket.getOutputStream()));
                                            writer.write(socketData + "\0");
                                            writer.flush();
                                            BufferedReader reader = new BufferedReader(
                                                    new InputStreamReader(socket.getInputStream()));
                                            String text=reader.readLine();
                                            socket.close();
                                            Looper.prepare();
                                            if(text.startsWith("success"))
                                                Toast.makeText(getApplicationContext(), "爱死你了呢~",
                                                        Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(getApplicationContext(), "嘻嘻，知道你爱我，但是奖励不能多给哟~",
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
                        loveBuilder.show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "要等猪猪把树种下去才可以玩哟~",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        showPopupPic();
                        break;
                    case 5:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Socket socket;
                                try {
                                    socket = new Socket(HomeActivity.host, 4497);

                                    String socketData = "daily5:";
                                    BufferedWriter writer = new BufferedWriter(
                                            new OutputStreamWriter(socket.getOutputStream()));
                                    writer.write(socketData + "\0");
                                    writer.flush();
                                    socket.close();
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), "午安~",
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
                        break;
                    case 6:
                        Toast.makeText(getApplicationContext(), "嘿嘿，我也在想你~",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Intent intent = new Intent(DailyActivity.this, SportActivity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        final EditText angerInput = new EditText(DailyActivity.this);
                        AlertDialog.Builder angerBuilder = new AlertDialog.Builder(DailyActivity.this);
                        angerBuilder.setTitle("怼一怼更健康").setIcon(android.R.drawable.ic_input_get)
                                .setView(angerInput).setNegativeButton("算了猪猪辣么可爱", null);
                        angerBuilder.setPositiveButton("滚啊", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Socket socket;
                                        try {
                                            socket = new Socket(HomeActivity.host, 4497);

                                            String socketData = "daily8:" + angerInput.getText().toString();
                                            BufferedWriter writer = new BufferedWriter(
                                                    new OutputStreamWriter(socket.getOutputStream()));
                                            writer.write(socketData + "\0");
                                            writer.flush();
                                            socket.close();
                                            Looper.prepare();
                                            Toast.makeText(getApplicationContext(), "我，我错了…别怼漏气了就好…",
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
                        angerBuilder.show();
                        break;
                    case 9:
                        final EditText dreamInput = new EditText(DailyActivity.this);
                        AlertDialog.Builder dreamBuilder = new AlertDialog.Builder(DailyActivity.this);
                        dreamBuilder.setTitle("兔子又在期待什么呀").setIcon(android.R.drawable.ic_input_get)
                                .setView(dreamInput).setNegativeButton("算了没什么", null);
                        dreamBuilder.setPositiveButton("就是这个", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Socket socket;
                                        try {
                                            socket = new Socket(HomeActivity.host, 4497);

                                            String socketData = "daily9:" + dreamInput.getText().toString();
                                            BufferedWriter writer = new BufferedWriter(
                                                    new OutputStreamWriter(socket.getOutputStream()));
                                            writer.write(socketData + "\0");
                                            writer.flush();
                                            BufferedReader reader = new BufferedReader(
                                                    new InputStreamReader(socket.getInputStream()));
                                            String text=reader.readLine();
                                            socket.close();
                                            Looper.prepare();
                                            if(text.startsWith("success"))
                                                Toast.makeText(getApplicationContext(), "猜猜什么时候梦想实现呢~",
                                                        Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(getApplicationContext(), "一天不要幻想太多次哟~",
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
                        dreamBuilder.show();
                        break;
                    case 10:
                        Toast.makeText(getApplicationContext(), "小兔崽子没出生呢，别着急~",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 11:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Socket socket;
                                try {
                                    socket = new Socket(HomeActivity.host, 4497);

                                    String socketData = "daily11:";
                                    BufferedWriter writer = new BufferedWriter(
                                            new OutputStreamWriter(socket.getOutputStream()));
                                    writer.write(socketData + "\0");
                                    writer.flush();
                                    BufferedReader reader = new BufferedReader(
                                            new InputStreamReader(socket.getInputStream()));
                                    String text=reader.readLine();
                                    socket.close();
                                    Looper.prepare();
                                    if(text.startsWith("success"))
                                        Toast.makeText(getApplicationContext(), "晚安安安~",
                                                Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getApplicationContext(), "晚安过了，兔叽快睡觉吧~",
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
                        break;
                }
            }
        });
    }

    private void showPopupPic(){
        View popView = View.inflate(this,R.layout.util_popup,null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);

        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SUB_PICTURE);
                popupWindow.dismiss();
            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent,SUB_CAMERA);
                popupWindow.dismiss();
            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {
            if (requestCode == SUB_PICTURE && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    final String picturePath = cursor.getString(columnIndex);
                    Bitmap photo = BitmapFactory.decodeFile(picturePath);
                    uploadPic(photo);
                    cursor.close();
                }
            }
            else if (requestCode == SUB_CAMERA){
                if (data != null && (data.getData() != null|| data.getExtras() != null)){
                    Uri uri =data.getData();
                    Bitmap photo = null;
                    if (uri != null) {
                        photo = BitmapFactory.decodeFile(uri.getPath());
                    }
                    if (photo == null) {
                        Bundle bundle =data.getExtras();
                        if (bundle != null){
                            photo = (Bitmap)bundle.get("data");
                            uploadPic(photo);
                        } else {
                            Toast.makeText(getApplicationContext(), "找不到图片",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    private void uploadPic(Bitmap pic){
        ByteBuffer buffer = ByteBuffer.allocate(pic.getWidth() * pic.getHeight()*4);
        pic.copyPixelsToBuffer(buffer);
        byte [] bytes= buffer.array();
        for(int i = 0; i < bytes.length; i++){
            if(bytes[i] < 0)
                bytes[i]=(byte)((bytes[i]+256)/2);
            else
                bytes[i] = (byte)(bytes[i]/2);
        }
        final String imgsrc = new String(bytes);
        final int width = pic.getWidth();
        final int height = pic.getHeight();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket;
                try {
                    socket = new Socket(HomeActivity.host, 4497);

                    String socketData = "pic:" + width + " " + height + " " + imgsrc;
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(socketData + "\0");
                    writer.flush();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String text=reader.readLine();
                    socket.close();
                    Looper.prepare();
                    if(text.startsWith("success"))
                        Toast.makeText(getApplicationContext(), "收到啦~兔子多吃点~",
                                Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "今天收到过了呢~",
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

}
