package com.maliprestige.Presenter.OrderSummary;

import android.content.Context;
import android.os.AsyncTask;

import com.maliprestige.View.Interfaces.ConnectionFragView;
import com.maliprestige.View.Interfaces.HomeView;
import com.maliprestige.View.Interfaces.InscriptionFragView;
import com.maliprestige.View.Interfaces.OrderSummaryFragView;
import com.maliprestige.View.Interfaces.PwdOublieDialogView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SendOrderFormData extends AsyncTask<Void, Void, String> {
    private Context context;
    private  String codeRetour;
    private URL url;
    private String actionForm;
    private HttpURLConnection httpURLConnection;
    private HashMap<String, String> postDataParams;
    private OrderSummaryFragView.IPresenter iOrderSummaryPresenter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try{
            url = new URL(actionForm);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setReadTimeout(8000);
            httpURLConnection.setConnectTimeout(8000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line=br.readLine()) != null){
                    codeRetour+=line;
                }
            }
        }
        catch (Exception e)
        {
            return null;
        }

        httpURLConnection.disconnect();

        return codeRetour;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(iOrderSummaryPresenter != null) iOrderSummaryPresenter.onSendOrderFormFinished(context, s);
    }

    public void initializeData(Context context, HashMap<String, String> postDataParams, String actionForm){
        this.context = context;
        this.postDataParams = postDataParams;
        this.actionForm = actionForm;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder resultat = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if(first){
                first = false;
            }
            else {
                resultat.append("&");
            }
            resultat.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            resultat.append("=");
            resultat.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return resultat.toString();
    }

    public void setiOrderSummaryPresenter(OrderSummaryFragView.IPresenter iOrderSummaryPresenter) {
        this.iOrderSummaryPresenter = iOrderSummaryPresenter;
    }
}
