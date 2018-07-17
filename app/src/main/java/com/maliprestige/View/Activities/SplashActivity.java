package com.maliprestige.View.Activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.maliprestige.Presenter.Splash.SplashPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.SplashView;

public class SplashActivity extends AppCompatActivity implements SplashView.ISplash{

    // Ref widgets
    private ImageView imageFemme;
    private ImageView imageHomme;
    private ImageView imageEnfant;
    private ImageView imageExotique;
    private ImageView imageLogo;
    private CountDownTimer downTimer;
    // Presenter
    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        // Load data from presenter
        splashPresenter = new SplashPresenter(this);
        splashPresenter.loadSplashData(SplashActivity.this);
    }

    @Override
    public void initialize() {
        imageFemme = findViewById(R.id.imageFemme);
        imageHomme = findViewById(R.id.imageHomme);
        imageEnfant = findViewById(R.id.imageEnfant);
        imageExotique = findViewById(R.id.imageExotique);
        imageLogo = findViewById(R.id.imageLogo);
    }

    @Override
    public void events() {
        // Animation Top to Bottom
        Animation animTopToBottom = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_bottom);
        imageFemme.startAnimation(animTopToBottom);
        imageHomme.startAnimation(animTopToBottom);
        // Animation Bottom to Top
        Animation animBottomToTop = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_top);
        imageEnfant.startAnimation(animBottomToTop);
        imageExotique.startAnimation(animBottomToTop);
        // Animation Right to Left
        Animation animRightToLeft = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_left);
        imageLogo.startAnimation(animRightToLeft);
    }

    @Override
    public void hideHeader() {
        getSupportActionBar().hide();
    }

    @Override
    public void displayHome() {
        downTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        splashPresenter.cancelCountDownTimer(downTimer);
        super.onBackPressed();
    }
}
