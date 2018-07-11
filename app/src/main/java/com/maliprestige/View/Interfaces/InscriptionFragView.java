package com.maliprestige.View.Interfaces;

import android.content.Context;
import android.view.View;

public class InscriptionFragView {
    public interface IInscriptionFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void loadCiviliteData(String[] civilites);
        public void champNomObligatoire();
        public void champEmailObligatoire();
        public void champPasswordObligatoire();
        public void champConfirmPasswordObligatoire();
        public void displaySnackBar(View view, String message);
    }

    // Presenter interface
    public interface IPresenter{
        public void onSendInscriptionFormFinished(Context context, String returnCode);
    }
}
