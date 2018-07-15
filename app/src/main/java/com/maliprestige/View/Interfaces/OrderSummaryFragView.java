package com.maliprestige.View.Interfaces;

import android.content.Context;

import com.maliprestige.Model.Panier;

import java.util.ArrayList;

public class OrderSummaryFragView {
    public interface IOrderSummaryFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public HomeView.IHome retrieveIHomeInstance();
        public void changeSubTotal(String subtotal);
        public void loadAdressesLivraisons(String[] livraisons);
        public void loadAdressesFacturations(String[] facturations);
        public void messageVisibility(int visibility);
        public void messagePanier(String message);
        public void changeDateLivraison(String message);
        public void containerVisibility(int visibility);
    }

    // Presenter interface
    public interface IPresenter{}
}
