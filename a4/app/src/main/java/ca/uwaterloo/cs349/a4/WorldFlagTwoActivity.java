package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class WorldFlagTwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_flag_two);

        userModel.qCurrent = 2;

        this.setTitle("Question");

        TextView state = findViewById(R.id.state);
        state.setText(userModel.qCurrent+"/"+userModel.getqNum());

        TextView userName = findViewById(R.id.name);
        userName.setText("Name: "+userModel.getName());

        Button next = findViewById(R.id.finish);
        if (userModel.getqNum() == userModel.qCurrent) {
            next.setText("Finish");
        } else {
            next.setText("Next");
        }

        CheckBox c;
        c = findViewById(R.id.flag2c1);
        c.setChecked(userModel.ans2[0]);

        c = findViewById(R.id.flag2c2);
        c.setChecked(userModel.ans2[1]);

        c = findViewById(R.id.flag2c3);
        c.setChecked(userModel.ans2[2]);

        c = findViewById(R.id.flag2c4);
        c.setChecked(userModel.ans2[3]);
    }

    public void onChoiceSelected(View v) {
        CheckBox flag2c1 = findViewById(R.id.flag2c1);
        CheckBox flag2c2 = findViewById(R.id.flag2c2);
        CheckBox flag2c3 = findViewById(R.id.flag2c3);
        CheckBox flag2c4 = findViewById(R.id.flag2c4);
        if (flag2c1.isChecked() && flag2c3.isChecked() &&
                !flag2c2.isChecked() && !flag2c4.isChecked()) {
            userModel.result1[1] = true;
        } else {
            userModel.result1[1] = false;
        }
        userModel.ans2[0] = flag2c1.isChecked();
        userModel.ans2[1] = flag2c2.isChecked();
        userModel.ans2[2] = flag2c3.isChecked();
        userModel.ans2[3] = flag2c4.isChecked();
    }

    public void logout(View v) {
        Intent intent = new Intent(WorldFlagTwoActivity.this, MainActivity.class);
        startActivity(intent);
        userModel.clear();
    }

    public void nextOrfinish(View v) {
        Intent intent;
        if (userModel.qCurrent == userModel.getqNum()) {
            intent = new Intent(WorldFlagTwoActivity.this, ResultsActivity.class);

        } else {
            intent = new Intent(WorldFlagTwoActivity.this, WorldFlagThreeActivity.class);
        }
        startActivity(intent);
    }

    public void goPrevious(View v) {
        Intent intent = new Intent(WorldFlagTwoActivity.this, WorldFlagOneActivity.class);
        startActivity(intent);
    }
}
