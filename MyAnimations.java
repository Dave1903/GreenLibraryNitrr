package com.thedevlopershome.intranetnitrr;


import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class MyAnimations {

    public MyAnimations(){

    }

    public Animation inFromRightAnimation(){
           Animation inFromRight=new TranslateAnimation(Animation.RELATIVE_TO_PARENT,+1.0f,
                  Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,0.0f,
                  Animation.RELATIVE_TO_PARENT,0.0f);
                  inFromRight.setDuration(240);
                  inFromRight.setInterpolator(new AccelerateInterpolator());
          return  inFromRight;


    }


    public Animation outToRightAnimation(){
        Animation outToLeft=new TranslateAnimation(Animation.RELATIVE_TO_PARENT,-1.0f,
                Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,0.0f,
                Animation.RELATIVE_TO_PARENT,0.0f);
                outToLeft.setDuration(240);
                outToLeft.setInterpolator(new AccelerateInterpolator());
        return  outToLeft;


    }




}
