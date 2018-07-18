package com.maliprestige.View.Interfaces;

import android.content.Context;

import com.maliprestige.Model.Commande;
import com.maliprestige.Model.CommandeProduit;

import java.util.ArrayList;

public class OrderFragView {
    public interface IOrderFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void messageVisibility(int visibility);
        public void messageOrder(String message);
        public void loadRecyclerViewData(ArrayList<Commande> commandes, int numberColumns);
        public HomeView.IHome retrieveIHomeInstance();
    }

    // Presenter interface
    public interface IPresenter{
        public void onLoadCommandesFinished(Context context, ArrayList<Commande> commandes);
    }
}
