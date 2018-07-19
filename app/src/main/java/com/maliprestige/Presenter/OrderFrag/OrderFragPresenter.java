package com.maliprestige.Presenter.OrderFrag;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.maliprestige.Model.Commande;
import com.maliprestige.Model.CommandeProduit;
import com.maliprestige.Model.DAOCommande;
import com.maliprestige.Model.DAOCommandeProduit;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.OrderFragView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class OrderFragPresenter implements OrderFragView.IPresenter{

    private OrderFragView.IOrderFrag iOrderFrag;

    private GetAllOrders ordersAsyntask;

    public OrderFragPresenter(OrderFragView.IOrderFrag iOrderFrag) {
        this.iOrderFrag = iOrderFrag;
    }

    // Method to load order fragment data
    public void loadOrderFragData(View view){
        try {
            Context context = view.getContext();
            String clientToken = HomePresenter.retrieveClientToken(context);
            if(iOrderFrag != null && context != null && clientToken != null && clientToken.length() >= 50){
                iOrderFrag.initialize();
                iOrderFrag.events();

                iOrderFrag.swipeRefreshVisibility(false);
                iOrderFrag.messageVisibility(View.GONE);
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
                        else{
                            iOrderFrag.messageVisibility(View.VISIBLE);
                            iOrderFrag.messageOrder(context.getResources().getString(R.string.lb_aucune_commande));
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderFragPresenter-->loadOrderFragData() : "+ex.getMessage());
        }
    }


    // Method to refresh data by swiping
    public void swipeRefresFragmenthData(View view){
        try {
            Context context = view.getContext();
            String clientToken = HomePresenter.retrieveClientToken(context);
            if(HomePresenter.isMobileConnected(context) && iOrderFrag != null && clientToken != null && clientToken.length() >= 50) {
                HomeView.IHome iHome = iOrderFrag.retrieveIHomeInstance();
                if(iHome != null) {
                    HomePresenter homePresenter = new HomePresenter(iHome);
                    homePresenter.initializeRefreshFragment("refresh");
                    //--
                    refreshFragmentData(context);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderFragPresenter-->swipeRefresFragmenthData() : "+ex.getMessage());
        }
    }

    // Method to refresh fragment data
    public void refreshFragmentData(Context context){
        try {
            String clientToken = HomePresenter.retrieveClientToken(context);
            if(HomePresenter.isMobileConnected(context) && iOrderFrag != null && clientToken != null && clientToken.length() >= 50){
                HomeView.IHome iHome = iOrderFrag.retrieveIHomeInstance();
                if(iHome != null){
                    HomePresenter homePresenter = new HomePresenter(iHome);
                    String refreshData = homePresenter.retrieveRefreshFragment();
                    if(refreshData != null && (
                            refreshData.equalsIgnoreCase("paypal") ||
                            refreshData.equalsIgnoreCase("refresh")
                    )){
                        iOrderFrag.progressVisibility(View.VISIBLE);
                        if(refreshData.equalsIgnoreCase("refresh")){
                            iOrderFrag.swipeRefreshVisibility(true);
                            iOrderFrag.progressVisibility(View.GONE);
                        }
                        ordersAsyntask = new GetAllOrders();
                        ordersAsyntask.setOrderFragPresenter(this);
                        ordersAsyntask.initialize(context, clientToken);
                        ordersAsyntask.execute();
                        //--
                        homePresenter.initializeRefreshFragment(null);
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderFragPresenter-->refreshFragmentData() : "+ex.getMessage());
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

    // Method to show popup menu
    public void showPopupMenuOrder(View view, String numeroCommande, String numeroFacture, boolean isFactureAcquittee){
        try {
            if(iOrderFrag != null){
                showPopupWindow(view, numeroCommande, numeroFacture, isFactureAcquittee);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderFragPresenter-->showPopupMenuOrder() : "+ex.getMessage());
        }
    }

    // Manage when user click on reading item
    private void showPopupWindow(final View view, final String numeroCommande, final String numeroFacture, final boolean isFactureAcquittee) {
        MenuItem paypalMenuItem;
        final Context context = view.getContext();
        final String clientToken = HomePresenter.retrieveClientToken(context);
        final HomeView.IHome iHome = iOrderFrag.retrieveIHomeInstance();
        if(HomePresenter.isMobileConnected(context)){
            if(iHome != null && clientToken != null && clientToken.length() >= 50){
                //---
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.getMenuInflater().inflate(R.menu.menu_user_orders, popup.getMenu());
                paypalMenuItem = popup.getMenu().findItem(R.id.action_reglement_paypal);
                paypalMenuItem.setVisible(!isFactureAcquittee);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id){
                            case R.id.action_detail_commande:
                                String urlDetailCommande = context.getResources().getString(R.string.mp_json_hote_production)+
                                        context.getResources().getString(R.string.mp_json_client_detail_commande)
                                                .replace("{NUMERO_COMMANDE}", numeroCommande)
                                                .replace("{CLIENT_TOKEN}", clientToken);
                                HomePresenter pCmd = new HomePresenter(iHome);
                                pCmd.launchWebHtmlActivity(urlDetailCommande);
                                pCmd.initializeRefreshFragment(null);
                                break;
                            case R.id.action_detail_facture:
                                String urlDetailFacture = context.getResources().getString(R.string.mp_json_hote_production)+
                                        context.getResources().getString(R.string.mp_json_client_detail_facture)
                                                .replace("{NUMERO_FACTURE}", numeroFacture)
                                                .replace("{CLIENT_TOKEN}", clientToken);
                                HomePresenter pFacture = new HomePresenter(iHome);
                                pFacture.launchWebHtmlActivity(urlDetailFacture);
                                pFacture.initializeRefreshFragment(null);
                                break;
                            case R.id.action_reglement_paypal:
                                String urlPaypal = context.getResources().getString(R.string.mp_json_hote_production)+
                                        context.getResources().getString(R.string.mp_json_client_paiement_paypal)
                                                .replace("{NUMERO_COMMANDE}", numeroCommande)
                                                .replace("{CLIENT_TOKEN}", clientToken);
                                HomePresenter pPaypal = new HomePresenter(iHome);
                                pPaypal.launchWebHtmlActivity(urlPaypal);
                                pPaypal.initializeRefreshFragment("paypal");
                                break;
                            case R.id.action_voir_facture:
                                String urlGenererFacture = context.getResources().getString(R.string.mp_json_hote_production)+
                                        context.getResources().getString(R.string.mp_json_client_generer_facture)
                                                .replace("{NUMERO_FACTURE}", numeroFacture)
                                                .replace("{CLIENT_TOKEN}", clientToken);
                                HomePresenter.launchExternalPageWeb(context, urlGenererFacture);
                                HomePresenter pvFacture = new HomePresenter(iHome);
                                pvFacture.initializeRefreshFragment(null);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        }
        else{
            HomePresenter.messageSnackBar(view, context.getResources().getString(R.string.no_connection));
        }
    }

    @Override
    public void onLoadCommandesFinished(Context context, ArrayList<Commande> commandes) {
        try {
            String clientToken = HomePresenter.retrieveClientToken(context);
            if (iOrderFrag != null && clientToken != null && clientToken.length() >= 50){
                iOrderFrag.swipeRefreshVisibility(false);
                iOrderFrag.progressVisibility(View.GONE);
                iOrderFrag.messageVisibility(View.GONE);
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
                    else{
                        iOrderFrag.messageVisibility(View.VISIBLE);
                        iOrderFrag.messageOrder(context.getResources().getString(R.string.lb_aucune_commande));
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderFragPresenter-->onLoadCommandesFinished() : "+ex.getMessage());
        }
    }
}
