package com.psp.home;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
public class _kWh_Manager extends Activity{
    
	int ar[];
	String a;
    ListView _kwh_;
    @Override      
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kwh); 
        ActionBar an=getActionBar();
        an.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_action));
        Bundle extras = getIntent().getExtras();
        a = extras.getString("array");
        
         _kwh_=(ListView) findViewById(R.id.kwh_list);
    	
    	 final Button kwh1=(Button) findViewById(R.id.button1);
    	 final Button kwh2=(Button) findViewById(R.id.button2);
    	           
          ImageView info=(ImageView) findViewById(R.id.imageView2);
    	   info.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		infor();
        	  }     
           });     
    	     
        kwh1.setOnClickListener(new View.OnClickListener()
            { 
         	 public void onClick(View view) 
               {    
         		
         		kwh1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
         		kwh2.setBackgroundColor(0xFF33b5e5);
         	    kwh1.setTextColor(0xFF33b5e5);
         	    kwh2.setTextColor(0xFFFFFFFF);
         	    complete();
         	   }     
            });
           
         kwh2.setOnClickListener(new View.OnClickListener()
            { 
         	 public void onClick(View view) 
               {     
         		 
         		kwh2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        		 kwh1.setBackgroundColor(0xFF33b5e5);
        		kwh1.setTextColor(0xFFFFFFFF);
          		kwh2.setTextColor(0xFF33b5e5);
          		specific();
         	  }     
            });
         
         kwh1.performClick();
        }
    public void infor()
    {
    	
    	final Context mContext = this; 
    	final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.kwh_info);
    	dialog.setTitle("Information");
        dialog.show();
        RelativeLayout kw_in=(RelativeLayout) dialog.findViewById(R.id.kwh_info_pop);
        kw_in.setOnClickListener(new View.OnClickListener()
      { 
   	 public void onClick(View view) 
         {    dialog.dismiss();
   	  }     
      });
        
   	Button info=(Button) dialog.findViewById(R.id.bu1);
	   info.setOnClickListener(new View.OnClickListener()
     { 
  	 public void onClick(View view) 
        {    
  		infor2();
  	  }     
     });
        
    }
    
    public void infor2()
    {
    	final Context mContext = this; 
    	final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.unit_slab);
    	dialog.setTitle("Unit Slab");
        dialog.show();
                           
   	Button info=(Button) dialog.findViewById(R.id.button1);
	   info.setOnClickListener(new View.OnClickListener()
     {    
  	 public void onClick(View view) 
       {    
  		dialog.dismiss();
  	  }     
     });
           
    }
    public void shw(int f)
    {
    	final Context mContext = this; 
    	final Dialog dialog = new Dialog(mContext);
    	if(f==1) dialog.setContentView(R.layout.kwh_list);
    	if(f==2) dialog.setContentView(R.layout.kwh_sp_listinfo);
    	dialog.setTitle("Details");
        dialog.show();
        
        RelativeLayout kw_in_single=(RelativeLayout) dialog.findViewById(R.id.rel_kwh_singlelist);
        kw_in_single.setOnClickListener(new View.OnClickListener()
      { 
   	 public void onClick(View view) 
         {    dialog.dismiss();
   	  }     
      });
      }
    
    public void complete()
    {
    	_kwh_.setAdapter(new _List_Adapter(this,2,a));
    	_kwh_.setOnItemClickListener(new OnItemClickListener() {
 			public void onItemClick(AdapterView<?> parent, View view,
 					int position, long id) {
 			    
 				shw(1);
 			 }
 		});
    }
        
    public void specific()
    {
    	_kwh_.setAdapter(new _List_Adapter(this,3,a));
    	_kwh_.setOnItemClickListener(new OnItemClickListener() {
 			public void onItemClick(AdapterView<?> parent, View view,
 					int position, long id) {
 			    
 				shw(2);
 			}
 		});
    }
}