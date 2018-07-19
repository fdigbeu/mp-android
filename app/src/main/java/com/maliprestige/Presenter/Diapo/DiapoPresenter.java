package com.maliprestige.Presenter.Diapo;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.DAOPanier;
import com.maliprestige.Model.DAOProduit;
import com.maliprestige.Model.DAOSearch;
import com.maliprestige.Model.JsonData;
import com.maliprestige.Model.Panier;
import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Search;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.DiapoView;

import java.util.ArrayList;

public class DiapoPresenter {

    private DiapoView.IDiapo iDiapo;
    private DiapoView.IPlaceholder iPlaceholder;
    private CountDownTimer downTimer;

    public DiapoPresenter(DiapoView.IDiapo iDiapo) {
        this.iDiapo = iDiapo;
    }


    public DiapoPresenter(DiapoView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    // Load diapo data
    public void loadDiapoData(Context context, Intent intent){
        if(context != null && intent != null && iDiapo != null){
            try {
                String produitId = intent.getStringExtra("produitId");
                String nomProduit = intent.getStringExtra("nomProduit");
                String produitImage1 = intent.getStringExtra("produitImage1");
                String produitImage2 = intent.getStringExtra("produitImage2");
                String produitImage3 = intent.getStringExtra("produitImage3");
                //--
                iDiapo.initialize();
                iDiapo.layoutPanierVisibility(View.GONE);
                iDiapo.newProduitVisibility(View.GONE);
                //--
                if(!nomProduit.isEmpty() && nomProduit != null){
                    iDiapo.changeDiapoTitle(nomProduit);
                    ArrayList<String> urlDiapos = new ArrayList<>();
                    if(produitImage1 != null && !produitImage1.isEmpty()){ urlDiapos.add(produitImage1); }
                    if(produitImage2 != null && !produitImage2.isEmpty()){ urlDiapos.add(produitImage2); }
                    if(produitImage3 != null && !produitImage3.isEmpty()){ urlDiapos.add(produitImage3); }
                    setNumberOfDiapoFinded(urlDiapos.size());
                    loadDataDiapoFinded(urlDiapos);
                }
                else{
                    iDiapo.closeActivity();
                }
                //--
                if(produitId == null){
                    DAOSearch daoSearch = new DAOSearch(context);
                    Search search = daoSearch.getInfoByNom(nomProduit);
                    if(search != null){
                        produitId = ""+search.getProduitId();
                    }
                }
                if(produitId != null){
                    DAOProduit daoProduit = new DAOProduit(context);
                    Produit produit = daoProduit.getInfoBy(Integer.parseInt(produitId));
                    if(produit != null) {
                        // Persist produit data
                        iDiapo.persistProduit(produit);
                        Log.i("TAG_PRODUIT", "PERSISTENCE_DU_PRODUIT");
                        //---
                        iDiapo.layoutPanierVisibility(View.VISIBLE);
                        iDiapo.newProduitVisibility(produit.isNouveaute() ? View.VISIBLE : View.GONE);
                        iDiapo.changeNewProduitValue(produit.isNouveaute() && produit.getPrixUnitaireGros()==0 ? "Nouveauté" : (produit.getPrixUnitaireGros() > 0 ? "Le prix est au kilo": ""));
                        //--
                        if(produit.isReduction()){
                            iDiapo.changePrixValue("€"+String.format("%.2f", produit.getPrixReductionTtc()));
                        }
                        else{
                            iDiapo.changePrixValue(produit.getPrixUnitaireGros() > 0 ? "€"+String.format("%.2f", produit.getPrixUnitaireGros()) : "€"+String.format("%.2f", produit.getPrixUnitaireTtc()));
                        }
                    }
                }
            }
            catch (Exception ex){
                Log.e("TAG_ERREUR", "DiapoPresenter->loadDiapoData() : "+ex.getMessage());
            }
        }
    }

    public void loadPlaceholderData(View view, int positionFrag){
        try {
            if(iPlaceholder != null) {
                Context context = view.getContext();
                iPlaceholder.initialize(view);
                iPlaceholder.events();
                DiapoView.IDiapo iDiapo = iPlaceholder.retrieveIDiapoInstance();
                if(iDiapo != null){
                    ArrayList<String> urlDiapos = iDiapo.retrievePersistDiapos();
                    if(urlDiapos.size() > positionFrag){
                        int width = HomePresenter.getScreenResolution(context).getWidth();
                        int height = (int)(width*1.14f);
                        iPlaceholder.loadDiapoData(urlDiapos.get(positionFrag), width, height);
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "DiapoPresenter->loadPlaceholderData() : "+ex.getMessage());
        }
    }

    // Metho to set number of slides finded
    public void setNumberOfDiapoFinded(int numberFinded){
        try {
            if(iDiapo != null){
                iDiapo.setNumberOfDiapoFinded(numberFinded);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "DiapoPresenter->setNumberOfDiapoFinded() : "+ex.getMessage());
        }
    }

    public void loadDataDiapoFinded(final ArrayList<String> urlDiapos){
        try {
            if(iDiapo != null){
                iDiapo.persistDiapos(urlDiapos);
                iDiapo.loadPlaceHolderFragment();
                iDiapo.events();
                downTimer = new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        iDiapo.feedDiapoPageNumber("1/"+urlDiapos.size());
                    }
                }.start();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "DiapoPresenter->loadDataDiapoFinded() : "+ex.getMessage());
        }
    }

    public void changeDiapoNumber(int currentViewPager, int totalViewPager){
        try {
            if(iDiapo != null) {
                iDiapo.feedDiapoPageNumber((currentViewPager + 1) + "/" + totalViewPager);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "DiapoPresenter->changeDiapoNumber() : "+ex.getMessage());
        }
    }

    public void closeActivity(){
        try {
            if(iDiapo != null) {
                iDiapo.closeActivity();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "DiapoPresenter->closeActivity() : "+ex.getMessage());
        }
    }

    public void cancelCountDownTimer(){
        try {
            if(downTimer != null){
                downTimer.cancel();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "DiapoPresenter->cancelCountDownTimer() : "+ex.getMessage());
        }
    }

    public void retrieveUserAction(View view){
        try {
            if(iDiapo != null && view != null){
                Produit produit = iDiapo.retrievePersistProduit();
                addProductToBasket(view, produit);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "DiapoPresenter->retrieveUserAction() : "+ex.getMessage());
        }
    }

    // Method to add product to the basket
    public void addProductToBasket(View view, Produit produit){
        try {
            if(iDiapo != null && produit != null){
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
            Log.e("TAG_ERROR", "DiapoPresenter-->addProductToBasket() : "+ex.getMessage());
        }
    }

}
