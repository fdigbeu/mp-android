package com.maliprestige.Presenter.Home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;

import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Screen;
import com.maliprestige.Model.Slide;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.HomeView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomePresenter implements HomeView.IPresenter{

    private HomeView.IHome iHome;

    public HomePresenter(HomeView.IHome iHome) {
        this.iHome = iHome;
    }

    // Method to load home data
    public void loadHomeData(final Context context){
        try {
            if(iHome != null && context != null){
                iHome.initialize();
                iHome.events();
                iHome.notifyUserIsConnected(false);
                // Home : default
                iHome.changeHomeView(0, context.getResources().getString(R.string.lb_accueil));
                // Checked home menu in navigation view
                iHome.checkedNavigationView(R.id.nav_menu_accueil);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->loadHomeData() : "+ex.getMessage());
        }
    }

    // Method to open or close drawer menu
    public void openOrCloseMenuDrawer(DrawerLayout drawer){
        try {
            if(drawer != null && iHome != null){
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    iHome.closeActivity();
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->openOrCloseMenuDrawer() : "+ex.getMessage());
        }
    }

    // Method to show view pager
    public void showViewPager(String titreSlide){
        try {
            if(iHome != null && titreSlide != null){
                String tabTitre[] = titreSlide.split( " ");
                String typeProduit = tabTitre.length >= 2 ? tabTitre[1] : tabTitre[0];
                switch (typeProduit){
                    case "hommes": // Vêtements hommes
                        iHome.changeHomeView(1, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_vetement_homme);
                        break;

                    case "femmes": // Vêtements femmes
                        iHome.changeHomeView(2, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_vetement_femme);
                        break;

                    case "enfants": // Vêtements enfants
                        iHome.changeHomeView(3, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_vetement_enfant);
                        break;

                    case "exotiques": // Produits exotiques
                        iHome.changeHomeView(4, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_produit_exotique);
                        break;

                    case "connecter": // Se connecter
                        iHome.changeHomeView(8, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_connexion);
                        break;
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->showViewPager() : "+ex.getMessage());
        }
    }

    // Method to manage user action
    public void retrieveUserAction(Context context, MenuItem item){
        try {
            if(iHome != null && item != null && context != null){
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_menu_accueil:
                        iHome.changeHomeView(0, context.getResources().getString(R.string.lb_accueil));
                        break;

                    case R.id.nav_vetement_homme:
                        iHome.changeHomeView(1, context.getResources().getString(R.string.lb_homme));
                        break;

                    case R.id.nav_vetement_femme:
                        iHome.changeHomeView(2, context.getResources().getString(R.string.lb_femme));
                        break;

                    case R.id.nav_vetement_enfant:
                        iHome.changeHomeView(3, context.getResources().getString(R.string.lb_enfant));
                        break;

                    case R.id.nav_produit_exotique:
                        iHome.changeHomeView(4, context.getResources().getString(R.string.lb_exotique));
                        break;

                    case R.id.nav_mon_compte:
                        iHome.changeHomeView(5, context.getResources().getString(R.string.lb_compte));
                        break;

                    case R.id.nav_inscription:
                        iHome.changeHomeView(9, context.getResources().getString(R.string.lb_inscription));
                        break;

                    case R.id.nav_connexion:
                        iHome.changeHomeView(8, context.getResources().getString(R.string.lb_connexion));
                        break;

                    case R.id.nav_mes_commandes:
                        iHome.changeHomeView(6, context.getResources().getString(R.string.lb_commande));
                        break;

                    case R.id.nav_mon_panier:
                    case R.id.action_shopping:
                        iHome.changeHomeView(7, context.getResources().getString(R.string.lb_shopping));
                        if(id == R.id.action_shopping){
                            iHome.checkedNavigationView(R.id.nav_mon_panier);
                        }
                        break;

                    case R.id.nav_partager_app:
                        break;

                    case R.id.nav_envoyer_mail:
                        break;

                    case R.id.nav_deconnexion:
                        break;

                    case R.id.nav_livraison:
                        break;

                    case R.id.nav_qui_sommes_nous:
                        break;

                    case R.id.nav_conditions_ventes:
                        break;
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrieveUserAction() : "+ex.getMessage());
        }
    }

    // Persist slides data
    public void persistSlides(ArrayList<Slide> slides){
        try {
            if (iHome != null && slides != null) {
                iHome.persistSlides(slides);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->persistSlides() : "+ex.getMessage());
        }
    }
    // Retrieve persist slides data
    public ArrayList<Slide> retrievePersistSlides(){
        try {
            if (iHome != null) {
                return iHome.retrievePersistSlides();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrieveSlides() : "+ex.getMessage());
        }
        return null;
    }

    // Persist produits data
    public void persistProduits(String key, ArrayList<Produit> produits){
        try {
            if (iHome != null && produits != null) {
                iHome.persistProduits(key, produits);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->persistProduits() : "+ex.getMessage());
        }
    }
    // Retrieve persist produits data
    public ArrayList<Produit> retrievePersistProduits(String key){
        try {
            if (iHome != null) {
                return iHome.retrievePersistProduits(key);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrievePersistProduits() : "+ex.getMessage());
        }
        return null;
    }

    // Get screen resolution
    public static Screen getScreenResolution(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new Screen(width, height);
    }

    public static boolean isMobileConnected(Context context){
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType) return true;
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean isMailValide(String emailStr)
    {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(emailStr);
        return matcher.find();
    }

    public static Spanned buildHtml(String str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(str);
        }
    }

    // Method to retrieve client token
    public static String retrieveClientToken(Context context){
        try { return getDataFromSharePreferences(context, "MP_CLIENT_TOKEN"); }
        catch (Exception ex){
            return null;
        }
    }

    // Method to save client token
    public static void saveClientToken(Context context, String token){
        try { saveDataInSharePreferences(context, "MP_CLIENT_TOKEN", token); }
        catch (Exception ex){ Log.e("TAG_ERROR", "HomePresenter-->saveClientToken() : "+ex.getMessage()); }
    }


    // Method to get data in share preferences
    private static String getDataFromSharePreferences(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    // Method to save data in share preferences
    private static void saveDataInSharePreferences(Context context, String key, String contentData){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, contentData);
            editor.commit(); // <=> editor.apply();
        }
        catch (Exception ex){}
    }

    @Override
    public void onLoadProduitsFinished(Context context, ArrayList<Produit> produits) {

    }
}
