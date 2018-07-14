package com.maliprestige.View.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maliprestige.Presenter.ConnectionFrag.ConnectionFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Activities.HomeActivity;
import com.maliprestige.View.Interfaces.ConnectionFragView;
import com.maliprestige.View.Interfaces.HomeView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionFragment extends Fragment implements ConnectionFragView.IConnectionFrag{

    // Ref HomeActivity interface
    private HomeView.IHome iHome;

    // Ref Widgets
    private TextInputEditText email;
    private TextInputEditText password;
    private Button btnConnexion;
    private LinearLayout progressBar;
    private TextView passwordOublie;
    private TextView pasDeCompte;

    private String mEmail;
    private String mPassword;
    private boolean mConnAutomatic;

    // Ref presenter
    private ConnectionFragPresenter fragPresenter;


    public ConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Load presenter data
        fragPresenter = new ConnectionFragPresenter(this);
        fragPresenter.loadConnectionFragData(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection, container, false);
    }

    @Override
    public void initialize() {
        email = getActivity().findViewById(R.id.form_connect_email);
        password = getActivity().findViewById(R.id.form_connect_idPassword);
        btnConnexion = getActivity().findViewById(R.id.form_btn_connection);
        progressBar = getActivity().findViewById(R.id.connection_frag_progressBar);
        passwordOublie = getActivity().findViewById(R.id.passwordOublie);
        pasDeCompte = getActivity().findViewById(R.id.pasDeCompte);
    }

    @Override
    public void events() {
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = email.getText().toString().trim();
                mPassword = password.getText().toString().trim();
                fragPresenter.retrieveFormData(v, mEmail, mPassword, true);
            }
        });
        passwordOublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragPresenter.passwordOublie(getActivity());
            }
        });
        pasDeCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragPresenter.createNewCompteClient();
            }
        });
    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
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
    public void displaySnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public HomeView.IHome retrieveIHomeInstance(){
        return iHome;
    }

    @Override
    public void rempliChampEmail(String adresseEmail) {
        email.setText(adresseEmail);
        password.setText("");
    }

    @Override
    public void enableDisableButton(boolean enable) {
        btnConnexion.setEnabled(enable);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = getActivity();
        iHome =(HomeView.IHome) context;
        ((HomeActivity)context).initialiseIConnectionFrag(this);
    }
}
