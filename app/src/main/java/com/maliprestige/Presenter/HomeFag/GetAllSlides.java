package com.maliprestige.Presenter.HomeFag;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;

import com.maliprestige.Model.DAOSlide;
import com.maliprestige.Model.Slide;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.HomeFragView;

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

public class GetAllSlides  extends AsyncTask<Void, Void, ArrayList<Slide>> {

    private CountDownTimer downTimer;

    private ArrayList<Slide> slides;
    // Ref attributes
    private Context context;
    private HttpURLConnection urlConnection;
    private String slideUrl;
    // Ref interfaces attributes
    private HomeFragView.IPresenter homeFragPresenter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Slide> doInBackground(Void... voids) {

        slides = new ArrayList<>();

        StringBuilder responseHttp =  new StringBuilder();

        try {
            URL url = new URL(slideUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("User-Agent", HomePresenter.USER_AGENT);
            urlConnection.setRequestProperty("Connection", "close");
            urlConnection.setConnectTimeout(8000);
            urlConnection.setReadTimeout(8000);
            urlConnection.connect();

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null){
                responseHttp.append(line);
            }

        }
        catch (Exception e) {
            DAOSlide daoSlide = new DAOSlide(context);
            slides = daoSlide.getAll();
            return slides;
        }
        finally {
            urlConnection.disconnect();
        }

        try {
            JSONArray results = new JSONArray(responseHttp.toString());
            for (int i = 0; i < results.length(); i++){
                JSONObject jsonObject = results.getJSONObject(i);
                Slide slide = new Slide();
                slide.setTitre(jsonObject.getString("titre"));
                slide.setUrl(jsonObject.getString("url"));
                slides.add(slide);
            }
        }
        catch (JSONException e) {
            DAOSlide daoSlide = new DAOSlide(context);
            slides = daoSlide.getAll();
            return slides;
        }

        return slides;
    }

    @Override
    protected void onPostExecute(ArrayList<Slide> slides) {
        super.onPostExecute(slides);
        if(homeFragPresenter != null) homeFragPresenter.onLoadSlidesFinished(context, slides);
    }

    public void initialize(Context context){
        this.context = context;
        slideUrl = context.getResources().getString(R.string.mp_json_hote_production);
        slideUrl += context.getResources().getString(R.string.mp_json_slide);
    }

    public void setHomeFragPresenter(HomeFragView.IPresenter homeFragPresenter) {
        this.homeFragPresenter = homeFragPresenter;
    }
}
