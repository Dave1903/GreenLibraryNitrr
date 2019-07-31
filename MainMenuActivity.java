package com.thedevlopershome.intranetnitrr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class MainMenuActivity extends AppCompatActivity {



    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        sharedPreferences=getSharedPreferences("userLogInData", Activity.MODE_PRIVATE);





        String name=sharedPreferences.getString("name","null");
        String firstTime=sharedPreferences.getString("firstTime","null");
        String mobileVerification=sharedPreferences.getString("mobileVerification","null");



             if(firstTime.equals("null")){
                 Intent intent=new Intent(MainMenuActivity.this,FirstTimeActivity.class);
                 startActivity(intent);

          }
             else
             if(mobileVerification.equals("null")){
                 Intent intent1=new Intent(MainMenuActivity.this,MobileVerificationActivity.class);
                 startActivity(intent1);
             }
           else
             if(name.equals("null")){
                 Intent intent2=new Intent(MainMenuActivity.this,LoginActivity.class);
                 startActivity(intent2);
        }

        CardView greenLibraryCard=(CardView)findViewById(R.id.greenLibraryCard);
        CardView notesCard=(CardView)findViewById(R.id.notesCard);
        greenLibraryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGreenLibraryCardClick(view);
            }
        });

        notesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNotesCardClick(view);
            }
        });

    }


    void onGreenLibraryCardClick(View v){
      //  v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_item));
         Intent intent =new Intent(MainMenuActivity.this,GreenLibraryActivity.class);
        startActivity(intent);


    }

       void onNotesCardClick(View v){
      //     v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_item));
        Intent intent =new Intent(MainMenuActivity.this,BranchListActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.aboutMe:
                Intent intent = new Intent(MainMenuActivity.this,AboutDevloperActivity.class);
                startActivity(intent);
                return true;

            case R.id.webSite:
                Uri uriUrl = Uri.parse("http://gogreennitrr.org/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
                return true;

            case R.id.RateUs:Uri marketUri = Uri.parse("market://details?id=" + "com.thedevlopershome.intranetnitrr");
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
                return  true;

            case R.id.feedback:
                String s="Debug-infos:";
                s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
                s += "\n OS API Level: "+android.os.Build.VERSION.RELEASE + "("+android.os.Build.VERSION.SDK_INT+")";
                s += "\n Device: " + android.os.Build.DEVICE;
                s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
                Intent intent1 = new Intent (Intent.ACTION_SEND);
                intent1.setType("message/rfc822");
                intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{"thedevelopershome@gmail.com,gogreennitrr@gmail.com"});
                intent1.putExtra(Intent.EXTRA_SUBJECT, "feedback for greenLibraryNitrr");
                intent1.putExtra(Intent.EXTRA_TEXT,s);
                intent1.setPackage("com.google.android.gm");
                if (intent1.resolveActivity(getPackageManager())!=null)
                    startActivity(intent1);
                else
                    Toast.makeText(this,"Gmail App is not installed",Toast.LENGTH_SHORT).show();
                return true;

            default:
                      return super.onOptionsItemSelected(item);


        }
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
        String name=sharedPreferences.getString("name","null");
        String firstTime=sharedPreferences.getString("firstTime","null");
        String mobileVerification=sharedPreferences.getString("mobileVerification","null");

        if(firstTime.equals("null")){
            Intent intent=new Intent(MainMenuActivity.this,FirstTimeActivity.class);
            startActivity(intent);

        }  else
        if(mobileVerification.equals("null")){
            Intent intent1=new Intent(MainMenuActivity.this,MobileVerificationActivity.class);
            startActivity(intent1);
        }
        else
        if(name.equals("null")){
            Intent intent=new Intent(MainMenuActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
