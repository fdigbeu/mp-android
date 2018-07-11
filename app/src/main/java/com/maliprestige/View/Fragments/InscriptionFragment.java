package com.maliprestige.View.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.maliprestige.Presenter.InscriptionFrag.InscriptionFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.InscriptionFragView;

/**
 * A simple {@link Fragment} subclass.
 */
public class InscriptionFragment extends Fragment implements InscriptionFragView.IInscriptionFrag{

    // Ref widgets
    private Spinner civilite;
    private TextInputEditText nom;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText passwordConfirm;
    private TextView conditionVente;
    private Button btnEnregistrer;
    private ProgressBar progressBar;

    private String mCivilite;
    private String mNom;
    private String mEmail;
    private String mPassword;
    private String mConfirmPassword;

    // Ref presenter
    private InscriptionFragPresenter fragPresenter;

    public InscriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inscription, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Load data
        fragPresenter = new InscriptionFragPresenter(this);
        fragPresenter.loadInscriptionFragData(getActivity());
    }

    @Override
    public void initialize() {
        civilite = getActivity().findViewById(R.id.form_civilite);
        nom = getActivity().findViewById(R.id.form_nom);
        email = getActivity().findViewById(R.id.form_email);
        password = getActivity().findViewById(R.id.form_idPassword);
        passwordConfirm = getActivity().findViewById(R.id.form_idConfirmPassword);
        btnEnregistrer = getActivity().findViewById(R.id.form_btn_enregistrer);
        progressBar = getActivity().findViewById(R.id.inscription_progressBar);
        conditionVente = getActivity().findViewById(R.id.conditionVente);
    }

    @Override
    public void events() {
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNom = nom.getText().toString().trim();
                mEmail = email.getText().toString().trim();
                mPassword = password.getText().toString().trim();
                mConfirmPassword = passwordConfirm.getText().toString().trim();
                fragPresenter.retrieveFormData(v, mCivilite, mNom, mEmail, mPassword, mConfirmPassword);
            }
        });
        conditionVente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragPresenter.loadCGVPageWeb(getActivity());
            }
        });
    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void loadCiviliteData(final String[] civilites) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, civilites);
        civilite.setAdapter(adapter);
        civilite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCivilite = civilites[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void champNomObligatoire() {
        nom.setError(getActivity().getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void champEmailObligatoire() {
        email.setError(getActivity().getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void champPasswordObligatoire() {
        password.setError(getActivity().getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void champConfirmPasswordObligatoire() {
        passwordConfirm.setError(getActivity().getResources().getString(R.string.lb_champ_obligatoire));
    }

    @Override
    public void displaySnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
