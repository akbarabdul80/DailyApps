package com.papb.todo.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.papb.todo.R;
import com.papb.todo.root.App;
import com.papb.todo.ui.login.LoginActivity;
import com.papb.todo.ui.main.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        ImageView app_logo = findViewById(R.id.ivLogo);
        app_logo.startAnimation(app_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (App.session.isLogin()) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}