package com.maliprestige.Presenter.ConnectionFrag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.maliprestige.R;
import com.maliprestige.View.Interfaces.ConnectionFragView;

public class ConnectionFragPresenter implements ConnectionFragView.IPresenter {

    private ConnectionFragView.IConnectionFrag iConnectionFrag;

    public ConnectionFragPresenter(ConnectionFragView.IConnectionFrag iConnectionFrag) {
        this.iConnectionFrag = iConnectionFrag;
    }

    // Method to load connection frag data
    public void loadConnectionFragData(Context context){
        try {
            if(iConnectionFrag != null && context != null){
                iConnectionFrag.initialize();
                iConnectionFrag.events();
                iConnectionFrag.progressVisibility(View.GONE);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ConnectionFragPresenter-->loadConnectionFragData() : "+ex.getMessage());
        }
    }

    // Method to load external page web
    public void loadPasswordOubliePageWeb(Context context){
        try {
            String url = context.getResources().getString(R.string.mp_json_hote_production)+context.getResources().getString(R.string.mp_password_oublie_link);
            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ConnectionFragPresenter-->loadPasswordOubliePageWeb() : "+ex.getMessage());
        }
    }

    @Override
    public void onSendConnectionFormFinished(Context context, String returnCode) {

    }
}
