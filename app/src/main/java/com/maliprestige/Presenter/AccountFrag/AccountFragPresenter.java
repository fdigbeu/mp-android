package com.maliprestige.Presenter.AccountFrag;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.Adresse;
import com.maliprestige.Model.Client;
import com.maliprestige.Model.DAOAdresse;
import com.maliprestige.Model.DAOClient;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.View.Interfaces.AccountFragView;

import java.util.ArrayList;

public class AccountFragPresenter{

    private AccountFragView.IAccountFrag iAccountFrag;

    public AccountFragPresenter(AccountFragView.IAccountFrag iAccountFrag) {
        this.iAccountFrag = iAccountFrag;
    }

    public void loadAccountFragData(Context context){
        try {
            if(iAccountFrag != null && context != null){
                iAccountFrag.initialize();
                iAccountFrag.events();
                iAccountFrag.progressVisibility(View.GONE);
                //--
                String clientToken = HomePresenter.retrieveClientToken(context);
                if(clientToken != null && clientToken.length() >= 50){
                    //--
                    DAOClient daoClient = new DAOClient(context);
                    Client client = daoClient.getInfo(clientToken);
                    iAccountFrag.loadIdentiteData(client);
                    //--
                    String[] mAdressesFac = {"Aucune adresse pour le moment"};
                    DAOAdresse daoAdresse = new DAOAdresse(context);
                    ArrayList<Adresse> adressesFacturations = daoAdresse.getAll(clientToken, "facturation");
                    if(adressesFacturations != null && adressesFacturations.size() > 0){
                        mAdressesFac = new String[adressesFacturations.size()];
                        for (int i=0; i<adressesFacturations.size(); i++){
                            mAdressesFac[i] = adressesFacturations.get(i).getDestinataire()+" : "+adressesFacturations.get(i).getLibelle();
                        }
                    }
                    iAccountFrag.loadAdressesFacturations(mAdressesFac);
                    //--
                    String[] mAdressesLiv = {"Aucune adresse pour le moment"};
                    daoAdresse = new DAOAdresse(context);
                    ArrayList<Adresse> adressesLivraisons = daoAdresse.getAll(clientToken, "livraison");
                    if(adressesLivraisons != null && adressesLivraisons.size() > 0){
                        mAdressesLiv = new String[adressesLivraisons.size()];
                        for (int i=0; i<adressesLivraisons.size(); i++){
                            mAdressesLiv[i] = adressesLivraisons.get(i).getDestinataire()+" : "+adressesLivraisons.get(i).getLibelle();
                        }
                    }
                    iAccountFrag.loadAdressesLivraisons(mAdressesLiv);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "AccountFragPresenter-->loadAccountFragData() : "+ex.getMessage());
        }
    }
}
