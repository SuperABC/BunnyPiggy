package supergp.bunnypiggy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Button luck = (Button)findViewById(R.id.reward);
        if (luck != null) {
            luck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CardActivity.this, RewardActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}