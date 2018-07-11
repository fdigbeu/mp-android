package com.maliprestige.Presenter.AccountFrag;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.maliprestige.View.Interfaces.AccountFragView;

public class AccountFragPresenter{

    private AccountFragView.IAccountFrag iAccountFrag;

    public AccountFragPresenter(AccountFragView.IAccountFrag iAccountFrag) {
        this.iAccountFrag = iAccountFrag;
    }

    public void loadAccountFragData(Context context){
        try {
            if(iAccountFrag != null && context != null){
                iAccountFrag.initialize();
                iAccountFrag.events();
                iAccountFrag.progressVisibility(View.GONE);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "AccountFragPresenter-->loadAccountFragData() : "+ex.getMessage());
        }
    }
}
