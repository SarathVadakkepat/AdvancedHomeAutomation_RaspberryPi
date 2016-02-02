package com.psp.home;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
 

public class _My_Home_2 extends Activity {
                     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_home_2); 
        ActionBar an=getActionBar();
        an.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_action));
        RelativeLayout bd1=(RelativeLayout) findViewById(R.id.relativeLayout2);
        bd1.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		 final Intent mainIntent = new Intent(_My_Home_2.this, _Rooms_2.class);
        		 _My_Home_2.this.startActivity(mainIntent);
        		
              }     
           });
        
        RelativeLayout hall=(RelativeLayout) findViewById(R.id.relativeLayout1);
        hall.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		 final Intent mainIntent = new Intent(_My_Home_2.this, _Rooms_2.class);
        		 _My_Home_2.this.startActivity(mainIntent);
        		  
              }     
           });
          
        RelativeLayout bd2=(RelativeLayout) findViewById(R.id.relativeLayout4);
        bd2.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		 final Intent mainIntent = new Intent(_My_Home_2.this, _Rooms_2.class);
        		 _My_Home_2.this.startActivity(mainIntent);
        		
              }     
           });     
          
        RelativeLayout b1=(RelativeLayout) findViewById(R.id.relativeLayout3);
        b1.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		 final Intent mainIntent = new Intent(_My_Home_2.this, _Rooms_2.class);
        		 _My_Home_2.this.startActivity(mainIntent);
        		
              }     
           });   
        
        RelativeLayout b2=(RelativeLayout) findViewById(R.id.relativeLayout6);
        b2.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		 final Intent mainIntent = new Intent(_My_Home_2.this, _Rooms_2.class);
        		 _My_Home_2.this.startActivity(mainIntent);
        		
              }     
           });
        
        RelativeLayout k=(RelativeLayout) findViewById(R.id.relativeLayout5);
        k.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		 final Intent mainIntent = new Intent(_My_Home_2.this, _Rooms_2.class);
        		 _My_Home_2.this.startActivity(mainIntent);
        		
              }     
           });
         
        }
       
}