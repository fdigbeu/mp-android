package com.maliprestige.View.Interfaces;

import android.content.Context;

import com.maliprestige.Model.Slide;

import java.util.ArrayList;
import java.util.List;

public class HomeFragView {
    public interface IHomeFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void loadRecyclerViewData(List<Slide> slides, int numberColumns);
        public HomeView.IHome retrieveIHomeInstance();
    }

    // Presenter interface
    public interface IPresenter{
        public void onLoadSlidesFinished(Context context, ArrayList<Slide> slides);
    }
}
