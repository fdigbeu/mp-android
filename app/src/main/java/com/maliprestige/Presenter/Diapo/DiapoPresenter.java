package com.maliprestige.Presenter.Diapo;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.JsonData;
import com.maliprestige.Model.Search;
import com.maliprestige.Presenter.Home.HomePresenter;
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
                //--
                /*Log.i("TAG_PRODUIT", "produitId = "+produitId);
                Log.i("TAG_PRODUIT", "nomProduit = "+nomProduit);
                Log.i("TAG_PRODUIT", "produitImage1 = "+produitImage1);
                Log.i("TAG_PRODUIT", "produitImage2 = "+produitImage2);
                Log.i("TAG_PRODUIT", "produitImage3 = "+produitImage3);*/
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
}
