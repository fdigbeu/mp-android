package com.maliprestige.View.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maliprestige.Presenter.AdresseForm.AdresseFormPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.AdresseFormView;

public class AdresseFormActivity extends AppCompatActivity implements AdresseFormView.IAdresseForm{

    // Ref widgets
    private Toolbar toolbar;
    private TextView adresseTitle;
    private TextInputEditText nom;
    private TextInputEditText prenom;
    private TextInputEditText adresse;
    private TextInputEditText cp;
    private TextInputEditText ville;
    private TextInputEditText pays;
    private Button btnEnregistrer;
    private LinearLayout progressBar;

    // Attributes
    private String mNom;
    private String mPrenom;
    private String mAdresse;
    private String mCp;
    private String mVille;
    private String mPays;
    private String mTypeAdresse;


    // Ref presenter
    private AdresseFormPresenter formPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adresse_form);

        formPresenter = new AdresseFormPresenter(this);
        formPresenter.loadAdresseFormData(AdresseFormActivity.this, this.getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adresse_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                formPresenter.closeActivity();
                break;

            case R.id.action_adresse_livraison:
                formPresenter.retrieveUserAction(item);
                break;

            case R.id.action_adresse_facturation:
                formPresenter.retrieveUserAction(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Display Home Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adresseTitle = findViewById(R.id.adresseTitle);
        nom = findViewById(R.id.form_adresse_nom);
        prenom = findViewById(R.id.form_adresse_prenom);
        adresse = findViewById(R.id.form_adresse_adresse);
        cp = findViewById(R.id.form_adresse_cp);
        ville = findViewById(R.id.form_adresse_ville);
        pays = findViewById(R.id.form_adresse_pays);
        btnEnregistrer = findViewById(R.id.form_btn_adresse_enregistrer);
        progressBar = findViewById(R.id.adresse_frag_progressBar);
    }

    @Override
    public void events() {
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNom = nom.getText().toString().trim();
                mPrenom = prenom.getText().toString().trim();
                mAdresse = adresse.getText().toString().trim();
                mCp = cp.getText().toString().trim();
                mVille = ville.getText().toString().trim();
                mPays = pays.getText().toString().trim();
                formPresenter.retrieveUserAction(v, mNom, mPrenom, mAdresse, mCp, mVille, mPays, mTypeAdresse);
            }
        });
    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void champNomObligatoire() {
        nom.setError(getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void champPrenomObligatoire() {
        prenom.setError(getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void champAdresseObligatoire() {
        adresse.setError(getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void champCodePostalObligatoire() {
        cp.setError(getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void champVilleObligatoire() {
        ville.setError(getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void champPaysObligatoire() {
        pays.setError(getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void displaySnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void enableDisableButton(boolean enable) {
        btnEnregistrer.setEnabled(enable);
    }

    @Override
    public void loadTitleFromData(String title) {
        mTypeAdresse = title.trim().split(" ")[title.trim().split(" ").length-1];
        adresseTitle.setText(title);
    }

    @Override
    public String retrieveTypeAdresse(){
        return mTypeAdresse;
    }

    @Override
    public void closeActivity() {
        this.finish();
    }
}
