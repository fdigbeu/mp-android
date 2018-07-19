package com.maliprestige.View.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maliprestige.Model.Produit;
import com.maliprestige.Presenter.Diapo.DiapoPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.DiapoView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DiapoActivity extends AppCompatActivity implements DiapoView.IDiapo{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView diapoTitle;
    private Toolbar toolbar;
    private DiapoPresenter diapoPresenter;
    private int numberOfDiapo;
    private MenuItem menuItem;
    private TextView newProduit;
    private DiapoView.IPlaceholder iPlaceholder;
    private LinearLayout layoutPanier;
    private TextView prixProduit;
    private Button btnAjouterAuPanier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diapo);
        // Load diapo data
        diapoPresenter = new DiapoPresenter(this);
        diapoPresenter.loadDiapoData(DiapoActivity.this, this.getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_diapo, menu);
        menuItem = menu.findItem(R.id.action_diapo_pager);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                diapoPresenter.closeActivity();
                break;

            case R.id.action_diapo_pager:
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
        mViewPager = findViewById(R.id.container);
        diapoTitle = findViewById(R.id.diapoTitle);
        newProduit = findViewById(R.id.newProduit_textView);
        layoutPanier = findViewById(R.id.layout_ajouter_au_panier);
        prixProduit = findViewById(R.id.detail_prix_unitaire);
        btnAjouterAuPanier = findViewById(R.id.btn_ajouter_au_panier);
    }

    @Override
    public void events() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                diapoPresenter.changeDiapoNumber(position, mViewPager.getAdapter().getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        btnAjouterAuPanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diapoPresenter.retrieveUserAction(v);
            }
        });
    }

    @Override
    public void loadPlaceHolderFragment() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void setIplaceholderReference(DiapoView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    // Persist url diapos
    private ArrayList<String> urlDiapos;
    @Override
    public void persistDiapos(ArrayList<String> urlDiapos){ this.urlDiapos = urlDiapos; }
    @Override
    public ArrayList<String> retrievePersistDiapos(){ return this.urlDiapos; }

    @Override
    public void changeDiapoTitle(String title) {
        diapoTitle.setText(title);
    }

    @Override
    public void setNumberOfDiapoFinded(int number) {
        this.numberOfDiapo = number;
    }

    @Override
    public void feedDiapoPageNumber(String pageNumber) {
        menuItem.setTitle("IMAGE "+pageNumber);
    }

    private Produit produit = null;
    @Override
    public void persistProduit(Produit produit) { this.produit = produit; }
    @Override
    public Produit retrievePersistProduit() { return produit; }

    @Override
    public void layoutPanierVisibility(int visibility) {
        layoutPanier.setVisibility(visibility);
    }

    @Override
    public void changePrixValue(String value) {
        prixProduit.setText(value);
    }

    @Override
    public void changeNewProduitValue(String value) {
        newProduit.setText(value);
    }

    @Override
    public void newProduitVisibility(int visibility) {
        newProduit.setVisibility(visibility);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements DiapoView.IPlaceholder{

        private ImageView imageView;
        private DiapoView.IDiapo iDiapo;
        private int fragNumber;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_diapo, container, false);
            fragNumber = getArguments().getInt(ARG_SECTION_NUMBER)-1;
            // Load slides data
            DiapoPresenter diapoPresenter = new DiapoPresenter(this);
            diapoPresenter.loadPlaceholderData(rootView, fragNumber);

            return rootView;
        }

        @Override
        public void initialize(View view) {
            imageView = view.findViewById(R.id.diapo_imageview);
        }

        @Override
        public void events() {

        }

        @Override
        public void loadDiapoData(String urlImage, int width, int height) {
            Picasso.with(getActivity()).load(urlImage).memoryPolicy(MemoryPolicy.NO_CACHE).resize(width, height).into(imageView);
        }

        @Override
        public DiapoView.IDiapo retrieveIDiapoInstance() {
            return iDiapo;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            context = getActivity();
            iDiapo =(DiapoView.IDiapo) context;
            ((DiapoActivity)context).setIplaceholderReference(this);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return numberOfDiapo;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        diapoPresenter.cancelCountDownTimer();
    }
}
