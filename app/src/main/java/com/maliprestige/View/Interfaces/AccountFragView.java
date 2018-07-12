package com.maliprestige.View.Interfaces;

import com.maliprestige.Model.Client;

public class AccountFragView {
    public interface IAccountFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void loadAdressesLivraisons(String[] livraisons);
        public void loadAdressesFacturations(String[] facturations);
        public void loadIdentiteData(Client client);
    }

    // Presenter interface
    public interface IPresenter{}
}
