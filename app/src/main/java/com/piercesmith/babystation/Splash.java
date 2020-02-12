package com.piercesmith.babystation;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    private ProgressBar progressBar;
    private Timer timer;
    private int i = 0;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.logo);

        Typewriter writer = (Typewriter) findViewById(R.id.typewriter);
        writer.setCharacterDelay(150);
        writer.animateText("memories made to last");

        progressBar = findViewById(R.id.progressBar);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (i < 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    progressBar.setProgress(i);
                    i++;
                }
                else {
                    timer.cancel();
                    startActivity(new Intent(Splash.this, ScrollingActivity.class));
                    finish();
                }
            }
        }, 1000, 50);

        // Fade in animation
        ObjectAnimator fadeIMG = ObjectAnimator.ofFloat(logo, View.ALPHA, 0.0f, 1.0f);
        ObjectAnimator fadeTW = ObjectAnimator.ofFloat(writer, View.ALPHA, 0.0f, .5f);
        ObjectAnimator fadePB = ObjectAnimator.ofFloat(progressBar, View.ALPHA, 0.0f, 1.0f);
        fadeIMG.setDuration(1000);
        fadeTW.setDuration(2000);
        fadePB.setDuration(3000);
        fadeIMG.start();
        fadeTW.start();
        fadePB.start();
    }
}
