package com.maliprestige.View.Interfaces;

import android.content.Context;
import android.view.View;

import com.maliprestige.Model.Panier;

import java.util.ArrayList;

public class OrderSummaryFragView {
    public interface IOrderSummaryFrag{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public HomeView.IHome retrieveIHomeInstance();
        public void changeSubTotal(String subtotal);
        public void loadAdressesLivraisons(String[] livraisons);
        public void loadAdressesFacturations(String[] facturations);
        public void messageVisibility(int visibility);
        public void messageSummary(String message);
        public void changeDateLivraison(String message);
        public void containerVisibility(int visibility);
        public void changeBtnPaypalView();
        public void changeBtnMendatCashView();
        public void changeBtnVirementView();
        public void changeBtnEspeceView();
        public void launchAdresseForm(String typeAdresse);
        public void displaySnackBar(View view, String message);
        public void loadListeProduitsId(String liste);
        public void loadListeProduitsQte(String liste);
        public void loadListeProduitsPrix(String liste);
        public void enableDisableButton(boolean enable);
        public int retrieveModePaiement();
    }

    // Presenter interface
    public interface IPresenter{
        public void onSendOrderFormFinished(Context context, String returnCode);
    }
}
