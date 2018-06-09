package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class TopicSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_selection);

        this.setTitle("Topic Selection");
        TextView userName = findViewById(R.id.welcome_user);
        userName.setText("Welcome "+userModel.getName());

        Spinner qnum_selector = findViewById(R.id.qnum_selector);

        String[] items = new String[] { "1", "2", "3","4","5"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);

        qnum_selector.setAdapter(adapter);

        qnum_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d("Debug", "Position chosen is "+ position);
                userModel.setqNum(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void logout(View v) {
        Intent intent = new Intent(TopicSelectionActivity.this, MainActivity.class);
        startActivity(intent);
        userModel.clear();
    }

    public void load(View v) {
        Intent intent = new Intent(TopicSelectionActivity.this, WorldFlagOneActivity.class);
        startActivity(intent);
    }

}
