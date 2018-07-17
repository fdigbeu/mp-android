package com.maliprestige.View.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.maliprestige.Presenter.OrderSummary.OrderSummaryPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Activities.AdresseFormActivity;
import com.maliprestige.View.Activities.HomeActivity;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.OrderSummaryFragView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderSummaryFragment extends Fragment implements OrderSummaryFragView.IOrderSummaryFrag{

    // Ref HomeActivity interface
    private HomeView.IHome iHome;

    // Ref widgets
    private TextView dateLivraison;
    private TextView montantAPayer;
    private Spinner adresseLivraison;
    private Button ajouterAdresseLivraison;
    private Spinner adresseFacturation;
    private Button ajouterAdresseFacturation;
    private LinearLayout paiementPayal;
    private LinearLayout paiementMendatCash;
    private LinearLayout paiementVirement;
    private LinearLayout paiementEspece;
    private LinearLayout progressBar;
    private TextView messageRecap;
    private Button validerCommande;
    private Button annulerCommande;
    private ScrollView container;

    // Attributes
    private String mAdresseLivraison;
    private String mAdresseFacturation;
    private int mModePaiement;
    private String mLibelleDateLivraison;
    private String mMontant;
    private String mListeProduitsID;
    private String mListeProduitsQte;
    private String mListeProduitsPrix;

    // Ref presenter
    private OrderSummaryPresenter summaryPresenter;

    public OrderSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_summary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Load presenter data
        summaryPresenter = new OrderSummaryPresenter(this);
        summaryPresenter.loadOrderSummaryData(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        summaryPresenter.loadClientAdresse(getActivity());
        Log.i("TAG_CYCLE_VIE", "onResume()");
    }

    @Override
    public void initialize() {
        dateLivraison = getActivity().findViewById(R.id.sum_txtDateLivraison);
        montantAPayer = getActivity().findViewById(R.id.sum_txtMontantAPayer);
        adresseLivraison = getActivity().findViewById(R.id.sum_adresseLivraison);
        ajouterAdresseLivraison = getActivity().findViewById(R.id.btn_ajouter_newAdresse_livraison);
        adresseFacturation = getActivity().findViewById(R.id.sum_adresseFacturation);
        ajouterAdresseFacturation = getActivity().findViewById(R.id.btn_ajouter_newAdresse_facturation);
        paiementPayal = getActivity().findViewById(R.id.paypal_paiement);
        paiementMendatCash = getActivity().findViewById(R.id.mandat_cash_paiement);
        paiementVirement = getActivity().findViewById(R.id.virement_paiement);
        paiementEspece = getActivity().findViewById(R.id.espece_paiement);
        progressBar = getActivity().findViewById(R.id.sum_frag_progressBar);
        messageRecap = getActivity().findViewById(R.id.recapitulatif_message);
        validerCommande = getActivity().findViewById(R.id.btn_valider_commande);
        annulerCommande = getActivity().findViewById(R.id.btn_annuler_commande);
        container = getActivity().findViewById(R.id.sum_scrollView);
    }

    @Override
    public void events() {
        validerCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryPresenter.retrieveUserEntriedData(v, mLibelleDateLivraison, mModePaiement, mAdresseLivraison, mAdresseFacturation,
                        mMontant, mListeProduitsID, mListeProduitsPrix, mListeProduitsQte);
            }
        });
        annulerCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryPresenter.retrieveUserAction(v);
            }
        });
        ajouterAdresseFacturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryPresenter.retrieveUserAction(v);
            }
        });
        ajouterAdresseLivraison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryPresenter.retrieveUserAction(v);
            }
        });
        paiementPayal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryPresenter.retrieveUserAction(v);
            }
        });
        paiementMendatCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryPresenter.retrieveUserAction(v);
            }
        });
        paiementVirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryPresenter.retrieveUserAction(v);
            }
        });
        paiementEspece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryPresenter.retrieveUserAction(v);
            }
        });

    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void changeSubTotal(String subtotal) {
        mMontant = subtotal;
        montantAPayer.setText(subtotal);
    }

    @Override
    public void loadAdressesLivraisons(final String[] livraisons) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, livraisons);
        adresseLivraison.setAdapter(adapter);
        adresseLivraison.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdresseLivraison = livraisons[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void loadAdressesFacturations(final String[] facturations) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, facturations);
        adresseFacturation.setAdapter(adapter);
        adresseFacturation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdresseFacturation = facturations[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void containerVisibility(int visibility){
        container.setVisibility(visibility);
    }

    @Override
    public void changeBtnPaypalView() {
        paiementPayal.setBackgroundResource(R.drawable.progress_bar_hover_radius);
        paiementMendatCash.setBackgroundResource(R.drawable.progress_bar_radius);
        paiementVirement.setBackgroundResource(R.drawable.progress_bar_radius);
        paiementEspece.setBackgroundResource(R.drawable.progress_bar_radius);
        mModePaiement = 1;
    }

    @Override
    public void changeBtnMendatCashView() {
        paiementPayal.setBackgroundResource(R.drawable.progress_bar_radius);
        paiementMendatCash.setBackgroundResource(R.drawable.progress_bar_hover_radius);
        paiementVirement.setBackgroundResource(R.drawable.progress_bar_radius);
        paiementEspece.setBackgroundResource(R.drawable.progress_bar_radius);
        mModePaiement = 3;
    }

    @Override
    public void changeBtnVirementView() {
        paiementPayal.setBackgroundResource(R.drawable.progress_bar_radius);
        paiementMendatCash.setBackgroundResource(R.drawable.progress_bar_radius);
        paiementVirement.setBackgroundResource(R.drawable.progress_bar_hover_radius);
        paiementEspece.setBackgroundResource(R.drawable.progress_bar_radius);
        mModePaiement = 2;
    }

    @Override
    public void changeBtnEspeceView() {
        paiementPayal.setBackgroundResource(R.drawable.progress_bar_radius);
        paiementMendatCash.setBackgroundResource(R.drawable.progress_bar_radius);
        paiementVirement.setBackgroundResource(R.drawable.progress_bar_radius);
        paiementEspece.setBackgroundResource(R.drawable.progress_bar_hover_radius);
        mModePaiement = 5;
    }

    @Override
    public void launchAdresseForm(String typeAdresse) {
        Intent intent = new Intent(getActivity(), AdresseFormActivity.class);
        intent.putExtra("typeAdresse", typeAdresse);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void loadListeProduitsId(String liste) {
        mListeProduitsID = liste;
    }

    @Override
    public void loadListeProduitsQte(String liste) {
        mListeProduitsQte = liste;
    }

    @Override
    public void loadListeProduitsPrix(String liste) {
        mListeProduitsPrix = liste;
    }

    @Override
    public void enableDisableButton(boolean enable) {
        adresseFacturation.setEnabled(enable);
        ajouterAdresseFacturation.setEnabled(enable);
        validerCommande.setEnabled(enable);
        annulerCommande.setEnabled(enable);
    }

    @Override
    public int retrieveModePaiement() {
        return mModePaiement;
    }

    @Override
    public void messageVisibility(int visibility) {
        messageRecap.setVisibility(visibility);
    }

    @Override
    public void messageSummary(String message) {
        messageRecap.setText(message);
    }

    @Override
    public void changeDateLivraison(String message) {
        dateLivraison.setText(message);
        mLibelleDateLivraison = message;
    }

    @Override
    public HomeView.IHome retrieveIHomeInstance(){
        return iHome;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = getActivity();
        iHome =(HomeView.IHome) context;
        ((HomeActivity)context).initialiseIOrderSummaryFrag(this);
    }

    @Override
    public void onDetach() {
        summaryPresenter.countDownTimeCancel();
        super.onDetach();
    }
}
