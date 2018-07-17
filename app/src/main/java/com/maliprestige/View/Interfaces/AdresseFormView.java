package com.maliprestige.View.Interfaces;

import android.content.Context;
import android.view.View;

public class AdresseFormView {
    public interface IAdresseForm{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void champNomObligatoire();
        public void champPrenomObligatoire();
        public void champAdresseObligatoire();
        public void champCodePostalObligatoire();
        public void champVilleObligatoire();
        public void champPaysObligatoire();
        public void displaySnackBar(View view, String message);
        public void enableDisableButton(boolean enable);
        public void loadTitleFromData(String title);
        public void closeActivity();
        public String retrieveTypeAdresse();
    }

    // Presenter interface
    public interface IPresenter{
        public void onSendAdresseFormFinished(Context context, String returnCode);
    }
}
