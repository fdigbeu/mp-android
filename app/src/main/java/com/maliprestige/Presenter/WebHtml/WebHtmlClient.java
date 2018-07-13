package com.maliprestige.Presenter.WebHtml;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maliprestige.View.Interfaces.WebHtmlView;

public class WebHtmlClient extends WebViewClient {

    private WebHtmlView.IPresenter iPresenter;


    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(Uri.parse(url).getHost().endsWith("maliprestige.com")
                || Uri.parse(url).getHost().endsWith("recette.maliprestige.com")) {
            return false;
        }
        //--
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url=request.getUrl().toString();
        if(Uri.parse(url).getHost().endsWith("maliprestige.com")
                || Uri.parse(url).getHost().endsWith("recette.maliprestige.com")) {
            return false;
        }
        //--
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:(function() {" +
                "var head = document.getElementsByClassName('header')[0];"
                + "head.parentNode.removeChild(head);"

                +"var headbot = document.getElementsByClassName('header-bot')[0];"
                + "headbot.parentNode.removeChild(headbot);"

                +"var bantop = document.getElementsByClassName('ban-top')[0];"
                + "bantop.parentNode.removeChild(bantop);"

                +"var salew3ls = document.getElementsByClassName('sale-w3ls')[0];"
                + "salew3ls.parentNode.removeChild(salew3ls);"

                +"var coupon = document.getElementsByClassName('coupons')[0];"
                + "coupon.parentNode.removeChild(coupon);"

                + "var footer = document.getElementsByClassName('footer')[0];"
                + "footer.parentNode.removeChild(footer);" +
                "})()");
        super.onPageFinished(view, url);
        iPresenter.onLoadWebHtmlFinished();
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        iPresenter.onLoadWebHtmlFailure();
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        iPresenter.onLoadWebHtmlHttpError();
    }

    public void setiPresenter(WebHtmlView.IPresenter iPresenter) {
        this.iPresenter = iPresenter;
    }
}
