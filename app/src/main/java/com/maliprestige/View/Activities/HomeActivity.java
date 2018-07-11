package com.maliprestige.View.Activities;

import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Slide;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Adapters.HomePagerAdapter;
import com.maliprestige.View.Fragments.AccountFragment;
import com.maliprestige.View.Fragments.BasketFragment;
import com.maliprestige.View.Fragments.ChildFragment;
import com.maliprestige.View.Fragments.ConnectionFragment;
import com.maliprestige.View.Fragments.ExoticFragment;
import com.maliprestige.View.Fragments.HomeFragment;
import com.maliprestige.View.Fragments.InscriptionFragment;
import com.maliprestige.View.Fragments.ManFragment;
import com.maliprestige.View.Fragments.OrderFragment;
import com.maliprestige.View.Fragments.WomanFragment;
import com.maliprestige.View.Interfaces.ChildFragView;
import com.maliprestige.View.Interfaces.ConnectionFragView;
import com.maliprestige.View.Interfaces.ExoticFragView;
import com.maliprestige.View.Interfaces.HomeFragView;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.InscriptionFragView;
import com.maliprestige.View.Interfaces.ManFragView;
import com.maliprestige.View.Interfaces.WomanFragView;
import com.maliprestige.View.ViewPagers.HomeViewPager;

import java.util.ArrayList;
import java.util.Hashtable;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeView.IHome {

    // Ref interfaces of fragments
    private HomeFragView.IHomeFrag iHomeFrag;
    private ManFragView.IManFrag iManFrag;
    private WomanFragView.IWomanFrag iWomanFrag;
    private ChildFragView.IChildFrag iChildFrag;
    private ExoticFragView.IExoticFrag iExoticFrag;
    private InscriptionFragView.IInscriptionFrag iInscriptionFrag;
    private ConnectionFragView.IConnectionFrag iConnectionFrag;

    // Widgets
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TextView homeTitle;

    // Ref ViewPager
    private HomePagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragments;
    private HomeViewPager homeViewPager;

    // Presenter
    private HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Loading of presenter
        homePresenter = new HomePresenter(this);
        homePresenter.loadHomeData(HomeActivity.this);
    }

    @Override
    public void onBackPressed() {
        homePresenter.openOrCloseMenuDrawer(drawer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        homePresenter.retrieveUserAction(HomeActivity.this, item);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        homePresenter.retrieveUserAction(HomeActivity.this, item);
        //--
        homePresenter.openOrCloseMenuDrawer(drawer);
        return true;
    }

    @Override
    public void initialize() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeTitle = findViewById(R.id.homeTitle);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);

        // HomeViewPager contents
        fragments = new ArrayList<>();
        fragments.add(Fragment.instantiate(this, HomeFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ManFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, WomanFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ChildFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ExoticFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, AccountFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, OrderFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, BasketFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ConnectionFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, InscriptionFragment.class.getName()));

        pagerAdapter  = new HomePagerAdapter(super.getSupportFragmentManager(), fragments);

        homeViewPager = findViewById(R.id.homeViewPager);
        homeViewPager.setAdapter(pagerAdapter);
        homeViewPager.setPagingEnabled(false);
    }

    @Override
    public void events() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void notifyUserIsConnected(boolean response) {
        navigationView.getMenu().setGroupVisible(R.id.groupe_compte, response);
        navigationView.getMenu().setGroupVisible(R.id.groupe_no_compte, !response);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void changeHomeView(int position, String title) {
        homeViewPager.setCurrentItem(position);
        homeTitle.setText(title);
    }

    @Override
    public void checkedNavigationView(int menuItemId) {
        navigationView.setCheckedItem(menuItemId);
    }

    // Initialisation of ref interafce
    public void initialiseIHomeFrag(HomeFragView.IHomeFrag iHomeFrag){ this.iHomeFrag = iHomeFrag; }
    public void initialiseIManFrag(ManFragView.IManFrag iManFrag){ this.iManFrag = iManFrag; }
    public void initialiseIWomanFrag(WomanFragView.IWomanFrag iWomanFrag){ this.iWomanFrag = iWomanFrag; }
    public void initialiseIChildFrag(ChildFragView.IChildFrag iChildFrag){ this.iChildFrag = iChildFrag; }
    public void initialiseIExoticFrag(ExoticFragView.IExoticFrag iExoticFrag){ this.iExoticFrag = iExoticFrag; }
    public void initialiseIInscriptionFrag(InscriptionFragView.IInscriptionFrag iInscriptionFrag){ this.iInscriptionFrag = iInscriptionFrag; }
    public void initialiseIConnectionFrag(ConnectionFragView.IConnectionFrag iConnectionFrag){ this.iConnectionFrag = iConnectionFrag; }

    // Persist slides data
    private ArrayList<Slide> slides;
    @Override
    public void persistSlides(ArrayList<Slide> slides){ this.slides = slides; }
    @Override
    public ArrayList<Slide> retrievePersistSlides(){ return this.slides; }

    // Persist produits data
    private Hashtable<String, ArrayList<Produit>> listeProduits = new Hashtable<>();
    @Override
    public void persistProduits(String key, ArrayList<Produit> produits){ this.listeProduits.put(key, produits); }
    @Override
    public ArrayList<Produit> retrievePersistProduits(String key){ return this.listeProduits.get(key); }
}
