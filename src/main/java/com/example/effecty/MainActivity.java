package com.example.effecty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To start FirstPage activity.
        Intent firstIntent = new Intent(this, FirstPage.class);
        startActivity(firstIntent);
    }
}
