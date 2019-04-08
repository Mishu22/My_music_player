package com.example.jarin.music_player;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class homeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        progressBar = findViewById(R.id.p_bar);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startapp();
            }
        });

        thread.start();
    }

    private void doWork() {

        for (progress = 20; progress <= 100; progress =progress+ 20) {
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void startapp() {
        Intent homeIntent = new Intent(homeActivity.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
