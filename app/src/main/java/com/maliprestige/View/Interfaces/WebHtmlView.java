package com.maliprestige.View.Interfaces;

import com.maliprestige.Presenter.WebHtml.WebHtmlClient;

public class WebHtmlView {
    public interface IWebHtml{
        public void hideHeader();
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void closeActivity();
        public void btnCloseVisibility(int visibility);
        public void loadWebView(WebHtmlClient webClient, String url);
        public void webViewVisibility(int visibility);
        public void messageError(String message);
    }

    // Presenter interface
    public interface IPresenter{
        public void onLoadWebHtmlFinished();
        public void onLoadWebHtmlFailure();
        public void onLoadWebHtmlHttpError();
    }
}
