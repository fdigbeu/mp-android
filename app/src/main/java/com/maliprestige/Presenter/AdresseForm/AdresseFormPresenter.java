package com.maliprestige.Presenter.AdresseForm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.maliprestige.Model.Adresse;
import com.maliprestige.Model.DAOAdresse;
import com.maliprestige.Model.JsonData;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.Presenter.Home.SendFormData;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.AdresseFormView;
import com.maliprestige.View.Interfaces.HomeView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdresseFormPresenter implements AdresseFormView.IPresenter{

    private AdresseFormView.IAdresseForm iAdresseForm;
    // Ref Asyntask
    private HashMap<String, String> postDataParams;
    private SendFormData sendAdresseForm;

    public AdresseFormPresenter(AdresseFormView.IAdresseForm iAdresseForm) {
        this.iAdresseForm = iAdresseForm;
    }

    public void loadAdresseFormData(Context context, Intent intent){
        try {
            if(iAdresseForm != null && context != null && intent != null){
                iAdresseForm.initialize();
                iAdresseForm.events();
                iAdresseForm.progressVisibility(View.GONE);
                String titreForm = intent.getStringExtra("typeAdresse");
                iAdresseForm.loadTitleFromData("Nouvelle adresse de "+titreForm);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "AdresseFormPresenter-->loadAdresseFormData() : "+ex.getMessage());
        }
    }

    public void retrieveUserAction(MenuItem item){
        try {
            String titreForm = "";
            if(item != null && iAdresseForm != null){
                int id = item.getItemId();
                switch (id){
                    case R.id.action_adresse_livraison:
                        titreForm = "livraison";
                        iAdresseForm.loadTitleFromData("Nouvelle adresse de "+titreForm);
                        break;
                    case R.id.action_adresse_facturation:
                        titreForm = "facturation";
                        iAdresseForm.loadTitleFromData("Nouvelle adresse de "+titreForm);
                        break;
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "AdresseFormPresenter-->retrieveUserAction() : "+ex.getMessage());
        }
    }

    public void retrieveUserAction(View view, String nom, String prenom, String adresse, String cp, String ville, String pays, String typeAdresse){
        try {
            if(view != null && iAdresseForm != null){
                if(nom.length()==0) {iAdresseForm.champNomObligatoire(); return;}
                if(prenom.length()==0) {iAdresseForm.champPrenomObligatoire(); return;}
                if(adresse.length()==0) {iAdresseForm.champAdresseObligatoire(); return;}
                if(cp.length()==0) {iAdresseForm.champCodePostalObligatoire(); return;}
                if(ville.length()==0) {iAdresseForm.champVilleObligatoire(); return;}
                if(pays.length()==0) {iAdresseForm.champPaysObligatoire(); return;}
                //--
                String clientToken = HomePresenter.retrieveClientToken(view.getContext());
                //--
                if(HomePresenter.isMobileConnected(view.getContext())) {
                    iAdresseForm.progressVisibility(View.VISIBLE);
                    iAdresseForm.enableDisableButton(false);
                    //--
                    postDataParams = new HashMap<>();
                    postDataParams.put("token", clientToken);
                    postDataParams.put("typeAdresse", typeAdresse);
                    postDataParams.put("nom", nom);
                    postDataParams.put("prenom", prenom);
                    postDataParams.put("adresse", adresse);
                    postDataParams.put("codePostal", cp);
                    postDataParams.put("ville", ville);
                    postDataParams.put("pays", pays);
                    //--
                    String actionForm = view.getContext().getResources().getString(R.string.mp_json_hote_production) + view.getContext().getResources().getString(R.string.mp_json_client_ajouter_adresse);
                    sendAdresseForm = new SendFormData();
                    sendAdresseForm.setiAdresseFormPresenter(this);
                    sendAdresseForm.initializeData(view.getContext(), postDataParams, actionForm);
                    sendAdresseForm.execute();
                }
                else{
                    iAdresseForm.displaySnackBar(view, view.getContext().getResources().getString(R.string.no_connection));
                    iAdresseForm.enableDisableButton(true);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "AdresseFormPresenter-->retrieveUserAction() : "+ex.getMessage());
        }
    }

    @Override
    public void onSendAdresseFormFinished(Context context, String returnCode) {
        try {
            if(iAdresseForm != null && context != null){
                iAdresseForm.progressVisibility(View.GONE);
                iAdresseForm.enableDisableButton(true);
                if(returnCode == null){
                    Toast.makeText(context, context.getResources().getString(R.string.unstable_connection), Toast.LENGTH_LONG).show();
                }
                else{
                    Log.i("TAG_RETURN_CODE", returnCode);
                    String jsonString = returnCode.replace("null", "");
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String codeRetour = jsonObject.getString("codeRetour");
                    String message = jsonObject.getString("message");
                    //--
                    if(Integer.parseInt(codeRetour) != 200){
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                    else{
                        JsonData jsonData = new JsonData(jsonString);
                        String clientToken = HomePresenter.retrieveClientToken(context);
                        String typeAdresse = iAdresseForm.retrieveTypeAdresse();
                        Adresse adresse = null;
                        if(typeAdresse.equalsIgnoreCase("facturation")){
                            adresse = jsonData.getAdresseFacturationFromJson(clientToken);
                        }
                        else if(typeAdresse.equalsIgnoreCase("livraison")){
                            adresse = jsonData.getAdresseLivraisonFromJson(clientToken);
                        }
                        else{}
                        // Save in the data base
                        DAOAdresse daoAdresse = new DAOAdresse(context);
                        daoAdresse.add(adresse);
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "AdresseFormPresenter-->onSendAdresseFormFinished() : "+ex.getMessage());
        }
    }

    public void closeActivity(){
        try {
            if(iAdresseForm != null) iAdresseForm.closeActivity();
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "AdresseFormPresenter-->closeActivity() : "+ex.getMessage());
        }
    }
}
