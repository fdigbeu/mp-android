package com.maliprestige.View.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.maliprestige.Model.Produit;
import com.maliprestige.Presenter.ManFrag.ManFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Activities.HomeActivity;
import com.maliprestige.View.Adapters.ProduitRecyclerAdapter;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.ManFragView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManFragment extends Fragment implements ManFragView.IManFrag{

    // Ref HomeActivity interface
    private HomeView.IHome iHome;

    private ManFragPresenter fragPresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProduitRecyclerAdapter adapter;


    public ManFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_man, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragPresenter = new ManFragPresenter(this);
        fragPresenter.loadManFragData(getActivity());
    }

    @Override
    public void initialize() {
        recyclerView = getActivity().findViewById(R.id.man_recyclerView);
        progressBar = getActivity().findViewById(R.id.man_progressBar);
    }

    @Override
    public void events() {

    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void loadRecyclerViewData(List<Produit> produits, int numberColumns) {
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setHasFixedSize(true);
        adapter = new ProduitRecyclerAdapter(getActivity(), (ArrayList<Produit>) produits, this);
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
        ((HomeActivity)context).initialiseIManFrag(this);
    }
}
