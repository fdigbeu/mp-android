package com.maliprestige.Presenter.Splash;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.maliprestige.View.Interfaces.SplashView;

public class SplashPresenter {

    private CountDownTimer downTimer;
    private SplashView.ISplash iSplash;

    // Constructor
    public SplashPresenter(SplashView.ISplash iSplash) {
        this.iSplash = iSplash;
    }

    // Method allows to load splash screen data
    public void loadSplashData(Context context){
        try {
            if(iSplash != null){
                iSplash.hideHeader();
                iSplash.initialize();
                iSplash.events();
                iSplash.displayHome();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "SplashPresenter-->loadSplashData() : "+ex.getMessage());
        }
    }

    // Method allows to cancel CountDownTimer
    public void cancelCountDownTimer(CountDownTimer countDownTimer){
        try {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "SplashPresenter-->cancelCountDownTimer() : "+ex.getMessage());
        }
    }
}
