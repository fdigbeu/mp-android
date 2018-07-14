package com.maliprestige.Presenter.BasketFrag;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.DAOPanier;
import com.maliprestige.Model.Panier;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.BasketFragView;

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

    @Override
    public void onLoadPaniersFinished(Context context, ArrayList<Panier> paniers) {

    }
}
