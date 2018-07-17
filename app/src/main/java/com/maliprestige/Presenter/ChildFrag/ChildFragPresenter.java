package com.maliprestige.Presenter.ChildFrag;

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
import com.maliprestige.View.Interfaces.ChildFragView;
import com.maliprestige.View.Interfaces.HomeView;

import java.util.ArrayList;

public class ChildFragPresenter implements ChildFragView.IPresenter{

    // Ref interface
    private ChildFragView.IChildFrag iChildFrag;

    private GetAllProduits childAsyntask;

    public ChildFragPresenter(ChildFragView.IChildFrag iChildFrag) {
        this.iChildFrag = iChildFrag;
    }

    // Method to load child fragment data
    public void loadChildFragData(Context context){
        try {
            if(iChildFrag != null && context != null){
                iChildFrag.initialize();
                iChildFrag.events();

                final HomeView.IHome mIHome = iChildFrag.retrieveIHomeInstance();
                final HomePresenter homePresenter = new HomePresenter(mIHome);
                ArrayList<Produit> produits = homePresenter.retrievePersistProduits("enfant");
                //--
                if(produits != null && produits.size() > 0){
                    iChildFrag.loadRecyclerViewData(produits, 1);
                    homePresenter.persistProduits("enfant", produits);
                    Log.i("TAG_DATA", "loadChildFragData(RETRIEVE_PERSIST_DATA)");
                }
                else{
                    // Load all data produits
                    if(HomePresenter.isMobileConnected(context)){
                        iChildFrag.progressVisibility(View.VISIBLE);
                        //Log.i("TAG_DATA", "loadChildFragData(CALL_ASYNTASK)");
                        childAsyntask = new GetAllProduits();
                        childAsyntask.setChildPresenter(this);
                        childAsyntask.initialize(context, "enfant", "500");
                        childAsyntask.execute();
                    }
                    else{
                        //Log.i("TAG_DATA", "loadChildFragData(DATABASE_OK)");
                        // Retrieve produits from database
                        DAOProduit daoProduit = new DAOProduit(context);
                        produits = daoProduit.getAllBy("enfant");
                        if(produits != null && produits.size() > 0) {
                            iChildFrag.loadRecyclerViewData(produits, 1);
                            if(mIHome != null) {
                                homePresenter.persistProduits("enfant", produits);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ChildFragPresenter-->loadChildFragData() : "+ex.getMessage());
        }
    }

    // Screen resolution
    public static Screen getScreenResolution(Context context){
        return HomePresenter.getScreenResolution(context);
    }

    @Override
    public void onLoadProduitsFinished(Context context, ArrayList<Produit> produits) {
        try {
            iChildFrag.progressVisibility(View.GONE);
            // Save produit in database
            if(produits != null && produits.size() > 0){
                //Log.i("TAG_DATA", "onLoadProduitsFinished(CHILD_FRAG_PRESENTER : TOTAL_PRODUITS = "+produits.size()+")");
                iChildFrag.loadRecyclerViewData(produits, 1);
                //--
                HomeView.IHome mIHome = iChildFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                if(mIHome != null) {
                    homePresenter.persistProduits("enfant", produits);
                }
                //--
                DAOProduit daoProduit = new DAOProduit(context);
                daoProduit.deleteBy("enfant");
                for(int i=0; i<produits.size(); i++){
                    daoProduit = new DAOProduit(context);
                    daoProduit.add(produits.get(i));
                }
            }
            else{
                //Log.i("TAG_DATA", "loadChildFragData(DATABASE_OK)");
                // Retrieve produits from database
                DAOProduit daoProduit = new DAOProduit(context);
                produits = daoProduit.getAllBy("enfant");
                if(produits != null && produits.size() > 0) {
                    iChildFrag.loadRecyclerViewData(produits, 1);
                    HomeView.IHome mIHome = iChildFrag.retrieveIHomeInstance();
                    HomePresenter homePresenter = new HomePresenter(mIHome);
                    if(mIHome != null) {
                        homePresenter.persistProduits("enfant", produits);
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ChildFragPresenter-->onLoadProduitsFinished() : "+ex.getMessage());
        }
    }

    // Method to show product detail
    public void showProductDetail(Produit produit){
        try {

        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ChildFragPresenter-->showProductDetail() : "+ex.getMessage());
        }
    }

    // Method to add product to the basket
    public void addProductToBasket(View view, Produit produit){
        try {
            if(iChildFrag != null && produit != null){
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
            Log.e("TAG_ERROR", "ChildFragPresenter-->showProductDetail() : "+ex.getMessage());
        }
    }

    public void cancelAsytask(){
        if(childAsyntask != null){
            childAsyntask.cancel(true);
        }
    }
}
