package com.maliprestige.View.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.maliprestige.Model.Slide;
import com.maliprestige.Presenter.HomeFag.HomeFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Activities.HomeActivity;
import com.maliprestige.View.Adapters.SlideRecyclerAdapter;
import com.maliprestige.View.Interfaces.HomeFragView;
import com.maliprestige.View.Interfaces.HomeView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeFragView.IHomeFrag {

    // Ref HomeActivity interface
    private HomeView.IHome iHome;

    private HomeFragPresenter fragPresenter;
    private RecyclerView recyclerView;
    private LinearLayout progressBar;
    private SlideRecyclerAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragPresenter = new HomeFragPresenter(this);
        fragPresenter.loadHomeFragData(getActivity());
    }

    @Override
    public void initialize() {
        recyclerView = getActivity().findViewById(R.id.home_recyclerView);
        progressBar = getActivity().findViewById(R.id.home_frag_progressBar);
    }

    @Override
    public void events() {

    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void loadRecyclerViewData(List<Slide> slides, int numberColumns) {
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setHasFixedSize(true);
        adapter = new SlideRecyclerAdapter(getActivity(), (ArrayList<Slide>) slides, this);
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
        ((HomeActivity)context).initialiseIHomeFrag(this);
    }
}
