package com.maliprestige.Presenter.HomeFag;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.DAOSlide;
import com.maliprestige.Model.Screen;
import com.maliprestige.Model.Slide;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.View.Interfaces.HomeFragView;
import com.maliprestige.View.Interfaces.HomeView;
import java.util.ArrayList;

public class HomeFragPresenter implements HomeFragView.IPresenter{

    // Ref interface
    private HomeFragView.IHomeFrag iHomeFrag;

    private GetAllSlides getAllSlides;

    // Construtor
    public HomeFragPresenter(HomeFragView.IHomeFrag iHomeFrag) {
        this.iHomeFrag = iHomeFrag;
    }

    // Method to load home fragment data
    public void loadHomeFragData(final Context context){
        try {
            if(iHomeFrag != null && context != null){
                iHomeFrag.initialize();
                iHomeFrag.events();

                HomeView.IHome mIHome = iHomeFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                ArrayList<Slide> slides = homePresenter.retrievePersistSlides();
                if(slides != null){
                    iHomeFrag.loadRecyclerViewData(slides, 1);
                    Log.i("TAG_DATA", "loadHomeFragData(RETRIEVE_PERSISTANCE_DATA)");
                }
                else{
                    if(HomePresenter.isMobileConnected(context)){
                        Log.i("TAG_DATA", "loadHomeFragData(CALL_ASYNTASK)");
                        iHomeFrag.progressVisibility(View.VISIBLE);
                        getAllSlides = new GetAllSlides();
                        getAllSlides.setHomeFragPresenter(this);
                        getAllSlides.initialize(context);
                        getAllSlides.execute();
                    }
                    else{
                        //Log.i("TAG_DATA", "loadHomeFragData(DATABASE_OK)");
                        // Retrieve slides from database
                        DAOSlide daoSlide = new DAOSlide(context);
                        slides = daoSlide.getAll();
                        if(slides != null && slides.size() > 0) {
                            iHomeFrag.loadRecyclerViewData(slides, 1);
                            if(mIHome != null) {
                                homePresenter.persistSlides(slides);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomeFragPresenter-->loadHomeFragData() : "+ex.getMessage());
        }
    }

    // Screen resolution
    public static Screen getScreenResolution(Context context){
        return HomePresenter.getScreenResolution(context);
    }

    @Override
    public void onLoadSlidesFinished(Context context, ArrayList<Slide> slides) {
        try {
            if(iHomeFrag != null && slides != null && slides.size() > 0){
                iHomeFrag.progressVisibility(View.GONE);
                //Log.i("TAG_DATA", "onLoadSlidesFinished(TOTAL_SLIDES = "+slides.size()+")");
                iHomeFrag.loadRecyclerViewData(slides, 1);
                //--
                HomeView.IHome mIHome = iHomeFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                if(mIHome != null){
                    homePresenter.persistSlides(slides);
                }
                //--
                DAOSlide daoSlide = new DAOSlide(context);
                daoSlide.deleteAll();
                for(int i=0; i<slides.size(); i++){
                    daoSlide = new DAOSlide(context);
                    daoSlide.add(slides.get(i));
                }
            }
            else{
                //Log.i("TAG_DATA", "loadHomeFragData(DATABASE_OK)");
                // Retrieve slides from database
                DAOSlide daoSlide = new DAOSlide(context);
                slides = daoSlide.getAll();
                if(slides != null && slides.size() > 0) {
                    iHomeFrag.loadRecyclerViewData(slides, 1);
                    HomeView.IHome mIHome = iHomeFrag.retrieveIHomeInstance();
                    HomePresenter homePresenter = new HomePresenter(mIHome);
                    if(mIHome != null) {
                        homePresenter.persistSlides(slides);
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomeFragPresenter-->onLoadSlidesFinished() : "+ex.getMessage());
        }
    }

    // Method to notify that a slide has been selected
    public void notifySlideIsSelected(Slide slide){
        try {
            if(iHomeFrag != null && slide != null){
                HomeView.IHome mIHome = iHomeFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                homePresenter.showViewPager(slide.getTitle());
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomeFragPresenter-->notifySlideIsSelected() : "+ex.getMessage());
        }
    }


    public void cancelAsytask(){
        if(getAllSlides != null){
            getAllSlides.cancel(true);
        }
    }
}
