package com.maliprestige.View.Interfaces;

import android.content.Context;
import android.view.View;

public class AccountFragView {
    public interface IAccountFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void loadAdressesLivraisons(String[] livraisons);
        public void loadAdressesFacturations(String[] facturations);
    }

    // Presenter interface
    public interface IPresenter{}
}
