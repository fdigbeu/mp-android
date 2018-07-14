package com.maliprestige.View.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.maliprestige.Model.Panier;
import com.maliprestige.Model.Produit;
import com.maliprestige.Presenter.BasketFrag.BasketFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Activities.HomeActivity;
import com.maliprestige.View.Adapters.PanierRecyclerAdapter;
import com.maliprestige.View.Adapters.ProduitRecyclerAdapter;
import com.maliprestige.View.Interfaces.BasketFragView;
import com.maliprestige.View.Interfaces.HomeView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketFragment extends Fragment implements BasketFragView.IBasketFrag{

    // Ref HomeActivity interface
    private HomeView.IHome iHome;

    // Ref widgets
    private RecyclerView recyclerView;
    private LinearLayout progressBar;
    private TextView messagePanier;
    private CardView cardViewContent;
    private TextView montantTotal;
    private Button btnValiderPanier;

    private PanierRecyclerAdapter adapter;

    // Ref presenter
    private BasketFragPresenter fragPresenter;

    public BasketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Load presenter data
        fragPresenter = new BasketFragPresenter(this);
        fragPresenter.loadBasketFragData(getActivity());
    }

    @Override
    public void initialize() {
        recyclerView = getActivity().findViewById(R.id.panierRecyclerView);
        progressBar = getActivity().findViewById(R.id.panier_frag_progressBar);
        messagePanier = getActivity().findViewById(R.id.panier_message);
        btnValiderPanier = getActivity().findViewById(R.id.btn_valider_panier);
        cardViewContent = getActivity().findViewById(R.id.cardViewContent);
        montantTotal = getActivity().findViewById(R.id.montantTotal);
    }

    @Override
    public void events() {

    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void messagePanier(String message) {
        messagePanier.setText(message);
    }

    @Override
    public void validerPanierVisibility(int visibility) {
        cardViewContent.setVisibility(visibility);
    }

    @Override
    public void messageVisibility(int visibility) {
        messagePanier.setVisibility(visibility);
    }

    @Override
    public void loadRecyclerViewData(ArrayList<Panier> paniers, int numberColumns) {
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setHasFixedSize(true);
        adapter = new PanierRecyclerAdapter(getActivity(), paniers, this);
        recyclerView.setAdapter(adapter);
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
        ((HomeActivity)context).initialiseIBasketFrag(this);
    }
}
