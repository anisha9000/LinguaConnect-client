package client.linguaconnect.com.linguaconnectclient.utils;

import android.app.ProgressDialog;
import android.content.Context;

import client.linguaconnect.com.linguaconnectclient.R;

/**
 * Created by anisha on 25/12/15.
 */
public class HandleProgressBar extends ProgressDialog {

    private ActivityListener listener;

    public HandleProgressBar(Context context) {
        super(context);
        this.setIndeterminate(true);
    }

    public HandleProgressBar(Context context,boolean isHorizontal) {
        super(context);
        if(!isHorizontal) {
            this.setIndeterminate(true);
        }

    }


    public void setControl() {
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    public void hideProgressBar() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

    public void showProgressBar() {
        if (!this.isShowing()) {
            this.show();
        }
    }

    @Override
    public void onBackPressed() {
        if(listener!=null) {
            listener.backPressedCalled();
        }
        super.onBackPressed();
    }

    public void setActivityListener(ActivityListener listener){
        this.listener=listener;
    }

    public static interface ActivityListener{
        void backPressedCalled();
    }

}
