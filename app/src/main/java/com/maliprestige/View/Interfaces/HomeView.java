package com.maliprestige.View.Interfaces;

import android.content.Context;

import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Slide;

import java.util.ArrayList;

public class HomeView {
    public interface IHome{
        public void initialize();
        public void events();
        public void notifyUserIsConnected(boolean response);
        public void closeActivity();
        public void changeHomeView(int position, String title);
        public void checkedNavigationView(int menuItemId);
        public void persistSlides(ArrayList<Slide> slides);
        public ArrayList<Slide> retrievePersistSlides();
        public void persistProduits(String key, ArrayList<Produit> produits);
        public ArrayList<Produit> retrievePersistProduits(String key);
    }

    public interface IPresenter{
        public void onLoadProduitsFinished(Context context, ArrayList<Produit> produits);
    }
}
