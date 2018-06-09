package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class WorldFlagOneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_flag_one);

        userModel.qCurrent = 1;

        this.setTitle("Question");

        TextView state = findViewById(R.id.state);
        state.setText(userModel.qCurrent+"/"+userModel.getqNum());

        TextView userName = findViewById(R.id.name);
        userName.setText("Name: "+userModel.getName());

        Button previous = findViewById(R.id.previous);
        previous.setEnabled(false);

        Button next = findViewById(R.id.finish);
        if (userModel.getqNum() == userModel.qCurrent) {
            next.setText("Finish");
        } else {
            next.setText("Next");
        }

        RadioButton c;
        if (userModel.ans1 == 1) {
            c = findViewById(R.id.flag1c1);
            c.setChecked(true);
        } else if (userModel.ans1 == 2) {
            c = findViewById(R.id.flag1c2);
            c.setChecked(true);
        } else if (userModel.ans1 == 3) {
            c = findViewById(R.id.flag1c3);
            c.setChecked(true);
        } else if (userModel.ans1 == 4) {
            c = findViewById(R.id.flag1c4);
            c.setChecked(true);
        } else {
            // Do nothing
        }

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.flag1c1:
                if (checked)
                    userModel.result1[0] = true;
                    userModel.ans1 = 1;
                    break;
            case R.id.flag1c2:
                if (checked)
                    userModel.result1[0] = false;
                    userModel.ans1 = 2;
                    break;
            case R.id.flag1c3:
                if (checked)
                    userModel.result1[0] = false;
                    userModel.ans1 = 3;
                    break;
            case R.id.flag1c4:
                if (checked)
                    userModel.result1[0] = false;
                    userModel.ans1 = 4;
        }
    }

    public void logout(View v) {
        Intent intent = new Intent(WorldFlagOneActivity.this, MainActivity.class);
        startActivity(intent);
        userModel.clear();
    }

    public void nextOrfinish(View v) {
        if (userModel.qCurrent == userModel.getqNum()) {
            Intent intent = new Intent(WorldFlagOneActivity.this, ResultsActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(WorldFlagOneActivity.this, WorldFlagTwoActivity.class);
            startActivity(intent);
        }
    }
}
