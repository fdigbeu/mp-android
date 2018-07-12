package com.maliprestige.Presenter.Home;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.maliprestige.Model.DAOProduit;
import com.maliprestige.Model.Produit;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.ChildFragView;
import com.maliprestige.View.Interfaces.ExoticFragView;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.ManFragView;
import com.maliprestige.View.Interfaces.WomanFragView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetAllProduits extends AsyncTask<Void, Void, ArrayList<Produit>> {

    private ArrayList<Produit> produits;
    // Ref attributes
    private String typeProduit;
    private String limitProduit;
    private Context context;
    private HttpURLConnection urlConnection;
    private String produitUrl;
    // Ref interfaces attributes
    private HomeView.IPresenter homePresenter;
    private ManFragView.IPresenter manPresenter;
    private WomanFragView.IPresenter womanPresenter;
    private ChildFragView.IPresenter childPresenter;
    private ExoticFragView.IPresenter exoticPresenter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Produit> doInBackground(Void... voids) {

        produits = new ArrayList<>();

        StringBuilder responseHttp =  new StringBuilder();

        try {
            URL url = new URL(produitUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(8000);
            urlConnection.setConnectTimeout(8000);

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null){
                responseHttp.append(line);
            }

        }
        catch (Exception e) {
            DAOProduit daoProduit = new DAOProduit(context);
            produits = daoProduit.getAllBy(typeProduit);
            return produits;
        }
        finally {
            urlConnection.disconnect();
        }

        try {
            JSONArray results = new JSONArray(responseHttp.toString());
            for (int i = 0; i < results.length(); i++){
                JSONObject jsonObject = results.getJSONObject(i);
                Produit produit = new Produit();
                produit.setProduitId(jsonObject.getInt("id"));
                produit.setNom(jsonObject.getString("nom"));
                produit.setDescrition(jsonObject.getString("descrition"));
                produit.setPrixUnitaire(Float.parseFloat(jsonObject.getString("prixUnitaire")));
                produit.setPrixUnitaireTtc(Float.parseFloat(jsonObject.getString("prixUnitaireTtc")));
                produit.setPrixUnitaireGros(Float.parseFloat(jsonObject.getString("prixUnitaireGros")));
                produit.setPrixExpedition(Float.parseFloat(jsonObject.getString("prixExpedition")));
                produit.setImage1(jsonObject.getString("image1"));
                produit.setImage2(jsonObject.getString("image2"));
                produit.setImage3(jsonObject.getString("image3"));
                produit.setDate(jsonObject.getString("date"));
                produit.setQuantite(jsonObject.getInt("quantite"));
                produit.setNouveaute(jsonObject.getBoolean("isNouveaute"));
                produit.setReduction(jsonObject.getBoolean("isReduction"));
                produit.setPrixReduction(Float.parseFloat(jsonObject.getString("prixReduction")));
                produit.setPrixReductionTtc(Float.parseFloat(jsonObject.getString("prixReductionTtc")));
                produit.setDateReduction(jsonObject.getString("dateReduction"));
                produit.setDelaiJourMin(jsonObject.getInt("delaiJourMin"));
                produit.setDelaiJourMax(jsonObject.getInt("delaiJourMax"));
                produit.setDateFinReduction(jsonObject.getString("dateFinReduction"));
                produit.setTailleStandard(jsonObject.getBoolean("isTailleStandard"));
                produit.setModeEmploi(jsonObject.getString("modeEmploi"));
                produit.setTypeCoupeId(jsonObject.getInt("typeCoupeId"));
                produit.setTypeCoupeLibelle(jsonObject.getString("typeCoupeLibelle"));
                produit.setTypeProduitId(jsonObject.getInt("typeProduitId"));
                produit.setTypeProduitLibelle(jsonObject.getString("typeProduitLibelle"));
                produit.setMatiereId(jsonObject.getInt("matiereId"));
                produit.setMatiereLibelle(jsonObject.getString("matiereLibelle"));
                produit.setTypeMancheId(jsonObject.getInt("typeMancheId"));
                produit.setTypeMancheLibelle(jsonObject.getString("typeMancheLibelle"));
                produits.add(produit);
            }
        }
        catch (JSONException ex) {
            Log.e("TAG_ERREUR", "GetAllProduits()->doInBackground("+typeProduit+") : "+ex.getMessage());
            DAOProduit daoProduit = new DAOProduit(context);
            produits = daoProduit.getAllBy(typeProduit);
            return produits;
        }

        return produits;
    }

    @Override
    protected void onPostExecute(ArrayList<Produit> produits) {
        super.onPostExecute(produits);
        //--
        if(homePresenter != null) homePresenter.onLoadProduitsFinished(context, produits);
        if(manPresenter != null) manPresenter.onLoadProduitsFinished(context, produits);
        if(womanPresenter != null) womanPresenter.onLoadProduitsFinished(context, produits);
        if(childPresenter != null) childPresenter.onLoadProduitsFinished(context, produits);
        if(exoticPresenter != null) exoticPresenter.onLoadProduitsFinished(context, produits);
    }

    public void initialize(Context context, String typeProduit, String limitProduit){
        this.context = context;
        this.typeProduit = typeProduit;
        this.limitProduit = limitProduit;
        produitUrl = context.getResources().getString(R.string.mp_json_hote_production);
        produitUrl += context.getResources().getString(R.string.mp_json_produit)
                .replace("{TYPE_PRODUIT}", typeProduit)
                .replace("{LIMIT_PRODUIT}", limitProduit);
        Log.i("TAG_URL_REQUEST", ""+produitUrl);
    }

    public void setHomePresenter(HomeView.IPresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    public void setManPresenter(ManFragView.IPresenter manPresenter) {
        this.manPresenter = manPresenter;
    }

    public void setWomanPresenter(WomanFragView.IPresenter womanPresenter) {
        this.womanPresenter = womanPresenter;
    }

    public void setChildPresenter(ChildFragView.IPresenter childPresenter) {
        this.childPresenter = childPresenter;
    }

    public void setExoticPresenter(ExoticFragView.IPresenter exoticPresenter) {
        this.exoticPresenter = exoticPresenter;
    }
}
