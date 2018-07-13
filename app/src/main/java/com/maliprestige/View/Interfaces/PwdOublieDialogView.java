package com.maliprestige.View.Interfaces;

import android.content.Context;
import android.view.View;

public class PwdOublieDialogView {
    public interface IPwdOublieDialog{
        public void initialize();
        public void events();
        public void progressVisibility(int visibility);
        public void champEmailObligatoire();
        public void displaySnackBar(View view, String message);
        public void enableDisableButton(boolean enable);
        public void closeDialog();
    }

    // Presenter interface
    public interface IPresenter{
        public void onSendPwdOublieFormFinished(Context context, String returnCode);
    }
}
