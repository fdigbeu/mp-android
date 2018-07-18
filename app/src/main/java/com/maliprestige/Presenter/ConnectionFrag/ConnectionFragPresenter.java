package com.maliprestige.Presenter.ConnectionFrag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.maliprestige.Model.Adresse;
import com.maliprestige.Model.Client;
import com.maliprestige.Model.DAOAdresse;
import com.maliprestige.Model.DAOClient;
import com.maliprestige.Model.JsonData;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.Presenter.Home.SendFormData;
import com.maliprestige.R;
import com.maliprestige.View.Dialogs.PwdOublieDialog;
import com.maliprestige.View.Interfaces.ConnectionFragView;
import com.maliprestige.View.Interfaces.HomeView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionFragPresenter implements ConnectionFragView.IPresenter {

    private ConnectionFragView.IConnectionFrag iConnectionFrag;
    // Ref Asyntask
    private HashMap<String, String> postDataParams;
    private SendFormData sendConnexionForm;

    public ConnectionFragPresenter(ConnectionFragView.IConnectionFrag iConnectionFrag) {
        this.iConnectionFrag = iConnectionFrag;
    }

    // Method to load connection frag data
    public void loadConnectionFragData(Context context){
        try {
            if(iConnectionFrag != null && context != null){
                iConnectionFrag.initialize();
                iConnectionFrag.events();
                iConnectionFrag.progressVisibility(View.GONE);
                //--
                String clientToken = HomePresenter.retrieveClientToken(context);
                if(clientToken != null && clientToken.equalsIgnoreCase("MP_CLIENT_DECONNECTED")){
                    String jsonClient = HomePresenter.retrieveLastClientConnected(context);
                    if(jsonClient != null){
                        Client client = new JsonData(jsonClient).getClientFromJson();
                        if(client != null) {
                            iConnectionFrag.rempliChampEmail(client.getEmail());
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ConnectionFragPresenter-->loadConnectionFragData() : "+ex.getMessage());
        }
    }

    // Method to show dialog to modify password
    public void passwordOublie(View view){
        if(iConnectionFrag != null && view != null) {
            try {
                Context context = view.getContext();
                PwdOublieDialog pwdOublieDialog = new PwdOublieDialog(context);
                pwdOublieDialog.setmView(view);
                pwdOublieDialog.showForm();
            }
            catch (Exception ex){
                Log.e("TAG_ERROR", "ConnectionFragPresenter-->passwordOublie() : "+ex.getMessage());
            }
        }
    }

    // Methode to create a client account
    public void createNewCompteClient(){
        try {
            if(iConnectionFrag != null) {
                HomeView.IHome mIHome = iConnectionFrag.retrieveIHomeInstance();
                HomePresenter homePresenter = new HomePresenter(mIHome);
                if (mIHome != null) {
                    homePresenter.showViewPager("inscription");
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ConnectionFragPresenter-->createNewCompteClient() : "+ex.getMessage());
        }
    }

    // Method to retrieve user data
    public void retrieveFormData(View view, String email, String password){
        try {
            if (view != null && iConnectionFrag != null) {
                if(email.length()==0) {iConnectionFrag.champEmailObligatoire(); return;}
                if(password.length()==0) {iConnectionFrag.champPasswordObligatoire(); return;}
                if(!HomePresenter.isMailValide(email)){ iConnectionFrag.displaySnackBar(view, view.getContext().getResources().getString(R.string.lb_form_email_invalide)); return; }
                //--
                if(HomePresenter.isMobileConnected(view.getContext())) {
                    iConnectionFrag.progressVisibility(View.VISIBLE);
                    iConnectionFrag.enableDisableButton(false);
                    //--
                    postDataParams = new HashMap<>();
                    postDataParams.put("email", HomePresenter.crypterData(email));
                    postDataParams.put("password", HomePresenter.crypterData(password));
                    //--
                    String actionForm = view.getContext().getResources().getString(R.string.mp_json_hote_production) + view.getContext().getResources().getString(R.string.mp_json_client_connexion);
                    sendConnexionForm = new SendFormData();
                    sendConnexionForm.setiConnectionPresenter(this);
                    sendConnexionForm.setView(view);
                    sendConnexionForm.initializeData(view.getContext(), postDataParams, actionForm);
                    sendConnexionForm.execute();
                }
                else{
                    iConnectionFrag.displaySnackBar(view, view.getContext().getResources().getString(R.string.no_connection));
                    iConnectionFrag.enableDisableButton(true);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ConnectionFragPresenter-->retrieveFormData() : "+ex.getMessage());
        }
    }

    @Override
    public void onSendConnectionFormFinished(Context context, String returnCode) {
        try {
            if(iConnectionFrag != null && context != null){
                iConnectionFrag.progressVisibility(View.GONE);
                iConnectionFrag.enableDisableButton(true);
                if(returnCode == null){
                    if(sendConnexionForm != null && sendConnexionForm.getView() != null){
                        HomePresenter.messageSnackBar(sendConnexionForm.getView(), context.getResources().getString(R.string.unstable_connection));
                    }
                    else{
                        Toast.makeText(context, context.getResources().getString(R.string.unstable_connection), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    String jsonString = returnCode.replace("null", "");
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String codeRetour = jsonObject.getString("codeRetour");
                    String message = jsonObject.getString("message");
                    String jsonFacturations = "";
                    if(!jsonObject.isNull("adressesFacturations")){
                        jsonFacturations = jsonObject.getString("adressesFacturations");
                    }
                    String jsonLivraisons = "";
                    if(!jsonObject.isNull("adressesFacturations")){
                        jsonLivraisons = jsonObject.getString("adressesLivraisons");
                    }
                    Log.i("TAG_JSON", jsonString);
                    if(Integer.parseInt(codeRetour) != 200){
                        if(sendConnexionForm != null && sendConnexionForm.getView() != null){
                            HomePresenter.messageSnackBar(sendConnexionForm.getView(), message);
                        }
                        else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        JsonData jsonData = new JsonData(jsonString);
                        Client client = jsonData.getClientFromJson();
                        jsonData = new JsonData(jsonLivraisons);
                        ArrayList<Adresse> adressesLivraisons = jsonData.getAdressesLivraisonFromJson(client.getToken());
                        jsonData = new JsonData(jsonFacturations);
                        ArrayList<Adresse> adressesFacturations = jsonData.getAdressesFacturationsFromJson(client.getToken());
                        // Save client token
                        HomePresenter.saveClientToken(context, client.getToken());
                        // Save client int the database
                        DAOClient daoClient = new DAOClient(context);
                        daoClient.deleteBy(client.getToken());
                        daoClient.add(client);
                        // Save adresses livraisons
                        if(adressesLivraisons != null && !adressesLivraisons.isEmpty()){
                            DAOAdresse daoAdresse = new DAOAdresse(context);
                            daoAdresse.delete(client.getToken(), "livraison");
                            for (int i=0; i<adressesLivraisons.size(); i++){
                                daoAdresse = new DAOAdresse(context);
                                daoAdresse.add(adressesLivraisons.get(i));
                            }
                        }
                        // Save adresses facturations
                        if(adressesFacturations != null && !adressesFacturations.isEmpty()){
                            DAOAdresse daoAdresse = new DAOAdresse(context);
                            daoAdresse.delete(client.getToken(), "facturation");
                            for (int i=0; i<adressesFacturations.size(); i++){
                                daoAdresse = new DAOAdresse(context);
                                daoAdresse.add(adressesFacturations.get(i));
                            }
                        }
                        // Save last client connected
                        HomePresenter.saveLastClientConnected(context, client.toString());
                        // Notify client is connected
                        HomeView.IHome mIHome = iConnectionFrag.retrieveIHomeInstance();
                        HomePresenter homePresenter = new HomePresenter(mIHome);
                        homePresenter.loadHomeData(context);
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ConnectionFragPresenter-->onSendConnectionFormFinished() : "+ex.getMessage());
        }
    }


    public void cancelAsytask(){
        if(sendConnexionForm != null){
            sendConnexionForm.cancel(true);
        }
    }
}
