package com.maliprestige.Presenter.PwdOublieDialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.maliprestige.Model.JsonData;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.Presenter.Home.SendFormData;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.PwdOublieDialogView;

import org.json.JSONObject;

import java.util.HashMap;

public class PwdOublieDialogPresenter implements PwdOublieDialogView.IPresenter{

    private PwdOublieDialogView.IPwdOublieDialog iPwdOublieDialog;

    // Ref Asyntask
    private HashMap<String, String> postDataParams;
    private SendFormData sendPwdOublieForm;

    public PwdOublieDialogPresenter(PwdOublieDialogView.IPwdOublieDialog iPwdOublieDialog) {
        this.iPwdOublieDialog = iPwdOublieDialog;
    }

    // Method to load dialog data
    public void loadPwdOublieDialogData(Context context){
        try {
            if(iPwdOublieDialog != null && context != null){
                iPwdOublieDialog.initialize();
                iPwdOublieDialog.events();
                iPwdOublieDialog.progressVisibility(View.GONE);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "PwdOublieDialogPresenter-->loadPwdOublieDialogData() : "+ex.getMessage());
        }
    }

    // Method to retrieve user data
    public void retrieveFormData(View view, String email){
        try {
            if (view != null && iPwdOublieDialog != null) {
                if(email.length()==0) {iPwdOublieDialog.champEmailObligatoire(); return;}
                if(!HomePresenter.isMailValide(email)){ iPwdOublieDialog.displaySnackBar(view, view.getContext().getResources().getString(R.string.lb_form_email_invalide)); return; }
                //--
                if(HomePresenter.isMobileConnected(view.getContext())) {
                    iPwdOublieDialog.progressVisibility(View.VISIBLE);
                    iPwdOublieDialog.enableDisableButton(false);
                    //--
                    postDataParams = new HashMap<>();
                    postDataParams.put("email", HomePresenter.crypterData(email));
                    //--
                    String actionForm = view.getContext().getResources().getString(R.string.mp_json_hote_production) + view.getContext().getResources().getString(R.string.mp_json_client_pwd_oublie);
                    sendPwdOublieForm = new SendFormData();
                    sendPwdOublieForm.setiPwdOublieDialogPresenter(this);
                    sendPwdOublieForm.setView(view);
                    sendPwdOublieForm.initializeData(view.getContext(), postDataParams, actionForm);
                    sendPwdOublieForm.execute();
                }
                else{
                    iPwdOublieDialog.displaySnackBar(view, view.getContext().getResources().getString(R.string.no_connection));
                    iPwdOublieDialog.enableDisableButton(true);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "PwdOublieDialogPresenter-->retrieveFormData() : "+ex.getMessage());
        }
    }

    // Method to close dialog
    public void closeDialog(){
        try {
            if(iPwdOublieDialog != null){
                iPwdOublieDialog.closeDialog();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "PwdOublieDialogPresenter-->closeDialog() : "+ex.getMessage());
        }
    }

    public void cancelAsytask(){
        if(sendPwdOublieForm != null){
            sendPwdOublieForm.cancel(true);
        }
    }

    @Override
    public void onSendPwdOublieFormFinished(Context context, String returnCode) {
        try {
            if(iPwdOublieDialog != null && context != null){
                iPwdOublieDialog.progressVisibility(View.GONE);
                iPwdOublieDialog.enableDisableButton(true);
                if(returnCode == null){
                    if(sendPwdOublieForm != null && sendPwdOublieForm.getView() != null)
                        HomePresenter.messageSnackBar(sendPwdOublieForm.getView(), context.getResources().getString(R.string.unstable_connection));
                    else
                        Toast.makeText(context, context.getResources().getString(R.string.unstable_connection), Toast.LENGTH_LONG).show();
                }
                else{
                    String jsonString = returnCode.replace("null", "");
                    //Log.i("TAG_RETURN_CODE", jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String codeRetour = jsonObject.getString("codeRetour");
                    String message = jsonObject.getString("message");
                    //--
                    if(Integer.parseInt(codeRetour) != 200){
                        if(sendPwdOublieForm != null && sendPwdOublieForm.getView() != null)
                            HomePresenter.messageSnackBar(sendPwdOublieForm.getView(), message);
                        else
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                    else{
                        String newCryptePassword = HomePresenter.decrypterData(jsonObject.getString("newPassword"));
                        Log.i("TAG_NEW_PASSWORD", "NEW_PASSWORD : "+newCryptePassword);
                        if(sendPwdOublieForm != null && sendPwdOublieForm.getView() != null)
                            HomePresenter.messageSnackBar(sendPwdOublieForm.getView(), message);
                        else
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "InscriptionFragPresenter-->onSendInscriptionFormFinished() : "+ex.getMessage());
        }
    }
}
