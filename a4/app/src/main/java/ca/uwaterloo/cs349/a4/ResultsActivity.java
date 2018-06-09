package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        this.setTitle("Results");

        TextView userName = findViewById(R.id.name);
        userName.setText("Name: "+userModel.getName());

        TextView result = findViewById(R.id.result);
        result.setText("Your Score: "+userModel.getFinalResult()+"/"+userModel.getqNum());
    }

    public void logout(View v) {
        Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
        startActivity(intent);
        userModel.clear();
    }

    public void topicSelection(View v) {
        Intent intent = new Intent(ResultsActivity.this, TopicSelectionActivity.class);
        startActivity(intent);
        String name = userModel.getName();
        userModel.clear();
        userModel.setName(name);
    }

}
