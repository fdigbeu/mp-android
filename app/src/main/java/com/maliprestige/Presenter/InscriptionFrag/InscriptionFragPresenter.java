package com.maliprestige.Presenter.InscriptionFrag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maliprestige.Model.Client;
import com.maliprestige.Model.DAOClient;
import com.maliprestige.Model.JsonData;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.Presenter.Home.SendFormData;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.InscriptionFragView;

import org.json.JSONObject;

import java.util.HashMap;

public class InscriptionFragPresenter implements InscriptionFragView.IPresenter{
    // Ref interface
    private InscriptionFragView.IInscriptionFrag iInscriptionFrag;
    // Ref Asyntask
    private HashMap<String, String> postDataParams;
    private SendFormData sendInscriptionForm;

    public InscriptionFragPresenter(InscriptionFragView.IInscriptionFrag iInscriptionFrag) {
        this.iInscriptionFrag = iInscriptionFrag;
    }

    // Method to load inscription frag data
    public void loadInscriptionFragData(Context context){
        try {
            if(iInscriptionFrag != null && context != null){
                iInscriptionFrag.initialize();
                iInscriptionFrag.events();
                iInscriptionFrag.progressVisibility(View.GONE);
                final String[] civilites = context.getResources().getStringArray(R.array.liste_civilite);
                iInscriptionFrag.loadCiviliteData(civilites);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "InscriptionFragPresenter-->loadInscriptionFragData() : "+ex.getMessage());
        }
    }

    // Method to load external page web
    public void loadCGVPageWeb(Context context){
        try {
            String url = context.getResources().getString(R.string.mp_json_hote_production)+context.getResources().getString(R.string.mp_cgv_link);
            HomeView.IHome iHome = iInscriptionFrag.retrieveIHomeInstance();
            HomePresenter homePresenter = new HomePresenter(iHome);
            homePresenter.launchWebHtmlActivity(url);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "InscriptionFragPresenter-->loadPageWebFromUrl() : "+ex.getMessage());
        }
    }

    // Method to retrieve user data
    public void retrieveFormData(View view, String civilite, String nom, String email, String password, String confirmPassword){
        try {
            if (view != null && iInscriptionFrag != null) {
                if(nom.length()==0) {iInscriptionFrag.champNomObligatoire(); return;}
                if(email.length()==0) {iInscriptionFrag.champEmailObligatoire(); return;}
                if(password.length()==0) {iInscriptionFrag.champPasswordObligatoire(); return;}
                if(confirmPassword.length()==0) {iInscriptionFrag.champConfirmPasswordObligatoire(); return;}
                if(!password.equals(confirmPassword)) { HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.lb_password_non_identique));return; }
                if(!HomePresenter.isMailValide(email)){ HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.lb_form_email_invalide)); return; }
                //--
                if(HomePresenter.isMobileConnected(view.getContext())) {
                    iInscriptionFrag.progressVisibility(View.VISIBLE);
                    iInscriptionFrag.enableDisableButton(false);
                    //--
                    postDataParams = new HashMap<>();
                    postDataParams.put("civilite", civilite);
                    postDataParams.put("nom", nom);
                    postDataParams.put("email", email);
                    postDataParams.put("password", password);
                    postDataParams.put("confirmPassword", confirmPassword);
                    //--
                    String actionForm = view.getContext().getResources().getString(R.string.mp_json_hote_production) + view.getContext().getResources().getString(R.string.mp_json_client_inscription);
                    sendInscriptionForm = new SendFormData();
                    sendInscriptionForm.setiInscriptionPresenter(this);
                    sendInscriptionForm.initializeData(view.getContext(), postDataParams, actionForm);
                    sendInscriptionForm.setView(view);
                    sendInscriptionForm.execute();
                }
                else{
                    HomePresenter.messageSnackBar(view, view.getContext().getResources().getString(R.string.no_connection));
                    iInscriptionFrag.enableDisableButton(true);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "InscriptionFragPresenter-->retrieveFormData() : "+ex.getMessage());
        }
    }

    @Override
    public void onSendInscriptionFormFinished(Context context, String returnCode) {
        try {
            if(iInscriptionFrag != null && context != null){
                iInscriptionFrag.progressVisibility(View.GONE);
                iInscriptionFrag.enableDisableButton(true);
                if(returnCode == null){
                    if(sendInscriptionForm != null && sendInscriptionForm.getView() != null){
                        HomePresenter.messageSnackBar(sendInscriptionForm.getView(), context.getResources().getString(R.string.unstable_connection));
                    }
                    else {
                        Toast.makeText(context, context.getResources().getString(R.string.unstable_connection), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Log.i("TAG_RETURN_CODE", returnCode);
                    String jsonString = returnCode.replace("null", "");
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String codeRetour = jsonObject.getString("codeRetour");
                    String message = jsonObject.getString("message");
                    //--
                    if(Integer.parseInt(codeRetour) != 200){
                        if(sendInscriptionForm != null && sendInscriptionForm.getView() != null){
                            HomePresenter.messageSnackBar(sendInscriptionForm.getView(), message);
                        }
                        else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        JsonData jsonData = new JsonData(jsonString);
                        Client client = jsonData.getClientFromJson();
                        // Save token
                        HomePresenter.saveClientToken(context, client.getToken());
                        // Save client int the database
                        DAOClient daoClient = new DAOClient(context);
                        daoClient.deleteBy(client.getToken());
                        daoClient.add(client);
                        // Notify client is connected
                        HomeView.IHome mIHome = iInscriptionFrag.retrieveIHomeInstance();
                        HomePresenter homePresenter = new HomePresenter(mIHome);
                        homePresenter.loadHomeData(context);
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "InscriptionFragPresenter-->onSendInscriptionFormFinished() : "+ex.getMessage());
        }
    }


    public void cancelAsytask(){
        if(sendInscriptionForm != null){
            sendInscriptionForm.cancel(true);
        }
    }
}
