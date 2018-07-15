package com.maliprestige.Presenter.OrderSummary;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.Adresse;
import com.maliprestige.Model.DAOAdresse;
import com.maliprestige.Model.DAOPanier;
import com.maliprestige.Model.Panier;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.OrderSummaryFragView;

import java.util.ArrayList;

public class OrderSummaryPresenter implements OrderSummaryFragView.IPresenter {

    private OrderSummaryFragView.IOrderSummaryFrag iOrderSummaryFrag;

    public OrderSummaryPresenter(OrderSummaryFragView.IOrderSummaryFrag iOrderSummaryFrag) {
        this.iOrderSummaryFrag = iOrderSummaryFrag;
    }

    // Method to load order summary data
    public void loadOrderSummaryData(Context context){
        try {
            if(iOrderSummaryFrag != null && context != null){
                iOrderSummaryFrag.initialize();
                iOrderSummaryFrag.events();
                iOrderSummaryFrag.progressVisibility(View.GONE);
                iOrderSummaryFrag.messageVisibility(View.GONE);
                iOrderSummaryFrag.containerVisibility(View.GONE);
                String clientToken = HomePresenter.retrieveClientToken(context);
                if(clientToken != null && clientToken.length() >= 50){
                    // Montant à payer
                    DAOPanier daoPanier = new DAOPanier(context);
                    ArrayList<Panier> paniers = daoPanier.getAllBy(clientToken);
                    float montantTotal = 0f;
                    int delaiJourMin = 0;
                    int delaiJourMax = 0;
                    for (int i=0; i<paniers.size(); i++){
                        montantTotal += paniers.get(i).getPrixQuantite();
                        if(paniers.get(i).getDelaiJourMin() >= delaiJourMin) delaiJourMin = paniers.get(i).getDelaiJourMin();
                        if(paniers.get(i).getDelaiJourMax() >= delaiJourMax) delaiJourMax = paniers.get(i).getDelaiJourMax();
                    }
                    // If montant exists
                    if(montantTotal > 0){
                        iOrderSummaryFrag.containerVisibility(View.VISIBLE);
                        iOrderSummaryFrag.changeSubTotal("€"+String.format("%.2f", montantTotal));
                        // Date de livraison
                        String dateDelaiMin = HomePresenter.getDelaiDateLivraison(delaiJourMin);
                        String dateDelaiMax = HomePresenter.getDelaiDateLivraison(delaiJourMax);
                        String libelleDate = context.getResources().getString(R.string.lb_delai_livraison)
                                .replace("{DATE_LIVRAISON_1}", dateDelaiMin)
                                .replace("{DATE_LIVRAISON_2}", dateDelaiMax);
                        iOrderSummaryFrag.changeDateLivraison(libelleDate);

                        // Adresse de facturation
                        String[] mAdressesFac = {"Aucune adresse pour le moment"};
                        DAOAdresse daoAdresse = new DAOAdresse(context);
                        ArrayList<Adresse> adressesFacturations = daoAdresse.getAll(clientToken, "facturation");
                        if(adressesFacturations != null && adressesFacturations.size() > 0){
                            mAdressesFac = new String[adressesFacturations.size()];
                            for (int i=0; i<adressesFacturations.size(); i++){
                                mAdressesFac[i] = adressesFacturations.get(i).getDestinataire()+" : "+adressesFacturations.get(i).getLibelle();
                            }
                        }
                        iOrderSummaryFrag.loadAdressesFacturations(mAdressesFac);
                        // Adresse de livraison
                        String[] mAdressesLiv = {"Aucune adresse pour le moment"};
                        daoAdresse = new DAOAdresse(context);
                        ArrayList<Adresse> adressesLivraisons = daoAdresse.getAll(clientToken, "livraison");
                        if(adressesLivraisons != null && adressesLivraisons.size() > 0){
                            mAdressesLiv = new String[adressesLivraisons.size()];
                            for (int i=0; i<adressesLivraisons.size(); i++){
                                mAdressesLiv[i] = adressesLivraisons.get(i).getDestinataire()+" : "+adressesLivraisons.get(i).getLibelle();
                            }
                        }
                        iOrderSummaryFrag.loadAdressesLivraisons(mAdressesLiv);
                    }
                    else{
                        iOrderSummaryFrag.messageVisibility(View.VISIBLE);
                    }

                }
                else{
                    iOrderSummaryFrag.messageVisibility(View.VISIBLE);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderSummaryPresenter-->loadOrderSummaryData() : "+ex.getMessage());
        }
    }

    // Method to retrieve user action
    public void retrieveUserAction(View view){
        int id = view.getId();
        //switch (id){}
    }
}
