package edu.mbhs.bekewled;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("help", "sos");

        ActionBar a = getSupportActionBar();
        if (a != null) {
            a.setTitle("");
        }
    }

    public void toGame(View v) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
}
