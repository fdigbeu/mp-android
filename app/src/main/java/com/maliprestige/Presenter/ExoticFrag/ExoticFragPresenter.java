package com.maliprestige.Presenter.ExoticFrag;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.maliprestige.Model.DAOPanier;
import com.maliprestige.Model.DAOProduit;
import com.maliprestige.Model.DAOSlide;
import com.maliprestige.Model.Panier;
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
            if(iExoticFrag != null){
                HomeView.IHome mIHome = iExoticFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                if(produit != null){
                    homePresenter.launchProduitDetail(produit);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ExoticFragPresenter-->showProductDetail() : "+ex.getMessage());
        }
    }

    // Method to add product to the basket
    public void addProductToBasket(View view, Produit produit){
        try {
            if(iExoticFrag != null && produit != null){
                Context context = view.getContext();
                String clientToken = HomePresenter.retrieveClientToken(context);
                int produitId = produit.getProduitId();
                float prixProduit = 0f;
                int quantite = 1;
                if(produit.isReduction()){ prixProduit = produit.getPrixReductionTtc(); }
                else{ prixProduit = produit.getPrixUnitaireGros() > 0 ? produit.getPrixUnitaireGros() : produit.getPrixUnitaireTtc(); }
                //--
                float prixQuantite = quantite*prixProduit;
                //--
                Panier panier = new Panier();
                if(clientToken != null && !clientToken.isEmpty()){
                    DAOPanier daoPanier = new DAOPanier(context);
                    Panier existDejaDansPanier = daoPanier.getInfoBy(clientToken, produitId);
                    // If already exists
                    if(existDejaDansPanier != null){
                        quantite = existDejaDansPanier.getQuantite()+1;
                        prixQuantite = quantite*prixProduit;
                        daoPanier = new DAOPanier(context);
                        daoPanier.modify(clientToken, produitId, quantite, prixQuantite);
                    }
                    else {
                        panier.setToken(clientToken);
                        panier.setQuantite(quantite);
                        panier.setPrixQuantite(prixQuantite);
                        panier.setProduitId(produitId);
                        panier.setImageProduit(produit.getImage1());
                        panier.setDelaiJourMin(produit.getDelaiJourMin());
                        panier.setDelaiJourMax(produit.getDelaiJourMax());
                        panier.setNomProduit(produit.getNom());
                        daoPanier = new DAOPanier(context);
                        daoPanier.add(panier);
                    }
                    //--
                    HomePresenter.messageSnackBar(view, context.getResources().getString(R.string.lb_produit_ajout_succes));
                }
                else{
                    HomePresenter.messageSnackBar(view, "Erreur ! aucun token trouvé.");
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
