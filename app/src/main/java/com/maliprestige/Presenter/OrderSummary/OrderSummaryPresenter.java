package com.maliprestige.Presenter.OrderSummary;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.maliprestige.Model.Adresse;
import com.maliprestige.Model.DAOAdresse;
import com.maliprestige.Model.DAOPanier;
import com.maliprestige.Model.Panier;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.Presenter.Home.SendFormData;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.OrderSummaryFragView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderSummaryPresenter implements OrderSummaryFragView.IPresenter {

    private OrderSummaryFragView.IOrderSummaryFrag iOrderSummaryFrag;
    // Ref CountDownTimer
    private CountDownTimer downTimer;
    // Ref Asyntask
    private HashMap<String, String> postDataParams;
    private SendFormData sendOrderForm;

    public OrderSummaryPresenter(OrderSummaryFragView.IOrderSummaryFrag iOrderSummaryFrag) {
        this.iOrderSummaryFrag = iOrderSummaryFrag;
    }

    // Method to load order summary data
    public void loadOrderSummaryData(final Context context){
        try {
            if(iOrderSummaryFrag != null && context != null){
                iOrderSummaryFrag.initialize();
                iOrderSummaryFrag.events();
                iOrderSummaryFrag.progressVisibility(View.GONE);
                iOrderSummaryFrag.messageVisibility(View.GONE);
                iOrderSummaryFrag.containerVisibility(View.GONE);
                String clientToken = HomePresenter.retrieveClientToken(context);
                if(clientToken != null && clientToken.length() >= 50){
                    String listeProduitsID = "";
                    String listeProduitsPrix = "";
                    String listeProduitsQte = "";
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
                        //--
                        listeProduitsID += paniers.get(i).getProduitId()+" ";
                        listeProduitsPrix += paniers.get(i).getPrixQuantite()+" ";
                        listeProduitsQte += paniers.get(i).getQuantite()+" ";
                    }
                    // Load products ID, Prix, QTE
                    iOrderSummaryFrag.loadListeProduitsId(listeProduitsID.trim());
                    iOrderSummaryFrag.loadListeProduitsPrix(listeProduitsPrix.trim());
                    iOrderSummaryFrag.loadListeProduitsQte(listeProduitsQte.trim());
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
                        iOrderSummaryFrag.messageSummary(context.getResources().getString(R.string.lb_pas_commande_encours));
                    }

                }
                else{
                    iOrderSummaryFrag.messageVisibility(View.VISIBLE);
                    final HomeView.IHome iHome = iOrderSummaryFrag.retrieveIHomeInstance();
                    if(iHome != null){
                        HomePresenter homePresenter = new HomePresenter(iHome);
                        int currentViewPagerItem = homePresenter.retrieveViewPagerCurrentItem();
                        // If current Fragment is : OrderSummaryFragment
                        if(currentViewPagerItem==10){
                            homePresenter.showViewPager(context.getResources().getString(R.string.lb_accueil));
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderSummaryPresenter-->loadOrderSummaryData() : "+ex.getMessage());
        }
    }

    // Method to load client adress
    public void loadClientAdresse(Context context){
        try {
            if(iOrderSummaryFrag != null && context != null){
                String clientToken = HomePresenter.retrieveClientToken(context);
                if(clientToken != null && clientToken.length() >= 50){
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
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderSummaryPresenter-->loadClientAdresse() : "+ex.getMessage());
        }
    }

    // Method to retrieve user entry
    public void retrieveUserEntriedData(View view, String libelleDateLivraison, int modePaiement, String adresseLivraison, String adresseFacturation, String montant, String listeProduitsID, String listeProduitsPrix, String listeProduitsQte){
        try {
            if(iOrderSummaryFrag != null && view != null){
                String aucuneAdressse = view.getContext().getResources().getString(R.string.lb_aucune_adresse);
                if(libelleDateLivraison == null) { HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.lb_erreur_date_livraison)); return; }
                if(modePaiement == 0) { HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.lb_erreur_aucun_mode_paiement)); return; }
                if(adresseLivraison.equalsIgnoreCase(aucuneAdressse)) { HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.lb_erreur_adresse_livraison)); return; }
                if(adresseFacturation.equalsIgnoreCase(aucuneAdressse)) { HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.lb_erreur_adresse_facturation)); return; }
                if(montant == null) { HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.lb_erreur_montant)); return; }
                //--
                if(listeProduitsID == null) { HomePresenter.messageSnackBar(view, "{ID}-"+view.getContext().getResources().getString(R.string.lb_erreur_data)); return; }
                if(listeProduitsPrix == null) { HomePresenter.messageSnackBar(view, "{PRIX}-"+view.getContext().getResources().getString(R.string.lb_erreur_data)); return; }
                if(listeProduitsQte == null) { HomePresenter.messageSnackBar(view, "{QTE}-"+view.getContext().getResources().getString(R.string.lb_erreur_data)); return; }
                //--
                String clientToken = HomePresenter.retrieveClientToken(view.getContext());
                if(clientToken != null && clientToken.length() >= 50){
                    String adresseLivraisonId = null;
                    String adresseFacturationId = null;
                    // Adresse de facturation
                    DAOAdresse daoAdresse = new DAOAdresse(view.getContext());
                    ArrayList<Adresse> adressesFacturations = daoAdresse.getAll(clientToken, "facturation");
                    if(adressesFacturations != null && adressesFacturations.size() > 0){
                        for (int i=0; i<adressesFacturations.size(); i++){
                            String mAdressesFac = adressesFacturations.get(i).getDestinataire()+" : "+adressesFacturations.get(i).getLibelle();
                            if(mAdressesFac.equalsIgnoreCase(adresseFacturation)){
                                adresseLivraisonId = ""+adressesFacturations.get(i).getAdresseId();
                                break;
                            }
                        }
                    }
                    // Adresse de livraison
                    daoAdresse = new DAOAdresse(view.getContext());
                    ArrayList<Adresse> adressesLivraisons = daoAdresse.getAll(clientToken, "livraison");
                    if(adressesLivraisons != null && adressesLivraisons.size() > 0){
                        for (int i=0; i<adressesLivraisons.size(); i++){
                            String mAdressesLiv = adressesLivraisons.get(i).getDestinataire()+" : "+adressesLivraisons.get(i).getLibelle();
                            if(mAdressesLiv.equalsIgnoreCase(adresseLivraison)){
                                adresseFacturationId = ""+adressesLivraisons.get(i).getAdresseId();
                                break;
                            }
                        }
                    }
                    // Post all the result
                    if(adresseLivraisonId != null && adresseFacturationId != null){
                        if(HomePresenter.isMobileConnected(view.getContext())) {
                            iOrderSummaryFrag.progressVisibility(View.VISIBLE);
                            iOrderSummaryFrag.enableDisableButton(false);
                            //--
                            postDataParams = new HashMap<>();
                            postDataParams.put("token", clientToken);
                            postDataParams.put("listeProduitsId", listeProduitsID);
                            postDataParams.put("listeProduitsPrix", listeProduitsPrix);
                            postDataParams.put("listeProduitsQuantite", listeProduitsQte);
                            postDataParams.put("adresseLivraisonId", adresseLivraisonId);
                            postDataParams.put("adresseFacturationId", adresseFacturationId);
                            postDataParams.put("modePaiementId", ""+modePaiement);
                            postDataParams.put("montantAPayer", montant.replace("€", "").replace(",", "."));
                            postDataParams.put("libelleDateLivraison", libelleDateLivraison);
                            //--
                            String actionForm = view.getContext().getResources().getString(R.string.mp_json_hote_production) + view.getContext().getResources().getString(R.string.mp_json_client_valider_commande);
                            sendOrderForm = new SendFormData();
                            sendOrderForm.setiOrderSummaryPresenter(this);
                            sendOrderForm.setView(view);
                            sendOrderForm.initializeData(view.getContext(), postDataParams, actionForm);
                            sendOrderForm.execute();
                        }
                        else{
                            HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.no_connection));
                            iOrderSummaryFrag.enableDisableButton(true);
                        }
                    }
                }
                else{
                    HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.lb_erreur_data));
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderSummaryPresenter-->retrieveUserEntriedData() : "+ex.getMessage());
        }
    }

    // Method to retrieve user action
    public void retrieveUserAction(View view){
        try {
            if(iOrderSummaryFrag != null && view != null){
                int id = view.getId();
                switch (id){
                    case R.id.btn_annuler_commande:
                        HomeView.IHome iHome = iOrderSummaryFrag.retrieveIHomeInstance();
                        if(iHome != null){
                            HomePresenter homePresenter = new HomePresenter(iHome);
                            homePresenter.showViewPager(view.getContext().getResources().getString(R.string.lb_accueil));
                        }
                        // Truncate basket
                        String clientToken = HomePresenter.retrieveClientToken(view.getContext());
                        if(clientToken != null && clientToken.length() >= 50){
                            DAOPanier daoPanier = new DAOPanier(view.getContext());
                            daoPanier.deleteBy(clientToken);
                        }
                        break;

                    case R.id.btn_ajouter_newAdresse_facturation:
                        iOrderSummaryFrag.launchAdresseForm("facturation");
                        break;

                    case R.id.btn_ajouter_newAdresse_livraison:
                        iOrderSummaryFrag.launchAdresseForm("livraison");
                        break;

                    case R.id.paypal_paiement:
                        iOrderSummaryFrag.changeBtnPaypalView();
                        break;

                    case R.id.mandat_cash_paiement:
                        iOrderSummaryFrag.changeBtnMendatCashView();
                        break;

                    case R.id.virement_paiement:
                        iOrderSummaryFrag.changeBtnVirementView();
                        break;

                    case R.id.espece_paiement:
                        iOrderSummaryFrag.changeBtnEspeceView();
                        break;
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderSummaryPresenter-->retrieveUserAction() : "+ex.getMessage());
        }
    }

    @Override
    public void onSendOrderFormFinished(final Context context, String returnCode) {
        try {
            if(context != null && iOrderSummaryFrag != null){
                final String clientToken = HomePresenter.retrieveClientToken(context);
                iOrderSummaryFrag.progressVisibility(View.GONE);
                iOrderSummaryFrag.enableDisableButton(true);
                if(returnCode == null){
                    if(sendOrderForm != null && sendOrderForm.getView() != null)
                        HomePresenter.messageSnackBar(sendOrderForm.getView(), context.getResources().getString(R.string.unstable_connection));
                    else
                        Toast.makeText(context, context.getResources().getString(R.string.unstable_connection), Toast.LENGTH_LONG).show();
                }
                else{
                    //Log.i("TAG_RETURN_CODE", returnCode);
                    String jsonString = returnCode.replace("null", "");
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String codeRetour = jsonObject.getString("codeRetour");
                    String message = jsonObject.getString("message");
                    //--
                    if(Integer.parseInt(codeRetour) != 200){
                        if(sendOrderForm != null && sendOrderForm.getView() != null)
                            HomePresenter.messageSnackBar(sendOrderForm.getView(), message);
                        else
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                    else{
                        HomeView.IHome iHome = iOrderSummaryFrag.retrieveIHomeInstance();
                        HomePresenter homePresenter = null;
                        if(iHome != null){
                            homePresenter = new HomePresenter(iHome);
                        }
                        String numeroCommande = jsonObject.getString("numeroCommande");
                        // Verify if paiement is payal
                        int mModePaiement = iOrderSummaryFrag.retrieveModePaiement();
                        // If payment payapl
                        if(mModePaiement==1){
                            // Load Paypal payment in WebView
                            String urlPaypal = context.getResources().getString(R.string.mp_json_hote_production)+
                                    context.getResources().getString(R.string.mp_json_client_paiement_paypal)
                                    .replace("{NUMERO_COMMANDE}", numeroCommande)
                                    .replace("{CLIENT_TOKEN}", clientToken);

                            if(iHome != null) {
                                iHome.launchWebHtmlActivity(urlPaypal);
                                //Log.i("TAG_URL", urlPaypal);
                            }
                        }
                        else{
                            if(sendOrderForm != null && sendOrderForm.getView() != null)
                                HomePresenter.messageSnackBar(sendOrderForm.getView(), message);
                            else
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            // Redirection vers liste des commandes
                            if(iHome != null){
                                homePresenter.showViewPager(context.getResources().getString(R.string.lb_commande));
                            }
                        }
                        // On vide le panier
                        if(clientToken != null && clientToken.length() >= 50){
                            downTimer = new CountDownTimer(3000, 1000) {
                                @Override
                                public void onTick(long l) {}

                                @Override
                                public void onFinish() {
                                    DAOPanier daoPanier = new DAOPanier(context);
                                    daoPanier.deleteBy(clientToken);
                                    // On affiche le message : Il n'y a plus de commande en cours
                                    iOrderSummaryFrag.containerVisibility(View.GONE);
                                    iOrderSummaryFrag.messageVisibility(View.VISIBLE);
                                    iOrderSummaryFrag.messageSummary(context.getResources().getString(R.string.lb_pas_commande_encours));
                                }
                            }.start();
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderSummaryPresenter-->onSendOrderFormFinished() : "+ex.getMessage());
        }
    }

    public void countDownTimeCancel(){
        try {
            if(downTimer != null){
                downTimer.cancel();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "OrderSummaryPresenter-->countDownTimeCancel() : "+ex.getMessage());
        }
    }
}
