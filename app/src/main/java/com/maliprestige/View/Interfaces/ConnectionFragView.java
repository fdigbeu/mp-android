package com.maliprestige.View.Interfaces;

import android.content.Context;
import android.view.View;

public class ConnectionFragView {
    public interface IConnectionFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void champEmailObligatoire();
        public void champPasswordObligatoire();
        public void displaySnackBar(View view, String message);
    }

    // Presenter interface
    public interface IPresenter{
        public void onSendConnectionFormFinished(Context context, String returnCode);
    }
}
