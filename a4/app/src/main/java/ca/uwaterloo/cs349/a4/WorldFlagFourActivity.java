package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class WorldFlagFourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_flag_four);

        userModel.qCurrent = 4;

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
        if (userModel.ans4 == 1) {
            c = findViewById(R.id.flag4c1);
            c.setChecked(true);
        } else if (userModel.ans4 == 2) {
            c = findViewById(R.id.flag4c2);
            c.setChecked(true);
        } else if (userModel.ans4 == 3) {
            c = findViewById(R.id.flag4c3);
            c.setChecked(true);
        } else if (userModel.ans4 == 4) {
            c = findViewById(R.id.flag4c4);
            c.setChecked(true);
        } else {
            // Do nothing
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.flag4c1:
                if (checked)
                    userModel.result1[3] = false;
                    userModel.ans4 = 1;
                break;
            case R.id.flag4c2:
                if (checked)
                    userModel.result1[3] = false;
                    userModel.ans4 = 2;
                break;
            case R.id.flag4c3:
                if (checked)
                    userModel.result1[3] = false;
                    userModel.ans4 = 3;
                break;
            case R.id.flag4c4:
                if (checked)
                    userModel.result1[3] = true;
                    userModel.ans4 = 4;
        }
    }

    public void logout(View v) {
        Intent intent = new Intent(WorldFlagFourActivity.this, MainActivity.class);
        startActivity(intent);
        userModel.clear();
    }

    public void nextOrfinish(View v) {
        Intent intent;
        if (userModel.qCurrent == userModel.getqNum()) {
            intent = new Intent(WorldFlagFourActivity.this, ResultsActivity.class);

        } else {
            intent = new Intent(WorldFlagFourActivity.this, WorldFlagFiveActivity.class);
        }
        startActivity(intent);
    }

    public void goPrevious(View v) {
        Intent intent = new Intent(WorldFlagFourActivity.this, WorldFlagThreeActivity.class);
        startActivity(intent);
    }
}
