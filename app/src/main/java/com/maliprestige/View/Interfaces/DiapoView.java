package com.maliprestige.View.Interfaces;

import android.content.Context;
import android.content.Intent;
import android.view.View;

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
        public void changeDiapoTitel(String title);
        public void setNumberOfDiapoFinded(int number);
        public void feedDiapoPageNumber(String pageNumber);
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
