package com.maliprestige.Presenter.OrderFrag;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import com.maliprestige.Model.Commande;
import com.maliprestige.Model.CommandeProduit;
import com.maliprestige.Model.DAOCommande;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.OrderFragView;

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

public class GetAllOrders extends AsyncTask<Void, Void, ArrayList<Commande>> {

    private String clientToken;
    private ArrayList<Commande> commandes;
    // Ref attributes
    private Context context;
    private HttpURLConnection urlConnection;
    private String commandeUrl;
    // Ref interfaces attributes
    private OrderFragView.IPresenter orderFragPresenter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Commande> doInBackground(Void... voids) {

        commandes = new ArrayList<>();

        StringBuilder responseHttp =  new StringBuilder();

        try {
            URL url = new URL(commandeUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("User-Agent", HomePresenter.USER_AGENT);
            urlConnection.setRequestProperty("Connection", "close");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();
            // If connection failed
            if (urlConnection.getResponseCode() != 200) {
                DAOCommande daoCommande = new DAOCommande(context);
                commandes = daoCommande.getAllBy(clientToken);
                return commandes;
            }
            //--
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null){
                responseHttp.append(line);
            }

        }
        catch (Exception ex) {
            Log.e("TAG_ERREUR", "GetAllOrders->Exception(Ex) : "+ex.getMessage());
            DAOCommande daoCommande = new DAOCommande(context);
            commandes = daoCommande.getAllBy(clientToken);
            return commandes;
        }
        finally {
            urlConnection.disconnect();
        }

        try {
            JSONArray results = new JSONArray(responseHttp.toString());
            for (int i = 0; i < results.length(); i++){
                JSONObject jsonObject = results.getJSONObject(i);
                Commande commande = new Commande();
                commande.setNumeroCommande(jsonObject.getString("numeroCommande"));
                commande.setMontantCommande(Float.parseFloat(jsonObject.getString("montantCommande")));
                commande.setDateValidation(jsonObject.getString("dateValidation"));
                commande.setDateAnnulation(jsonObject.getString("dateAnnulation"));
                commande.setDateRetour(jsonObject.getString("dateRetour"));
                commande.setDateFournisseur(jsonObject.getString("dateFournisseur"));
                commande.setDateExpedition(jsonObject.getString("dateExpedition"));
                commande.setDateReception(jsonObject.getString("dateReception"));
                commande.setDateEnPreparation(jsonObject.getString("dateEnPreparation"));
                commande.setNumeroColis(jsonObject.getString("numeroColis"));
                commande.setDateAchat(jsonObject.getString("dateAchat"));
                commande.setStatutLibelle(jsonObject.getString("statutLibelle"));
                commande.setStatutDetail(jsonObject.getString("statutDetail"));
                commande.setLibelleDateLivraison(jsonObject.getString("libelleDateLivraison"));
                commande.setLibelleAdresseLivraison(jsonObject.getString("libelleAdresseLivraison"));
                commande.setLibelleAdresseFacturation(jsonObject.getString("libelleAdresseFacturation"));
                commande.setNumeroFacture(jsonObject.getString("numeroFacture"));
                commande.setModePaiement(jsonObject.getString("modePaiement"));
                commande.setMontantRegle(Float.parseFloat(jsonObject.getString("montantRegle")));
                commande.setFactureAcquittee(Boolean.parseBoolean(jsonObject.getString("isFactureAcquittee")));
                commande.setDateAcquittement(jsonObject.getString("dateAcquittement"));
                //--
                JSONArray resultItems = new JSONArray(jsonObject.getString("produitItems"));
                ArrayList<CommandeProduit> commandeProduits = new ArrayList<>();
                for(int j=0; j<resultItems.length(); j++){
                    JSONObject jsonObjectItem = resultItems.getJSONObject(j);
                    CommandeProduit commandeProduit = new CommandeProduit();
                    commandeProduit.setProduitId(jsonObjectItem.getInt("id"));
                    commandeProduit.setNomProduit(jsonObjectItem.getString("nom"));
                    commandeProduit.setPrixQteProduit(Float.parseFloat(jsonObjectItem.getString("prixQteProduit")));
                    commandeProduit.setQteProduit(Integer.parseInt(jsonObjectItem.getString("qteProduit")));
                    commandeProduit.setToken(clientToken);
                    commandeProduit.setNumeroCommande(jsonObject.getString("numeroCommande"));
                    commandeProduits.add(commandeProduit);
                }
                commande.setCommandeProduits(commandeProduits);
                //--
                commandes.add(commande);
            }
        }
        catch (JSONException ex) {
            Log.e("TAG_ERREUR", "GetAllOrders->JSONException(ex) : "+ex.getMessage());
            DAOCommande daoCommande = new DAOCommande(context);
            commandes = daoCommande.getAllBy(clientToken);
            return commandes;
        }

        return commandes;
    }

    @Override
    protected void onPostExecute(ArrayList<Commande> commandes) {
        super.onPostExecute(commandes);
        if(orderFragPresenter != null) orderFragPresenter.onLoadCommandesFinished(context, commandes);
    }

    public void initialize(Context context, String clientToken){
        this.context = context;
        this.clientToken = clientToken;
        commandeUrl = context.getResources().getString(R.string.mp_json_hote_production);
        commandeUrl += context.getResources().getString(R.string.mp_json_client_commande)
                .replace("{CLIENT_TOKEN}", clientToken);
        Log.i("TAG_URL_ORDER", commandeUrl);
    }

    public void setOrderFragPresenter(OrderFragView.IPresenter orderFragPresenter) {
        this.orderFragPresenter = orderFragPresenter;
    }
}
