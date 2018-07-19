package com.maliprestige.View.Interfaces;

public class SplashView {
    public interface ISplash{
        public void initialize();
        public void events();
        public void hideHeader();
        public void displayHome();
        public void messageVisibility(int visibility);
    }

    public interface IPresenter{}
}
