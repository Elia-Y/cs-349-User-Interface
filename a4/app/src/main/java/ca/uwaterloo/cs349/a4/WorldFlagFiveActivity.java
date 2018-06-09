package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class WorldFlagFiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_flag_five);

        userModel.qCurrent = 5;

        this.setTitle("Question");

        TextView state = findViewById(R.id.state);
        state.setText(userModel.qCurrent+"/"+userModel.getqNum());

        TextView userName = findViewById(R.id.name);
        userName.setText("Name: "+userModel.getName());

        Button next = findViewById(R.id.finish);
        next.setText("Finish");

        CheckBox c;
        c = findViewById(R.id.flag5c1);
        c.setChecked(userModel.ans5[0]);

        c = findViewById(R.id.flag5c2);
        c.setChecked(userModel.ans5[1]);

        c = findViewById(R.id.flag5c3);
        c.setChecked(userModel.ans5[2]);

        c = findViewById(R.id.flag5c4);
        c.setChecked(userModel.ans5[3]);

    }

    public void onChoiceSelected(View v) {
        CheckBox flag5c1 = findViewById(R.id.flag5c1);
        CheckBox flag5c2 = findViewById(R.id.flag5c2);
        CheckBox flag5c3 = findViewById(R.id.flag5c3);
        CheckBox flag5c4 = findViewById(R.id.flag5c4);
        if (flag5c3.isChecked() && flag5c4.isChecked() &&
                !flag5c1.isChecked() && !flag5c2.isChecked()) {
            userModel.result1[4] = true;
        } else {
            userModel.result1[4] = false;
        }
        userModel.ans5[0] = flag5c1.isChecked();
        userModel.ans5[1] = flag5c2.isChecked();
        userModel.ans5[2] = flag5c3.isChecked();
        userModel.ans5[3] = flag5c4.isChecked();
    }

    public void logout(View v) {
        Intent intent = new Intent(WorldFlagFiveActivity.this, MainActivity.class);
        startActivity(intent);
        userModel.clear();
    }

    public void nextOrfinish(View v) {
        // There are at most 5 questions
        Intent intent = new Intent(WorldFlagFiveActivity.this, ResultsActivity.class);
        startActivity(intent);
    }

    public void goPrevious(View v) {
        Intent intent = new Intent(WorldFlagFiveActivity.this, WorldFlagFourActivity.class);
        startActivity(intent);
    }
}
