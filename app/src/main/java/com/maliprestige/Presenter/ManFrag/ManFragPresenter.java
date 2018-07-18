package com.maliprestige.Presenter.ManFrag;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.maliprestige.Model.DAOPanier;
import com.maliprestige.Model.DAOProduit;
import com.maliprestige.Model.Panier;
import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Screen;
import com.maliprestige.Presenter.Home.GetAllProduits;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.ManFragView;

import java.util.ArrayList;

public class ManFragPresenter implements ManFragView.IPresenter{
    // Ref interface
    private ManFragView.IManFrag iManFrag;

    private GetAllProduits manAsyntask;

    public ManFragPresenter(ManFragView.IManFrag iManFrag) {
        this.iManFrag = iManFrag;
    }

    // Method to load man fragment data
    public void loadManFragData(final Context context){
        try {
            if(iManFrag != null && context != null){
                iManFrag.initialize();
                iManFrag.events();

                HomeView.IHome mIHome = iManFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                ArrayList<Produit> produits = homePresenter.retrievePersistProduits("homme");
                //--
                if(produits != null && produits.size() > 0){
                    iManFrag.loadRecyclerViewData(produits, 1);
                    homePresenter.persistProduits("homme", produits);
                    //Log.i("TAG_DATA", "loadManFragData(RETRIEVE_PERSIST_DATA)");
                }
                else{
                    // Load all data produits
                    if(HomePresenter.isMobileConnected(context)){
                        iManFrag.progressVisibility(View.VISIBLE);
                        //Log.i("TAG_DATA", "loadManFragData(CALL_ASYNTASK)");
                        manAsyntask = new GetAllProduits();
                        manAsyntask.setManPresenter(this);
                        manAsyntask.initialize(context, "homme", "500");
                        manAsyntask.execute();
                    }
                    else{
                        //Log.i("TAG_DATA", "loadManFragData(DATABASE_OK)");
                        // Retrieve produits from database
                        DAOProduit daoProduit = new DAOProduit(context);
                        produits = daoProduit.getAllBy("homme");
                        if(produits != null && produits.size() > 0) {
                            iManFrag.loadRecyclerViewData(produits, 1);
                            if(mIHome != null) {
                                homePresenter.persistProduits("homme", produits);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ManFragPresenter-->loadManFragData() : "+ex.getMessage());

        }
    }

    // Screen resolution
    public static Screen getScreenResolution(Context context){
        return HomePresenter.getScreenResolution(context);
    }

    @Override
    public void onLoadProduitsFinished(Context context, ArrayList<Produit> produits) {
        try {
            iManFrag.progressVisibility(View.GONE);
            // Save produit in database
            if(produits != null && produits.size() > 0){
                iManFrag.loadRecyclerViewData(produits, 1);
                //--
                HomeView.IHome mIHome = iManFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                if(mIHome != null) {
                    homePresenter.persistProduits("homme", produits);
                }
                //--
                DAOProduit daoProduit = new DAOProduit(context);
                daoProduit.deleteBy("homme");
                for(int i=0; i<produits.size(); i++){
                    daoProduit = new DAOProduit(context);
                    daoProduit.add(produits.get(i));
                }
            }
            else{
                // Retrieve produits from database
                DAOProduit daoProduit = new DAOProduit(context);
                produits = daoProduit.getAllBy("homme");
                HomeView.IHome mIHome = iManFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                if(produits != null && produits.size() > 0) {
                    iManFrag.loadRecyclerViewData(produits, 1);
                    if(mIHome != null) {
                        homePresenter.persistProduits("homme", produits);
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ManFragPresenter-->onLoadProduitsFinished() : "+ex.getMessage());
        }
    }

    // Method to show product detail
    public void showProductDetail(Produit produit){
        try {

        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ManFragPresenter-->showProductDetail() : "+ex.getMessage());
        }
    }

    // Method to add product to the basket
    public void addProductToBasket(View view, Produit produit){
        try {
            if(iManFrag != null && produit != null){
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
            Log.e("TAG_ERROR", "ManFragPresenter-->addProductToBasket() : "+ex.getMessage());
        }
    }


    public void cancelAsyntask(){
        if(manAsyntask != null){
            manAsyntask.cancel(true);
        }
    }
}
