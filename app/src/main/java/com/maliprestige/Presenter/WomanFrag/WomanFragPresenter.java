package com.maliprestige.Presenter.WomanFrag;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.DAOProduit;
import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Screen;
import com.maliprestige.Presenter.Home.GetAllProduits;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.WomanFragView;

import java.util.ArrayList;

public class WomanFragPresenter implements WomanFragView.IPresenter{
    // Ref interface
    private WomanFragView.IWomanFrag iWomanFrag;

    private GetAllProduits womanAsyntask;

    public WomanFragPresenter(WomanFragView.IWomanFrag iWomanFrag) {
        this.iWomanFrag = iWomanFrag;
    }

    // Method to load woman fragment data
    public void loadWomanFragData(Context context){
        try {
            if(iWomanFrag != null && context != null){
                iWomanFrag.initialize();
                iWomanFrag.events();

                final HomeView.IHome mIHome = iWomanFrag.retrieveIHomeInstance();
                final HomePresenter homePresenter = new HomePresenter(mIHome);
                ArrayList<Produit> produits = homePresenter.retrievePersistProduits("femme");
                //--
                if(produits != null && produits.size() > 0){
                    iWomanFrag.loadRecyclerViewData(produits, 1);
                    homePresenter.persistProduits("femme", produits);
                    Log.i("TAG_DATA", "loadWomanFragData(RETRIEVE_PERSIST_DATA)");
                }
                else{
                    // Load all data produits
                    if(HomePresenter.isMobileConnected(context)){
                        iWomanFrag.progressVisibility(View.VISIBLE);
                        //Log.i("TAG_DATA", "loadWomanFragData(CALL_ASYNTASK)");
                        womanAsyntask = new GetAllProduits();
                        womanAsyntask.setWomanPresenter(this);
                        womanAsyntask.initialize(context, "femme", "500");
                        womanAsyntask.execute();
                    }
                    else{
                        //Log.i("TAG_DATA", "loadWomanFragData(DATABASE_OK)");
                        // Retrieve produits from database
                        DAOProduit daoProduit = new DAOProduit(context);
                        produits = daoProduit.getAllBy("femme");
                        if(produits != null && produits.size() > 0) {
                            iWomanFrag.loadRecyclerViewData(produits, 1);
                            if(mIHome != null) {
                                homePresenter.persistProduits("femme", produits);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "WomanFragPresenter-->loadWomanFragData() : "+ex.getMessage());

        }
    }

    // Screen resolution
    public static Screen getScreenResolution(Context context){
        return HomePresenter.getScreenResolution(context);
    }

    @Override
    public void onLoadProduitsFinished(Context context, ArrayList<Produit> produits) {
        try {
            iWomanFrag.progressVisibility(View.GONE);
            // Save produit in database
            if(produits != null && produits.size() > 0){
                //Log.i("TAG_DATA", "onLoadProduitsFinished(WOMAN_FRAG_PRESENTER : TOTAL_PRODUITS = "+produits.size()+")");
                iWomanFrag.loadRecyclerViewData(produits, 1);
                //--
                HomeView.IHome mIHome = iWomanFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                if(mIHome != null) {
                    homePresenter.persistProduits("femme", produits);
                }
                //--
                DAOProduit daoProduit = new DAOProduit(context);
                daoProduit.deleteBy("femme");
                for(int i=0; i<produits.size(); i++){
                    daoProduit = new DAOProduit(context);
                    daoProduit.add(produits.get(i));
                }
            }
            else{
                //Log.i("TAG_DATA", "loadWomanFragData(DATABASE_OK)");
                // Retrieve produits from database
                DAOProduit daoProduit = new DAOProduit(context);
                produits = daoProduit.getAllBy("femme");
                if(produits != null && produits.size() > 0) {
                    iWomanFrag.loadRecyclerViewData(produits, 1);
                    HomeView.IHome mIHome = iWomanFrag.retrieveIHomeInstance();
                    HomePresenter homePresenter = new HomePresenter(mIHome);
                    if(mIHome != null) {
                        homePresenter.persistProduits("femme", produits);
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "WomanFragPresenter-->onLoadProduitsFinished() : "+ex.getMessage());

        }
    }

    // Method to show product detail
    public void showProductDetail(Produit produit){
        try {

        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "WomanFragPresenter-->showProductDetail() : "+ex.getMessage());
        }
    }

    // Method to add product to the basket
    public void addProductToBasket(Context context, Produit produit){
        try {
            if(iWomanFrag != null && produit != null){
                String clientToken = HomePresenter.retrieveClientToken(context);
                // If user is not connected
                if(clientToken==null || clientToken.isEmpty() || clientToken.equalsIgnoreCase("YES") || clientToken.equalsIgnoreCase("NO")){
                    HomeView.IHome mIHome = iWomanFrag.retrieveIHomeInstance();
                    HomePresenter homePresenter = new HomePresenter(mIHome);
                    homePresenter.showViewPager(context.getResources().getString(R.string.lb_connexion));
                }
                else{

                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "WomanFragPresenter-->showProductDetail() : "+ex.getMessage());
        }
    }
}
