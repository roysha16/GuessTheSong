package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread mSplashThread = new Thread(){
            @Override
            public void run(){
                try{
                    synchronized (this){
                        wait(3000);
                    }
                }
                catch (InterruptedException ex) {
                }
                finish();

                Intent intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
            }
        };
        mSplashThread.start();

    }
}