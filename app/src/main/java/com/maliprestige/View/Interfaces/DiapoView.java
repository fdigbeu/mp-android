package com.maliprestige.View.Interfaces;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Search;
import com.maliprestige.Model.Slide;

import java.util.ArrayList;
import java.util.List;

public class DiapoView {
    public interface IDiapo{
        public void initialize();
        public void events();
        public void loadPlaceHolderFragment();
        public void setIplaceholderReference(DiapoView.IPlaceholder iPlaceholder);
        public void closeActivity();
        public void persistDiapos(ArrayList<String> urlDiapos);
        public ArrayList<String> retrievePersistDiapos();
        public void changeDiapoTitle(String title);
        public void setNumberOfDiapoFinded(int number);
        public void feedDiapoPageNumber(String pageNumber);
        public void persistProduit(Produit produit);
        public Produit retrievePersistProduit();
        public void layoutPanierVisibility(int visibility);
        public void changePrixValue(String value);
        public void changeNewProduitValue(String value);
        public void newProduitVisibility(int visibility);
    }

    public interface IPlaceholder{
        public void initialize(View view);
        public void events();
        public void loadDiapoData(String urlImage, int width, int height);
        public DiapoView.IDiapo retrieveIDiapoInstance();
    }

    public interface IPresenter{
    }
}
