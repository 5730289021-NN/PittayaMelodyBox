package com.oaksmuth.pittayamelodybox;

/**
 * Created by Elm on 12/14/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;

public class Splash extends AppCompatActivity {
    public static PoemProvider data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            data = new PoemProvider(this);
        } catch (IOException e) {
            Toast.makeText(this, "Text File Not Found", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}