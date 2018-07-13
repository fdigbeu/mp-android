package com.maliprestige.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maliprestige.Presenter.PwdOublieDialog.PwdOublieDialogPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.PwdOublieDialogView;

public class PwdOublieDialog implements PwdOublieDialogView.IPwdOublieDialog{
    // Ref widgets
    private Context context;
    private Dialog mDialog;
    private ProgressBar progressBar;
    private TextView txtEmail;
    private TextView titleDialog;
    private Button btnFermer;
    private Button btnEnvoyer;
    private String email;
    // Ref presenter
    private PwdOublieDialogPresenter dialogPresenter;

    public PwdOublieDialog(Context context){
        this.context = context;
    }

    public void showForm(){
        dialogPresenter = new PwdOublieDialogPresenter(this);
        dialogPresenter.loadPwdOublieDialogData(context);
    }

    @Override
    public void initialize() {
        mDialog=new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_password_oublie);
        mDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        txtEmail = mDialog.findViewById(R.id.form_pwdOublie_email);
        titleDialog = mDialog.findViewById(R.id.title_form_pwdOublie);
        titleDialog.setText(context.getResources().getString(R.string.lb_form_pasword_oublie).toUpperCase());
        btnFermer = mDialog.findViewById(R.id.form_btn_fermer);
        btnEnvoyer = mDialog.findViewById(R.id.form_btn_envoyer);
        progressBar = mDialog.findViewById(R.id.password_oublie_progressBar);
    }

    @Override
    public void events() {
        btnFermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPresenter.closeDialog();
            }
        });

        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmail.getText().toString().trim();
                dialogPresenter.retrieveFormData(v, email);
            }
        });


        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(mDialog != null) {
                    dialogPresenter.cancelAsytask();
                }
            }
        });

        mDialog.show();
    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void champEmailObligatoire() {
        txtEmail.setError(context.getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void displaySnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void enableDisableButton(boolean enable) {
        btnFermer.setEnabled(enable);
        btnEnvoyer.setEnabled(enable);
    }

    @Override
    public void closeDialog() {
        mDialog.dismiss();
    }
}
