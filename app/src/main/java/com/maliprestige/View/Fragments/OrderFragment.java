package com.maliprestige.View.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maliprestige.Model.Commande;
import com.maliprestige.Presenter.OrderFrag.OrderFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Activities.HomeActivity;
import com.maliprestige.View.Adapters.OrderRecyclerAdapter;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.OrderFragView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements OrderFragView.IOrderFrag{

    // Ref HomeActivity interface
    private HomeView.IHome iHome;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView messageOrder;
    private OrderFragPresenter fragPresenter;
    private RecyclerView recyclerView;
    private LinearLayout progressBar;
    private OrderRecyclerAdapter adapter;
    private View rootView;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_order, container, false);
        rootView = mView.getRootView();
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragPresenter = new OrderFragPresenter(this);
        fragPresenter.loadOrderFragData(rootView);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragPresenter.refreshFragmentData(getActivity());
        Log.i("TAG_RESUME", "refreshFragmentData(RESUME)");
    }

    @Override
    public void initialize() {
        recyclerView = getActivity().findViewById(R.id.order_recyclerView);
        progressBar = getActivity().findViewById(R.id.order_frag_progressBar);
        messageOrder = getActivity().findViewById(R.id.order_message_textView);
        swipeRefreshLayout = getActivity().findViewById(R.id.order_SwipeRefreshLayout);
        //rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    public void events() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragPresenter.swipeRefresFragmenthData(rootView);
            }
        });
    }

    @Override
    public void swipeRefreshVisibility(boolean visible){
        swipeRefreshLayout.setRefreshing(visible);
    }

    @Override
    public void progressVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void messageVisibility(int visibility) {
        messageOrder.setVisibility(visibility);
    }

    @Override
    public void messageOrder(String message) {
        messageOrder.setText(message);
    }

    @Override
    public void loadRecyclerViewData(ArrayList<Commande> commandes, int numberColumns) {
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setHasFixedSize(true);
        adapter = new OrderRecyclerAdapter(getActivity(), commandes, this);
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
        ((HomeActivity)context).initialiseIOrderFrag(this);
    }

    @Override
    public void onDetach() {
        fragPresenter.cancelAsyntask();
        super.onDetach();
    }
}
