package com.maliprestige.View.Interfaces;

import android.content.Context;

import com.maliprestige.Model.Produit;

import java.util.ArrayList;
import java.util.List;

public class ExoticFragView {
    public interface IExoticFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void loadRecyclerViewData(List<Produit> produits, int numberColumns);
        public HomeView.IHome retrieveIHomeInstance();
    }

    // Presenter interface
    public interface IPresenter{
        public void onLoadProduitsFinished(Context context, ArrayList<Produit> produits);
    }
}
