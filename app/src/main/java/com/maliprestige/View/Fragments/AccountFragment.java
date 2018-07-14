package com.maliprestige.View.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.maliprestige.Model.Client;
import com.maliprestige.Presenter.AccountFrag.AccountFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.AccountFragView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements AccountFragView.IAccountFrag{

    // Ref widgets
    private TextView txtNomPrenom;
    private TextView txtEmail;
    private TextView txtTelephone;
    private Spinner adresseLivraison;
    private Spinner adresseFacturation;
    private LinearLayout progressBar;

    // Ref preseneter
    private AccountFragPresenter fragPresenter;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Load presenter data
        fragPresenter = new AccountFragPresenter(this);
        fragPresenter.loadAccountFragData(getActivity());
    }

    @Override
    public void initialize() {
        txtNomPrenom = getActivity().findViewById(R.id.txtNomPrenom);
        txtEmail = getActivity().findViewById(R.id.txtEmail);
        txtTelephone = getActivity().findViewById(R.id.txtTelephone);
        adresseLivraison = getActivity().findViewById(R.id.adresseLivraison);
        adresseFacturation = getActivity().findViewById(R.id.adresseFacturation);
        progressBar = getActivity().findViewById(R.id.account_frag_progressBar);
    }

    @Override
    public void events() {

    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void loadAdressesLivraisons(String[] livraisons) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, livraisons);
        adresseLivraison.setAdapter(adapter);
    }

    @Override
    public void loadAdressesFacturations(String[] facturations) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, facturations);
        adresseFacturation.setAdapter(adapter);
    }

    @Override
    public void loadIdentiteData(Client client) {
        txtNomPrenom.setText(client.getCivilite()+" "+client.getNom()+" "+client.getPrenom());
        txtEmail.setText(client.getEmail());
        txtTelephone.setText(client.getTelPort()+" / "+client.getTelFixe());
    }
}
