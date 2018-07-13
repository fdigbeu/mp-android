package com.maliprestige.View.Activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maliprestige.Presenter.WebHtml.WebHtmlClient;
import com.maliprestige.Presenter.WebHtml.WebHtmlPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.WebHtmlView;

public class WebHtmlActivity extends AppCompatActivity implements WebHtmlView.IWebHtml {

    // Ref widgets
    private WebView webViewHtml;
    private ProgressBar progressBarHtml;
    private ImageView imageViewCancel;
    private TextView messageError;
    // Ref presenter
    private WebHtmlPresenter htmlPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_html);
        // Load presenter data
        htmlPresenter = new WebHtmlPresenter(this);
        htmlPresenter.loadWebHtmlData(WebHtmlActivity.this, this.getIntent());
    }

    @Override
    public void hideHeader() {
        getSupportActionBar().hide();
    }

    @Override
    public void initialize() {
        webViewHtml = findViewById(R.id.webview_webHtml);
        progressBarHtml = findViewById(R.id.progress_webHtml);
        imageViewCancel = findViewById(R.id.imageCancel_webHtml);
        messageError = findViewById(R.id.error_texView);
    }

    @Override
    public void events() {
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                htmlPresenter.closeActivity();
            }
        });
    }

    @Override
    public void progressVisibility(int visibility) {
        progressBarHtml.setVisibility(visibility);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void btnCloseVisibility(int visibility) {
        imageViewCancel.setVisibility(visibility);
    }

    @Override
    public void messageError(String message){
        messageError.setText(message);
    }

    @Override
    public void loadWebView(WebHtmlClient webClient, String url) {
        // Configure related browser settings
        webViewHtml.getSettings().setLoadsImagesAutomatically(true);
        webViewHtml.getSettings().setJavaScriptEnabled(true);
        webViewHtml.getSettings().setDomStorageEnabled(true);

        // Enable responsive layout
        webViewHtml.getSettings().setUseWideViewPort(true);

        // Display image from url : Android 5 and plus...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webViewHtml.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // Zoom out if the content width is greater than the width of the viewport
        webViewHtml.getSettings().setLoadWithOverviewMode(true);

        //allow manual zoom and get rid of pinch to zoom
        webViewHtml.getSettings().setSupportZoom(true);
        webViewHtml.getSettings().setBuiltInZoomControls(true);
        webViewHtml.getSettings().setDisplayZoomControls(false);

        webViewHtml.setBackgroundColor(0x00000000);
        webViewHtml.loadUrl(url);
        webViewHtml.setWebViewClient(webClient);
    }

    @Override
    public void webViewVisibility(int visibility) {
        webViewHtml.setVisibility(visibility);
    }
}
