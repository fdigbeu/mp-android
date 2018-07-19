package com.maliprestige.Presenter.Home;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maliprestige.Model.Client;
import com.maliprestige.Model.Commande;
import com.maliprestige.Model.Cryptage;
import com.maliprestige.Model.DAOClient;
import com.maliprestige.Model.DAOPanier;
import com.maliprestige.Model.DAOSearch;
import com.maliprestige.Model.JsonData;
import com.maliprestige.Model.Produit;
import com.maliprestige.Model.Screen;
import com.maliprestige.Model.Search;
import com.maliprestige.Model.Slide;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.HomeView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomePresenter implements HomeView.IPresenter{

    private HomeView.IHome iHome;

    private GetAllProduits produitsAsyntask;

    private static String MP_LAST_CLIENT_CONNECTED = "MP_LAST_CLIENT_CONNECTED";
    private static String MP_CLIENT_TOKEN = "MP_CLIENT_TOKEN";
    private static String MP_CLIENT_DECONNECTED = "MP_CLIENT_DECONNECTED";
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 1.6; en-us; GenericAndroidDevice) AppleWebKit/528.5+ (KHTML, like Gecko) Version/3.1.2 Mobile Safari/525.20.1";

    public HomePresenter(HomeView.IHome iHome) {
        this.iHome = iHome;
    }

    // Method to load home data
    public void loadHomeData(final Context context){
        try {
            if(iHome != null && context != null){
                iHome.initialize();
                iHome.events();

                int numberViewPager = iHome.retrieveNumberViewPager();

                String clientToken = HomePresenter.retrieveClientToken(context);
                if(clientToken != null && clientToken.length() >= 50){
                    DAOClient daoClient = new DAOClient(context);
                    Client client = daoClient.getInfo(clientToken);
                    String userNom = client.getCivilite()+" "+client.getNom();
                    String userEmail = client.getEmail();
                    int drawable = R.drawable.ic_photo_account;
                    iHome.notifyUserIsConnected(true, new String[]{""+drawable, userNom, userEmail});
                    // If order recapitulation
                    if(numberViewPager == 10){
                        DAOPanier daoPanier = new DAOPanier(context);
                        daoPanier.modifyByToken("MP_CLIENT_DECONNECTED", clientToken);
                        //--
                        showViewPager("recapitulatif");
                    }
                    else{
                        showViewPager(context.getResources().getString(R.string.lb_commande));
                    }
                }
                else{
                    int drawable = R.drawable.ic_photo_inconnu;
                    String userNom = context.getResources().getString(R.string.lb_who_are_you);
                    String userEmail = context.getResources().getString(R.string.lb_identify_you);
                    iHome.notifyUserIsConnected(false, new String[]{""+drawable, userNom, userEmail});
                    // Home : default
                    iHome.changeHomeView(numberViewPager, context.getResources().getString(R.string.lb_accueil));
                    // Checked home menu in navigation view
                    iHome.checkedNavigationView(R.id.nav_menu_accueil);
                }
                //--
                iHome.searchVisibility(View.GONE);
                iHome.fabSearchVisibility(View.GONE);
                // Verify if Search contains data
                DAOSearch daoSearch = new DAOSearch(context);
                ArrayList<Search> searches = daoSearch.getAll();
                if(searches != null && searches.size() > 0){
                    ArrayList<String> searchList = new ArrayList<>();
                    for (int i=0; i<searches.size(); i++){ searchList.add(searches.get(i).getNomProduit().trim()); }
                    iHome.loadAutoCompleteData(searchList);
                    iHome.fabSearchVisibility(View.VISIBLE);
                }
                // Search new data if connection exists
                if(HomePresenter.isMobileConnected(context)){
                    produitsAsyntask = new GetAllProduits();
                    produitsAsyntask.setHomePresenter(this);
                    produitsAsyntask.initialize(context, "all", "1000");
                    produitsAsyntask.execute();
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->loadHomeData() : "+ex.getMessage());
        }
    }

    // Method to open or close drawer menu
    public void openOrCloseMenuDrawer(DrawerLayout drawer){
        try {
            if(drawer != null && iHome != null){
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    iHome.closeActivity();
                    cancelAsyntask();
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->openOrCloseMenuDrawer() : "+ex.getMessage());
        }
    }

    // Method to retrieve viewpager current item
    public int retrieveViewPagerCurrentItem(){
        try {
            if(iHome != null) {
                return iHome.retrieveViewPagerCurrentItem();
            }
        }
        catch (Exception ex){
            return -1;
        }
        return -1;
    }

    // Method to show view pager
    public void showViewPager(String titreSlide){
        try {
            if(iHome != null && titreSlide != null){
                String tabTitre[] = titreSlide.split( " ");
                String typeProduit = tabTitre.length >= 2 ? tabTitre[1] : tabTitre[0];
                switch (typeProduit.toLowerCase()){
                    case "accueil": // Accueil
                        iHome.changeHomeView(0, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_menu_accueil);
                        break;

                    case "hommes": // Vêtements hommes
                        iHome.changeHomeView(1, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_vetement_homme);
                        break;

                    case "femmes": // Vêtements femmes
                        iHome.changeHomeView(2, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_vetement_femme);
                        break;

                    case "enfants": // Vêtements enfants
                        iHome.changeHomeView(3, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_vetement_enfant);
                        break;

                    case "exotiques": // Produits exotiques
                        iHome.changeHomeView(4, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_produit_exotique);
                        break;

                    case "compte": // Mon compte
                        iHome.changeHomeView(5, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_mon_compte);
                        break;

                    case "commandes": // Mes commandes
                        iHome.changeHomeView(6, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_mes_commandes);
                        break;

                    case "connecter": // Se connecter
                        iHome.changeHomeView(8, titreSlide);
                        iHome.checkedNavigationView(R.id.nav_connexion);
                        break;

                    case "inscription": // S'inscrire
                        iHome.changeHomeView(9, "S'inscrire");
                        iHome.checkedNavigationView(R.id.nav_inscription);
                        break;

                    case "recapitulatif": // Récapitulatif de ma commande
                        iHome.changeHomeView(10, "Récapitulatif de ma commande");
                        iHome.checkedNavigationView(R.id.nav_recapitulatif);
                        break;

                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->showViewPager() : "+ex.getMessage());
        }
    }

    // Method to manage user action
    public void retrieveUserAction(View view, String value){
        try {
            if(iHome != null && view != null){
                int id = view.getId();
                switch (id){
                    case R.id.imageViewClose:
                        String data = iHome.retrieveSearchData();
                        if(data.length()==0)
                            iHome.searchVisibility(View.GONE);
                        else
                            iHome.changeSearchData("");
                        break;

                    case R.id.autoCompletSearch:
                        String itemSelected = value;
                        messageSnackBar(view, itemSelected);
                        break;

                    case R.id.fab_search:
                        iHome.searchVisibility(View.VISIBLE);
                        break;
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrieveUserAction() : "+ex.getMessage());
        }
    }

    // Method to manage user action
    public void retrieveUserAction(Context context, MenuItem item){
        try {
            if(iHome != null && item != null && context != null){
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_menu_accueil:
                        iHome.changeHomeView(0, context.getResources().getString(R.string.lb_accueil));
                        break;

                    case R.id.nav_vetement_homme:
                        iHome.changeHomeView(1, context.getResources().getString(R.string.lb_homme));
                        break;

                    case R.id.nav_vetement_femme:
                        iHome.changeHomeView(2, context.getResources().getString(R.string.lb_femme));
                        break;

                    case R.id.nav_vetement_enfant:
                        iHome.changeHomeView(3, context.getResources().getString(R.string.lb_enfant));
                        break;

                    case R.id.nav_produit_exotique:
                        iHome.changeHomeView(4, context.getResources().getString(R.string.lb_exotique));
                        break;

                    case R.id.nav_mon_compte:
                        iHome.changeHomeView(5, context.getResources().getString(R.string.lb_compte));
                        iHome.checkedNavigationView(R.id.nav_mon_compte);
                        break;

                    case R.id.nav_mes_commandes:
                        iHome.changeHomeView(6, context.getResources().getString(R.string.lb_commande));
                        iHome.checkedNavigationView(R.id.nav_mes_commandes);
                        break;

                    case R.id.nav_mon_panier:
                    case R.id.action_shopping:
                        iHome.changeHomeView(7, context.getResources().getString(R.string.lb_shopping));
                        if(id == R.id.action_shopping){
                            iHome.checkedNavigationView(R.id.nav_mon_panier);
                        }
                        break;

                    case R.id.nav_connexion:
                        iHome.changeHomeView(8, context.getResources().getString(R.string.lb_connexion));
                        break;

                    case R.id.nav_inscription:
                        iHome.changeHomeView(9, context.getResources().getString(R.string.lb_inscription));
                        break;

                    case R.id.nav_recapitulatif:
                        iHome.changeHomeView(10, context.getResources().getString(R.string.lb_recapitulatif_cmd));
                        iHome.checkedNavigationView(R.id.nav_recapitulatif);
                        break;

                    case R.id.nav_partager_app:
                        shareApp(context);
                        break;

                    case R.id.nav_envoyer_mail:
                        sendMessage(context);
                        break;

                    case R.id.nav_deconnexion:
                        if(isMobileConnected(context)){
                            iHome.progressBarVisibility(View.VISIBLE);
                            String clientToken = HomePresenter.retrieveClientToken(context);
                            HashMap<String, String> postDataParams = new HashMap<>();
                            postDataParams.put("token", clientToken);
                            //--
                            String actionForm = context.getResources().getString(R.string.mp_json_hote_production) + context.getResources().getString(R.string.mp_json_client_deconnexion);
                            SendFormData deconnectionForm = new SendFormData();
                            deconnectionForm.setiHomePresenter(this);
                            deconnectionForm.initializeData(context, postDataParams, actionForm);
                            deconnectionForm.execute();
                        }
                        else{
                            Toast.makeText(context, context.getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                        }
                        break;

                    case R.id.nav_livraison:
                        String urlLivraison = context.getResources().getString(R.string.mp_json_hote_production)+
                                context.getResources().getString(R.string.mp_livraison_link);
                        iHome.launchWebHtmlActivity(urlLivraison);
                        break;

                    case R.id.nav_qui_sommes_nous:
                        String urlWhois = context.getResources().getString(R.string.mp_json_hote_production)+
                                context.getResources().getString(R.string.mp_whois_link);
                        iHome.launchWebHtmlActivity(urlWhois);
                        break;

                    case R.id.nav_conditions_ventes:
                        String urlCGV = context.getResources().getString(R.string.mp_json_hote_production)+
                                context.getResources().getString(R.string.mp_cgv_link);
                        iHome.launchWebHtmlActivity(urlCGV);
                        break;

                    case R.id.nav_app_update:
                        verifyAppUpdate(context);
                        break;
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrieveUserAction() : "+ex.getMessage());
        }
    }

    // Verify update
    private void verifyAppUpdate(Context context){
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.android.vending");
            // package name and activity
            ComponentName comp = new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity");
            intent.setComponent(comp);
            String marketLink = context.getResources().getString(R.string.google_market_link);
            intent.setData(Uri.parse(marketLink));
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->verifyAppUpdate() : "+ex.getMessage());
        }
    }

    // Launch WebHtmlActivity
    public void launchWebHtmlActivity(String url){
        try {
            if(iHome != null) {
                iHome.launchWebHtmlActivity(url);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->launchWebHtmlActivity() : "+ex.getMessage());
        }
    }

    public void launchProduitDetail(Produit produit){
        try {
            if(iHome != null) {
                iHome.launchProduitDetail(produit);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->launchProduitDetail() : "+ex.getMessage());
        }
    }

    // Persist slides data
    public void persistSlides(ArrayList<Slide> slides){
        try {
            if (iHome != null && slides != null) {
                iHome.persistSlides(slides);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->persistSlides() : "+ex.getMessage());
        }
    }

    // Retrieve persist slides data
    public ArrayList<Slide> retrievePersistSlides(){
        try {
            if (iHome != null) {
                return iHome.retrievePersistSlides();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrieveSlides() : "+ex.getMessage());
        }
        return null;
    }

    // Persist number view page
    public void persistNumberViewPager(int numberViewPager){
        try {
            if (iHome != null) {
                iHome.persistNumberViewPager(numberViewPager);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->persistNumberViewPager() : "+ex.getMessage());
        }
    }

    // Retrieve number viewpager
    public int retrieveNumberViewPager(){
        try {
            if (iHome != null) {
                return iHome.retrieveNumberViewPager();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrieveNumberViewPager() : "+ex.getMessage());
        }
        return 0;
    }

    // Persist produits data
    public void persistProduits(String key, ArrayList<Produit> produits){
        try {
            if (iHome != null && produits != null) {
                iHome.persistProduits(key, produits);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->persistProduits() : "+ex.getMessage());
        }
    }

    // Retrieve persist produits data
    public ArrayList<Produit> retrievePersistProduits(String key){
        try {
            if (iHome != null) {
                return iHome.retrievePersistProduits(key);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrievePersistProduits() : "+ex.getMessage());
        }
        return null;
    }

    // Persist orders data
    public void persistCommandes(ArrayList<Commande> commandes){
        try {
            if (iHome != null && commandes != null) {
                iHome.persistCommandes(commandes);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->persistCommandes() : "+ex.getMessage());
        }
    }

    // Retrieve persist orders data
    public ArrayList<Commande> retrievePersistCommandes(){
        try {
            if (iHome != null) {
                return iHome.retrievePersistCommandes();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrievePersistCommandes() : "+ex.getMessage());
        }
        return null;
    }

    // Persist : Method to know if the fragment must to be refresh
    public void initializeRefreshFragment(String refresh) {
        try {
            iHome.initializeRefreshFragment(refresh);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->initializeRefreshFragment() : "+ex.getMessage());
        }
    }

    // Retrieve persist : Method to know if the fragment must to be refresh
    public String retrieveRefreshFragment() {
        try {
            return iHome.retrieveRefreshFragment();
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrieveRefreshFragment() : "+ex.getMessage());
        }
        return null;
    }

    // Method to show user connected menu
    public void showUserConnectedMenu(View view){
        if(iHome != null && view != null) {
            try {
                showUserConnectedPopupMenu(view);
            }
            catch (Exception ex){
                Log.e("TAG_ERROR", "HomePresenter-->showUserConnectedMenu() : "+ex.getMessage());
            }
        }
    }

    // Manage when user click
    private void showUserConnectedPopupMenu(final View view) {
        Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.CustomPopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.menu_user_connected, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                retrieveUserAction(view.getContext(), item);
                iHome.openOrCloseMenuDrawer();
                return true;
            }
        });
        popup.show();
    }

    // Get screen resolution
    public static Screen getScreenResolution(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new Screen(width, height);
    }

    public static boolean isMobileConnected(Context context){
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType) return true;
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean isMailValide(String emailStr)
    {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(emailStr);
        return matcher.find();
    }

    //Method to load html in textview
    public static void loadHtmlInTextView(TextView textView, String message){
        textView.setText(buildHtml(message));
    }

    public static Spanned buildHtml(String str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(str);
        }
    }

    // Method to retrieve last client connected
    public static String retrieveLastClientConnected(Context context){
        try { return getDataFromSharePreferences(context, MP_LAST_CLIENT_CONNECTED); }
        catch (Exception ex){
            return null;
        }
    }

    // Method to save last client connected
    public static void saveLastClientConnected(Context context, String jsonClient){
        try { saveDataInSharePreferences(context, MP_LAST_CLIENT_CONNECTED, jsonClient); }
        catch (Exception ex){ Log.e("TAG_ERROR", "HomePresenter-->saveLastClientConnected() : "+ex.getMessage()); }
    }

    // Method to retrieve client token
    public static String retrieveClientToken(Context context){
        try { return getDataFromSharePreferences(context, MP_CLIENT_TOKEN); }
        catch (Exception ex){
            return null;
        }
    }

    // Method to save client token
    public static void saveClientToken(Context context, String token){
        try { saveDataInSharePreferences(context, MP_CLIENT_TOKEN, token); }
        catch (Exception ex){ Log.e("TAG_ERROR", "HomePresenter-->saveClientToken() : "+ex.getMessage()); }
    }

    // Method to crypte data
    public static String crypterData(String data){
        Cryptage cryptage = new Cryptage();
        return cryptage.crypterData(data);
    }

    // Method to decrypt data
    public static String decrypterData(String data){
        Cryptage cryptage = new Cryptage();
        return cryptage.decrypterData(data);
    }

    // Method to display snackbar message
    public static void messageSnackBar(View view, String message){
        try {
            if(view != null && message != null){
                Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        catch (Exception ex){}
    }

    // Share apps
    public static void shareApp(Context context)
    {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String objet = context.getResources().getString(R.string.app_name);
            String message = context.getResources().getString(R.string.lb_message_partager_app);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, objet);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(Intent.createChooser(sendIntent, context.getResources().getString(R.string.lb_partager_avec)));
        }
        catch (Exception ex){}
    }

    // Method to load external page web
    public static void launchExternalPageWeb(Context context, String url){
        try {
            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->launchExternalPageWeb() : "+ex.getMessage());
        }
    }

    public static void sendMessage(Context context){
        String message = "";
        String email = context.getResources().getString(R.string.lb_contact_adresse_email);
        String objet = context.getResources().getString(R.string.lb_demande_renseignement);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, objet);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message.trim());
        context.startActivity(Intent.createChooser(emailIntent, context.getResources().getString(R.string.lb_selection_messagerie)));
    }

    public static void getNavDrawerDimension(Context context, View navDrawer){
        ViewGroup.LayoutParams params = navDrawer.getLayoutParams();
        Screen screen = getScreenResolution(context);
        int imgWidth = screen.getWidth() <= screen.getHeight() ? screen.getWidth() : screen.getHeight();
        params.width = (int)(imgWidth*0.95f);
        navDrawer.setLayoutParams(params);
    }

    // Method to find the delai date
    public static String getDelaiDateLivraison(int numberOfDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, numberOfDate);
        return sdf.format(c.getTime());
    }

    // Method to get data in share preferences
    private static String getDataFromSharePreferences(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    // Method to save data in share preferences
    private static void saveDataInSharePreferences(Context context, String key, String contentData){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, contentData);
            editor.commit(); // <=> editor.apply();
        }
        catch (Exception ex){}
    }

    @Override
    public void onLoadProduitsFinished(Context context, ArrayList<Produit> produits) {
        try {
            // Save produit in search data
            if(iHome != null && produits != null && produits.size() > 0){
                ArrayList<String> searchList = new ArrayList<>();
                DAOSearch daoSearch = new DAOSearch(context);
                daoSearch.deleteAll();
                for (int i=0; i<produits.size(); i++){
                    Search search = new Search();
                    search.setProduitId(produits.get(i).getProduitId());
                    search.setNomProduit(produits.get(i).getNom());
                    search.setImage1(produits.get(i).getImage1());
                    search.setImage2(produits.get(i).getImage2());
                    search.setImage3(produits.get(i).getImage3());
                    //--
                    daoSearch = new DAOSearch(context);
                    daoSearch.add(search);
                    //--
                    searchList.add(produits.get(i).getNom());
                }
                if(searchList.size() > 0){
                    iHome.loadAutoCompleteData(searchList);
                    iHome.fabSearchVisibility(View.VISIBLE);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERREUR", "HomePresenter->onLoadProduitsFinished() : "+ex.getMessage());
        }
    }

    @Override
    public void onUserDeconnectionFinished(Context context, String returnCode) {
        try {
            if(iHome != null){
                iHome.progressBarVisibility(View.GONE);
                String jsonString = returnCode != null ? returnCode.replace("null", "") : "";
                if(jsonString == null || jsonString.isEmpty()){
                    Toast.makeText(context, context.getResources().getString(R.string.lb_deconnexion_erreur), Toast.LENGTH_LONG).show();
                }
                else {
                    String clientToken = retrieveClientToken(context);
                    if(clientToken != null){
                        DAOClient daoClient = new DAOClient(context);
                        Client client = daoClient.getInfo(clientToken);
                        if(client != null){
                            saveLastClientConnected(context, client.toString());
                        }
                    }
                    saveClientToken(context, MP_CLIENT_DECONNECTED);
                    loadHomeData(context);
                    iHome.emptyPersistence();
                    Log.i("TAG_DECONNEXION_CODE", returnCode);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->onUserDeconnectionFinished() : "+ex.getMessage());
        }
    }

    public void cancelAsyntask(){
        try {
            if(produitsAsyntask != null){
                produitsAsyntask.cancel(true);
            }
        }
        catch (Exception ex){}
    }
}
