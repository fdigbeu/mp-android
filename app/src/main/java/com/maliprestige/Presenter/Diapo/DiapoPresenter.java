package com.maliprestige.Presenter.Diapo;

import android.content.Context;
import android.content.Intent;
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

    public DiapoPresenter(DiapoView.IDiapo iDiapo) {
        this.iDiapo = iDiapo;
    }


    public DiapoPresenter(DiapoView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    // Load diapo data
    public void loadDiapoData(Context context, Intent intent){
        if(context != null && intent != null){
            try {
                String nomProduit = intent.getStringExtra("nomProduit");
                String produitId = intent.getStringExtra("produitId");
                Search search = null;
                String jsonSearches = HomePresenter.retrieveAutoCompleteData(context);
                JsonData jsonData = new JsonData(jsonSearches);
                ArrayList<Search> searches = jsonData.getSearchDataFromJson();
                //--
                if(searches != null && searches.size() > 0){
                    for (int i=0; i<searches.size(); i++){
                        // Search by name
                        if(nomProduit != null && nomProduit.length() >= 2){
                            if(nomProduit.trim().equalsIgnoreCase(searches.get(i).getNomProduit().trim())){ search = searches.get(i); break; }
                        }
                        // Search by id
                        if(produitId != null && Integer.parseInt(produitId) > 0){
                            if(Integer.parseInt(produitId) == searches.get(i).getProduitId()){ search = searches.get(i); break; }
                        }
                    }
                }
                //--
                if(search != null){
                    iDiapo.changeDiapoTitel(search.getNomProduit());
                    ArrayList<String> urlDiapos = new ArrayList<>();
                    if(search.getImage1() != null && !search.getImage1().isEmpty()){ urlDiapos.add(search.getImage1()); }
                    if(search.getImage2() != null && !search.getImage2().isEmpty()){ urlDiapos.add(search.getImage2()); }
                    if(search.getImage3() != null && !search.getImage3().isEmpty()){ urlDiapos.add(search.getImage3()); }
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
                        int height = (int)(width*0.87f);
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

    public void loadDataDiapoFinded(ArrayList<String> urlDiapos){
        try {
            if(iDiapo != null){
                iDiapo.persistDiapos(urlDiapos);
                iDiapo.loadPlaceHolderFragment();
                iDiapo.events();
                iDiapo.feedDiapoPageNumber("1/"+urlDiapos.size());
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "DiapoPresenter->loadDataDiapoFinded() : "+ex.getMessage());
        }
    }

    public void changeDiapoNumber(int currentViewPager, int totalViewPager){
        try {
            iDiapo.feedDiapoPageNumber((currentViewPager+1)+"/"+totalViewPager);
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "DiapoPresenter->changeDiapoNumber() : "+ex.getMessage());
        }
    }
}
