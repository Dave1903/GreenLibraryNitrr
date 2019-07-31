package com.thedevlopershome.intranetnitrr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.thefinestartist.Base;


public class FirstTimeActivity extends AppIntro {

      SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Base.initialize(getApplicationContext());
        sharedPreferences=getSharedPreferences("userLogInData",Activity.MODE_PRIVATE);

        addSlide(AppIntroFragment.newInstance("GREEN LIBRARY ","now register for green library books and materials online (One book per user)",R.drawable.firsttime,Color.parseColor("#222222")));
        addSlide(AppIntroFragment.newInstance("DIGITAL LIBRARY","Download and upload notes in pdf,doc/x,ppt/x format for different branches 24/7",R.drawable.dig,Color.parseColor("#222222")));
        addSlide(AppIntroFragment.newInstance("ENJOY","send us feedback to make this app better Thank You - go green committee NIT Raipur",R.drawable.gogreen,Color.parseColor("#222222")));




        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#222222"));
        setSeparatorColor(Color.parseColor("#222222"));


        showSkipButton(true);
        setProgressButtonEnabled(true);


        setVibrate(true);
        setVibrateIntensity(30);





    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("firstTime","true");
        editor.apply();
        Intent intent=new Intent(FirstTimeActivity.this,MainMenuActivity.class);
        startActivity(intent);
    }

    int APP_REQUEST_CODE=99;

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("firstTime","true");
        editor.apply();
        Intent intent=new Intent(FirstTimeActivity.this,MainMenuActivity.class);
        startActivity(intent);

    }





    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
