package com.psp.home;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class Developers extends Activity{
       
   @Override      
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev); 
        ActionBar an=getActionBar();
        an.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_action));
        }
   
}