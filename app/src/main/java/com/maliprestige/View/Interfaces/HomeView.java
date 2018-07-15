package com.maliprestige.View.Interfaces;

import android.content.Context;

import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Slide;

import java.util.ArrayList;

public class HomeView {
    public interface IHome{
        public void initialize();
        public void events();
        public void notifyUserIsConnected(boolean response, String[] userInfos);
        public void closeActivity();
        public void changeHomeView(int position, String title);
        public void checkedNavigationView(int menuItemId);
        public void persistSlides(ArrayList<Slide> slides);
        public ArrayList<Slide> retrievePersistSlides();
        public void persistProduits(String key, ArrayList<Produit> produits);
        public ArrayList<Produit> retrievePersistProduits(String key);
        public void launchWebHtmlActivity(String url);
        public void openOrCloseMenuDrawer();
        public void progressBarVisibility(int visibility);
        public void persistNumberViewPager(int numberViewPager);
        public int retrieveNumberViewPager();
    }

    public interface IPresenter{
        public void onLoadProduitsFinished(Context context, ArrayList<Produit> produits);
        public void onUserDeconnectionFinished(Context context, String returnCode);
    }
}
