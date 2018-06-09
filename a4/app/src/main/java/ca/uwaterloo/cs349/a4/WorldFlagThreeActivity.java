package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class WorldFlagThreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_flag_three);

        userModel.qCurrent = 3;

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

        RadioButton c;
        if (userModel.ans3 == 1) {
            c = findViewById(R.id.flag3c1);
            c.setChecked(true);
        } else if (userModel.ans3 == 2) {
            c = findViewById(R.id.flag3c2);
            c.setChecked(true);
        } else if (userModel.ans3 == 3) {
            c = findViewById(R.id.flag3c3);
            c.setChecked(true);
        } else if (userModel.ans3 == 4) {
            c = findViewById(R.id.flag3c4);
            c.setChecked(true);
        } else {
            // Do nothing
        }

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.flag3c1:
                if (checked)
                    userModel.result1[2] = false;
                    userModel.ans3 = 1;
                break;
            case R.id.flag3c2:
                if (checked)
                    userModel.result1[2] = false;
                    userModel.ans3 = 2;
                break;
            case R.id.flag3c3:
                if (checked)
                    userModel.result1[2] = true;
                    userModel.ans3 = 3;
                break;
            case R.id.flag3c4:
                if (checked)
                    userModel.result1[2] = false;
                    userModel.ans3 = 4;
        }
    }

    public void logout(View v) {
        Intent intent = new Intent(WorldFlagThreeActivity.this, MainActivity.class);
        startActivity(intent);
        userModel.clear();
    }

    public void nextOrfinish(View v) {
        Intent intent;
        if (userModel.qCurrent == userModel.getqNum()) {
            intent = new Intent(WorldFlagThreeActivity.this, ResultsActivity.class);

        } else {
            intent = new Intent(WorldFlagThreeActivity.this, WorldFlagFourActivity.class);
        }
        startActivity(intent);
    }

    public void goPrevious(View v) {
        Intent intent = new Intent(WorldFlagThreeActivity.this, WorldFlagTwoActivity.class);
        startActivity(intent);
    }
}
