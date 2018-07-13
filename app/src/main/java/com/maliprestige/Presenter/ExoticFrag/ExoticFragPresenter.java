package com.maliprestige.Presenter.ExoticFrag;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.DAOProduit;
import com.maliprestige.Model.DAOSlide;
import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Screen;
import com.maliprestige.Presenter.Home.GetAllProduits;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.ExoticFragView;
import com.maliprestige.View.Interfaces.HomeView;

import java.util.ArrayList;

public class ExoticFragPresenter implements ExoticFragView.IPresenter{
    // Ref interface
    private ExoticFragView.IExoticFrag iExoticFrag;

    private GetAllProduits exoticAsyntask;

    public ExoticFragPresenter(ExoticFragView.IExoticFrag iExoticFrag) {
        this.iExoticFrag = iExoticFrag;
    }

    // Method to load exotic fragment data
    public void loadExoticFragData(Context context){
        try {
            if(iExoticFrag != null && context != null){
                iExoticFrag.initialize();
                iExoticFrag.events();

                final HomeView.IHome mIHome = iExoticFrag.retrieveIHomeInstance();
                final HomePresenter homePresenter = new HomePresenter(mIHome);
                ArrayList<Produit> produits = homePresenter.retrievePersistProduits("exotique");
                //--
                if(produits != null && produits.size() > 0){
                    iExoticFrag.loadRecyclerViewData(produits, 1);
                    homePresenter.persistProduits("exotique", produits);
                    Log.i("TAG_DATA", "loadExoticFragData(RETRIEVE_PERSIST_DATA)");
                }
                else{
                    // Load all data produits
                    if(HomePresenter.isMobileConnected(context)){
                        iExoticFrag.progressVisibility(View.VISIBLE);
                        exoticAsyntask = new GetAllProduits();
                        exoticAsyntask.setExoticPresenter(this);
                        exoticAsyntask.initialize(context, "exotique", "500");
                        exoticAsyntask.execute();
                    }
                    else{
                        //Log.i("TAG_DATA", "loadExoticFragData(DATABASE_OK)");
                        // Retrieve produits from database
                        DAOProduit daoProduit = new DAOProduit(context);
                        produits = daoProduit.getAllBy("exotique");
                        if(produits != null && produits.size() > 0) {
                            iExoticFrag.loadRecyclerViewData(produits, 1);
                            if(mIHome != null) {
                                homePresenter.persistProduits("exotique", produits);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ExoticFragPresenter-->loadExoticFragData() : "+ex.getMessage());

        }
    }

    // Screen resolution
    public static Screen getScreenResolution(Context context){
        return HomePresenter.getScreenResolution(context);
    }

    @Override
    public void onLoadProduitsFinished(Context context, ArrayList<Produit> produits) {
        try {
            iExoticFrag.progressVisibility(View.GONE);
            // Save produit in database
            if(produits != null && produits.size() > 0){
                //Log.i("TAG_DATA", "onLoadProduitsFinished(EXOTIC_FRAG_PRESENTER : TOTAL_PRODUITS = "+produits.size()+")");
                iExoticFrag.loadRecyclerViewData(produits, 1);
                //--
                HomeView.IHome mIHome = iExoticFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                if(mIHome != null) {
                    homePresenter.persistProduits("exotique", produits);
                }
                //--
                DAOProduit daoProduit = new DAOProduit(context);
                daoProduit.deleteBy("exotique");
                for(int i=0; i<produits.size(); i++){
                    daoProduit = new DAOProduit(context);
                    daoProduit.add(produits.get(i));
                }
            }
            else{
                //Log.i("TAG_DATA", "loadExoticFragData(DATABASE_OK)");
                // Retrieve produits from database
                DAOProduit daoProduit = new DAOProduit(context);
                produits = daoProduit.getAllBy("exotique");
                if(produits != null && produits.size() > 0) {
                    iExoticFrag.loadRecyclerViewData(produits, 1);
                    HomeView.IHome mIHome = iExoticFrag.retrieveIHomeInstance();
                    HomePresenter homePresenter = new HomePresenter(mIHome);
                    if(mIHome != null) {
                        homePresenter.persistProduits("exotique", produits);
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ExoticFragPresenter-->onLoadProduitsFinished() : "+ex.getMessage());

        }
    }

    // Method to show product detail
    public void showProductDetail(Produit produit){
        try {

        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ExoticFragPresenter-->showProductDetail() : "+ex.getMessage());
        }
    }

    // Method to add product to the basket
    public void addProductToBasket(Context context, Produit produit){
        try {
            if(iExoticFrag != null && produit != null){
                String clientToken = HomePresenter.retrieveClientToken(context);
                // If user is not connected
                if(clientToken==null || clientToken.isEmpty() || clientToken.length() <= 15){
                    HomeView.IHome mIHome = iExoticFrag.retrieveIHomeInstance();
                    HomePresenter homePresenter = new HomePresenter(mIHome);
                    homePresenter.showViewPager(context.getResources().getString(R.string.lb_connexion));
                }
                else{

                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ExoticFragPresenter-->showProductDetail() : "+ex.getMessage());
        }
    }


    public void cancelAsytask(){
        if(exoticAsyntask != null){
            exoticAsyntask.cancel(true);
        }
    }
}
