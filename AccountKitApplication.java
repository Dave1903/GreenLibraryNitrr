package com.thedevlopershome.intranetnitrr;


import android.app.Application;

import com.facebook.accountkit.AccountKit;

public class AccountKitApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AccountKit.initialize(getApplicationContext());
    }

}
