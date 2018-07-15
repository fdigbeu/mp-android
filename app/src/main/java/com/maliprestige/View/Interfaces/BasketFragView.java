package com.maliprestige.View.Interfaces;

import android.content.Context;

import com.maliprestige.Model.Panier;
import com.maliprestige.Model.Produit;

import java.util.ArrayList;
import java.util.List;

public class BasketFragView {
    public interface IBasketFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void messageVisibility(int visibility);
        public void messagePanier(String message);
        public void validerPanierVisibility(int visibility);
        public void loadRecyclerViewData(ArrayList<Panier> paniers, int numberColumns);
        public HomeView.IHome retrieveIHomeInstance();
        public void changeSubTotal(String subtotal);
    }

    // Presenter interface
    public interface IPresenter{
        public void onLoadPaniersFinished(Context context, ArrayList<Panier> paniers);
    }
}
