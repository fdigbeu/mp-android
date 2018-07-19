package com.maliprestige.Presenter.BasketFrag;

import android.content.Context;
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
import com.maliprestige.View.Interfaces.BasketFragView;
import com.maliprestige.View.Interfaces.HomeView;

import java.util.ArrayList;

public class BasketFragPresenter implements BasketFragView.IPresenter{

    private BasketFragView.IBasketFrag iBasketFrag;

    public BasketFragPresenter(BasketFragView.IBasketFrag iBasketFrag) {
        this.iBasketFrag = iBasketFrag;
    }

    // Method to load basket data fragment
    public void loadBasketFragData(Context context){
        try {
            if(iBasketFrag != null && context != null){
                iBasketFrag.initialize();
                iBasketFrag.events();
                //--
                iBasketFrag.messageVisibility(View.GONE);
                iBasketFrag.validerPanierVisibility(View.GONE);
                //--
                String clientToken = HomePresenter.retrieveClientToken(context);
                if(clientToken != null && !clientToken.isEmpty()){
                    DAOPanier daoPanier = new DAOPanier(context);
                    ArrayList<Panier> paniers = daoPanier.getAllBy(clientToken);
                    if(paniers != null && paniers.size() > 0){
                        iBasketFrag.validerPanierVisibility(View.VISIBLE);
                        iBasketFrag.loadRecyclerViewData(paniers, 1);
                        float montantTotal = 0f;
                        for (int i=0; i<paniers.size(); i++){ montantTotal += paniers.get(i).getPrixQuantite(); }
                        iBasketFrag.changeSubTotal("Sous-Total : €"+String.format("%.2f", montantTotal));
                    }
                    else{
                        iBasketFrag.messageVisibility(View.VISIBLE);
                        iBasketFrag.messagePanier(context.getResources().getString(R.string.lb_panier_vide));
                    }
                }
                else{
                    iBasketFrag.messageVisibility(View.VISIBLE);
                    iBasketFrag.messagePanier(context.getResources().getString(R.string.lb_panier_vide));
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "BasketFragPresenter-->loadBasketFragData() : "+ex.getMessage());
        }
    }

    // Method to refresh basket data
    public void refreshBasketData(Context context) {
        if(iBasketFrag != null && context != null){
            HomeView.IHome iHome = iBasketFrag.retrieveIHomeInstance();
            if(iHome != null){
                int viewPagerCurrentItem = iHome.retrieveViewPagerCurrentItem();
                // If current fragement is : BasketFragment
                if(viewPagerCurrentItem == 7){
                    loadBasketFragData(context);
                }
            }
        }
    }

    // Method to retrieve user action
    public void retrieveUserAction(View view){
        try {
            if(iBasketFrag != null && view != null){
                int id = view.getId();
                switch (id){
                    case R.id.btn_valider_panier:
                        HomeView.IHome iHome = iBasketFrag.retrieveIHomeInstance();
                        if(iHome != null){
                            HomePresenter homePresenter = new HomePresenter(iHome);
                            // If user is connected
                            String clientToken = HomePresenter.retrieveClientToken(view.getContext());
                            if(clientToken != null && clientToken.length() >= 50){
                                // Redirect user to order summary
                                homePresenter.persistNumberViewPager(0);
                                homePresenter.showViewPager("recapitulatif");
                            }
                            else{
                                // Ask user to connect him
                                homePresenter.persistNumberViewPager(10);
                                homePresenter.showViewPager("connecter");
                            }
                        }
                        break;
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "BasketFragPresenter-->retrieveUserAction() : "+ex.getMessage());
        }
    }

    // Method to modify basket data
    public void modifyBasketData(Context context, Panier panier, String action){
        try {
            if(iBasketFrag != null && context != null && panier != null){
                DAOPanier daoPanier = null;
                // If product is not deleted in the basket
                if(action.equalsIgnoreCase("modifier")){
                    daoPanier = new DAOPanier(context);
                    daoPanier.modify(panier.getToken(), panier.getProduitId(), panier.getQuantite(), panier.getPrixQuantite());
                    //Log.i("TAG_ACTION", "modifyBasketData(MODIFIER)");
                }
                else if(action.equalsIgnoreCase("supprimer")){
                    daoPanier = new DAOPanier(context);
                    daoPanier.deleteBy(panier.getToken(), panier.getProduitId());
                    //Log.i("TAG_ACTION", "modifyBasketData(SUPPRIMER)");
                }
                else{}
                //--
                String clientToken = panier.getToken();
                if(clientToken != null && !clientToken.isEmpty()){
                    //Log.i("TAG_ACTION", "modifyBasketData(MODIFIER_SOUS_TOTAL)");
                    daoPanier = new DAOPanier(context);
                    ArrayList<Panier> paniers = daoPanier.getAllBy(clientToken);
                    if(paniers != null && paniers.size() > 0){
                        float montantTotal = 0f;
                        for (int i=0; i<paniers.size(); i++){ montantTotal += paniers.get(i).getPrixQuantite(); }
                        iBasketFrag.changeSubTotal("Sous-Total : €"+String.format("%.2f", montantTotal));
                    }
                    else{
                        iBasketFrag.validerPanierVisibility(View.GONE);
                        iBasketFrag.messageVisibility(View.VISIBLE);
                        iBasketFrag.messagePanier(context.getResources().getString(R.string.lb_panier_vide));
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "BasketFragPresenter-->modifyBasketData() : "+ex.getMessage());
        }
    }

    // Method to show bascket detail
    public void showProductDetail(Context context, Panier panier){
        try {
            if(iBasketFrag != null){
                Log.i("TAG_PRODUIT_ID", "panier : "+panier.getProduitId());
                DAOSearch daoSearch = new DAOSearch(context);
                Search search = daoSearch.getInfo(panier.getProduitId());
                if(search != null && panier != null){
                    HomeView.IHome mIHome = iBasketFrag.retrieveIHomeInstance();
                    HomePresenter homePresenter = new HomePresenter(mIHome);
                    Produit produit = new Produit();
                    produit.setProduitId(panier.getProduitId());
                    produit.setNom(panier.getNomProduit());
                    produit.setImage1(search.getImage1());
                    produit.setImage2(search.getImage2());
                    produit.setImage3(search.getImage3());
                    homePresenter.launchProduitDetail(produit);
                }
                //Log.i("TAG_PRODUIT_NOM", "panier : "+search.getNomProduit());
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "BasketFragPresenter-->showProductDetail() : "+ex.getMessage());
        }
    }

    @Override
    public void onLoadPaniersFinished(Context context, ArrayList<Panier> paniers) {

    }
}
