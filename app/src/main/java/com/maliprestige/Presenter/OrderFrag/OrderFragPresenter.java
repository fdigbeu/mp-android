package com.maliprestige.Presenter.OrderFrag;

import android.content.Context;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import com.maliprestige.Model.Commande;
import com.maliprestige.Model.CommandeProduit;
import com.maliprestige.Model.DAOCommande;
import com.maliprestige.Model.DAOCommandeProduit;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.OrderFragView;

import java.util.ArrayList;

public class OrderFragPresenter implements OrderFragView.IPresenter{

    private OrderFragView.IOrderFrag iOrderFrag;

    private GetAllOrders ordersAsyntask;

    public OrderFragPresenter(OrderFragView.IOrderFrag iOrderFrag) {
        this.iOrderFrag = iOrderFrag;
    }

    // Method to load order fragment data
    public void loadOrderFragData(Context context){
        try {
            String clientToken = HomePresenter.retrieveClientToken(context);
            if(iOrderFrag != null && context != null && clientToken != null && clientToken.length() >= 50){
                iOrderFrag.initialize();
                iOrderFrag.events();

                HomeView.IHome mIHome = iOrderFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                ArrayList<Commande> commandes = homePresenter.retrievePersistCommandes();
                if(commandes != null){
                    iOrderFrag.loadRecyclerViewData(commandes, 1);
                    homePresenter.persistCommandes(commandes);
                    Log.i("TAG_DATA", "loadOrderFragData(RETRIEVE_PERSISTANCE_DATA)");
                }
                else{
                    if(HomePresenter.isMobileConnected(context)){
                        Log.i("TAG_DATA", "loadOrderFragData(CALL_ASYNTASK)");
                        iOrderFrag.progressVisibility(View.VISIBLE);
                        ordersAsyntask = new GetAllOrders();
                        ordersAsyntask.setOrderFragPresenter(this);
                        ordersAsyntask.initialize(context, clientToken);
                        ordersAsyntask.execute();
                    }
                    else{
                        // Retrieve orders from database
                        DAOCommande daoCommande = new DAOCommande(context);
                        commandes = daoCommande.getAllBy(clientToken);
                        Log.i("TAG_DATA", "loadOrderFragData(DATABASE_OK_1)");
                        if(commandes != null && commandes.size() > 0) {
                            Log.i("TAG_DATA", "loadOrderFragData(DATABASE_OK_2)");
                            iOrderFrag.loadRecyclerViewData(commandes, 1);
                            if(mIHome != null) {
                                homePresenter.persistCommandes(commandes);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderFragPresenter-->loadOrderFragData() : "+ex.getMessage());
        }
    }

    // Method to load spanned data
    public static Spanned buildHtml(String htmlData){
        try {
            return HomePresenter.buildHtml(htmlData);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderFragPresenter-->buildHtml() : "+ex.getMessage());
            return null;
        }
    }

    public static String getColorStatut(String libelleStatut){
        if(libelleStatut.equalsIgnoreCase("Commande en attente de paiement")) return "#FC636B";
        if(libelleStatut.equalsIgnoreCase("Commande validée")) return "#0080FF";
        if(libelleStatut.equalsIgnoreCase("Commande annulée")) return "#EC971F";
        if(libelleStatut.equalsIgnoreCase("Commande retournée")) return "#F88E55";
        if(libelleStatut.equalsIgnoreCase("Commande fournisseur")) return "#83A697";
        if(libelleStatut.equalsIgnoreCase("Commande en préparation")) return "#31B0D5";
        if(libelleStatut.equalsIgnoreCase("Commande expédiée")) return "#0f9d58";
        if(libelleStatut.equalsIgnoreCase("Commande reçue")) return "#0f9d58";
        return "#000000";
    }

    public void cancelAsyntask(){
        if(ordersAsyntask != null){
            ordersAsyntask.cancel(true);
        }
    }

    @Override
    public void onLoadCommandesFinished(Context context, ArrayList<Commande> commandes) {
        try {
            iOrderFrag.progressVisibility(View.GONE);
            String clientToken = HomePresenter.retrieveClientToken(context);
            if (iOrderFrag != null && clientToken != null && clientToken.length() >= 50){
                //Log.i("TAG_DATA", "onLoadCommandesFinished(INSIDE_TOKEN)");
                // Save commande in database
                if(commandes != null && commandes.size() > 0){
                    Log.i("TAG_DATA", "commandes.size() = "+commandes.size());
                    iOrderFrag.loadRecyclerViewData(commandes, 1);
                    //--
                    HomeView.IHome mIHome = iOrderFrag.retrieveIHomeInstance();
                    HomePresenter homePresenter = new HomePresenter(mIHome);
                    if(mIHome != null) {
                        homePresenter.persistCommandes(commandes);
                    }
                    //--  Delete and Save order
                    DAOCommande daoCommande = new DAOCommande(context);
                    daoCommande.deleteBy(clientToken);
                    DAOCommandeProduit daoCommandeProduit = new DAOCommandeProduit(context);
                    daoCommandeProduit.deleteBy(clientToken);

                    //Log.i("TAG_DATA", "loadRecyclerViewData()");
                    ArrayList<ArrayList<CommandeProduit>> commandeProduits = new ArrayList<>();
                    for (int i=0; i<commandes.size(); i++){
                        daoCommande = new DAOCommande(context);
                        daoCommande.add(commandes.get(i));
                        commandeProduits.add(commandes.get(i).getCommandeProduits());
                        for (int j=0; j<commandes.get(i).getCommandeProduits().size(); j++){
                            CommandeProduit commandeProduit = commandes.get(i).getCommandeProduits().get(j);
                            daoCommandeProduit = new DAOCommandeProduit(context);
                            daoCommandeProduit.add(commandeProduit);
                        }
                    }
                }
                else{
                    // Retrieve orders from database
                    DAOCommande daoCommande = new DAOCommande(context);
                    commandes = daoCommande.getAllBy(clientToken);
                    //Log.i("TAG_DATA", "onLoadCommandesFinished(DATABASE_OK_1)");
                    if(commandes != null && commandes.size() > 0) {
                        //Log.i("TAG_DATA", "onLoadCommandesFinished(DATABASE_OK_2)");
                        iOrderFrag.loadRecyclerViewData(commandes, 1);
                        HomeView.IHome mIHome = iOrderFrag.retrieveIHomeInstance();
                        HomePresenter homePresenter = new HomePresenter(mIHome);
                        if(mIHome != null) {
                            homePresenter.persistCommandes(commandes);
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderFragPresenter-->onLoadCommandesFinished() : "+ex.getMessage());
        }
    }
}
