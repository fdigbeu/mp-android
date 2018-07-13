package com.maliprestige.Presenter.WebHtml;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.WebHtmlView;

public class WebHtmlPresenter implements WebHtmlView.IPresenter{

    private WebHtmlView.IWebHtml iWebHtml;

    private WebHtmlClient webHtmlClient;

    public WebHtmlPresenter(WebHtmlView.IWebHtml iWebHtml) {
        this.iWebHtml = iWebHtml;
    }

    public void loadWebHtmlData(Context context, Intent intent){
        try {
            if(iWebHtml != null && context != null && intent != null){
                iWebHtml.hideHeader();
                iWebHtml.initialize();
                iWebHtml.events();
                //--
                if(HomePresenter.isMobileConnected(context)) {

                    iWebHtml.progressVisibility(View.VISIBLE);
                    iWebHtml.btnCloseVisibility(View.GONE);
                    iWebHtml.webViewVisibility(View.GONE);
                    //--
                    webHtmlClient = new WebHtmlClient();
                    webHtmlClient.setiPresenter(this);
                    //--
                    String url = intent.getStringExtra("url");

                    iWebHtml.loadWebView(webHtmlClient, url);
                }
                else{
                    iWebHtml.messageError(context.getResources().getString(R.string.no_connection));
                }
            }
            else{
                iWebHtml.closeActivity();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "WebHtmlPresenter-->loadWebHtmlData() : "+ex.getMessage());
        }
    }

    public void closeActivity(){
        try {
            if(iWebHtml != null) {
                iWebHtml.closeActivity();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "WebHtmlPresenter-->closeActivity() : "+ex.getMessage());
        }
    }

    @Override
    public void onLoadWebHtmlFinished() {
        try {
            new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() {
                    iWebHtml.btnCloseVisibility(View.VISIBLE);
                    iWebHtml.progressVisibility(View.GONE);
                    iWebHtml.webViewVisibility(View.VISIBLE);
                    iWebHtml.messageError("");
                }
            }.start();
        }
        catch (Exception ex){
            Log.i("TAG_EXCEPTION", "Erreur : onLoadSucces()");
            iWebHtml.messageError("Erreur ! \n\n"+ex.getMessage());
        }
    }

    @Override
    public void onLoadWebHtmlFailure() {
        iWebHtml.btnCloseVisibility(View.VISIBLE);
        iWebHtml.progressVisibility(View.GONE);
        Log.e("TAG_ERROR", "WebHtmlPresenter-->onLoadWebHtmlFailure()");
        iWebHtml.messageError("Erreur de chargement ! \n\nUne erreur est survenue pendant le chargement des données.");
    }

    @Override
    public void onLoadWebHtmlHttpError() {
        iWebHtml.btnCloseVisibility(View.VISIBLE);
        iWebHtml.progressVisibility(View.GONE);
        Log.e("TAG_ERROR", "WebHtmlPresenter-->onLoadWebHtmlHttpError()");
        iWebHtml.messageError("Erreur Http ! \n\nUne erreur est survenue pendant le chargement des données.");
    }
}
