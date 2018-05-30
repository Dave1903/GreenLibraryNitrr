package com.thedevlopershome.intranetnitrr;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutDevloperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_devloper);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("DAVINDER SINGH");
        TextView placeDetail = (TextView) findViewById(R.id.devlopersDetail);
        placeDetail.setText("I design with Code. \n" +
                "Pretentious? Let me tone that down for you.\n" +
                "I'm Davinder, currently an Undergrad student, taking strides in the field of Application development. \n" +
                "With a passion for Programming and a knack for solving problems with Code, I'm looking to make a career out of this! \n" +
                "See you in my next App and the others that come thereafter, Peace!");
        ImageView placePicutre = (ImageView) findViewById(R.id.image);
        placePicutre.setImageResource(R.drawable.nit_raipur );
        ImageView fbButton=(ImageView)findViewById(R.id.facebookButton);
        ImageView lnButton=(ImageView)findViewById(R.id.lnButton);
    }


    public void fbButtonClick(View view){
        Uri uriUrl = Uri.parse("https://www.facebook.com/profile.php?id=100003699012025");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    public void onLnButonClick(View v){
        Uri uriUrl = Uri.parse("https://www.linkedin.com/in/davinder-singh-34003b13a/");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
