package com.maliprestige.Presenter.Splash;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.DAOSlide;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.View.Interfaces.SplashView;

import java.util.ArrayList;

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
                String clienToken = HomePresenter.retrieveClientToken(context);
                if(clienToken == null || clienToken.isEmpty()){
                    HomePresenter.saveClientToken(context, "MP_CLIENT_DECONNECTED");
                }
                //--
                iSplash.hideHeader();
                iSplash.initialize();
                iSplash.events();

                iSplash.messageVisibility(View.GONE);

                // Verify if slide data has never saved
                DAOSlide daoSlide = new DAOSlide(context);
                if(daoSlide.getAll() == null || (daoSlide.getAll() != null && daoSlide.getAll().size()==0)){
                    if(HomePresenter.isMobileConnected(context)){
                        iSplash.displayHome();
                    }
                    else{
                       iSplash.messageVisibility(View.VISIBLE);
                    }
                }
                else{
                    iSplash.displayHome();
                }
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
