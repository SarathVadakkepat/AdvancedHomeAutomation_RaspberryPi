package com.psp.home;

  
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
  
public class MainActivity extends Activity {
                   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry); 
   	  Button solver=(Button)findViewById(R.id.button1);
            solver.setOnClickListener(new View.OnClickListener()
               { 
            	 public void onClick(View view) 
                  {             
            		    EditText login=(EditText) findViewById(R.id.editText1);
            	        EditText pswd=(EditText) findViewById(R.id.editText2);
            	        int log, pw;
            	        log=(login.getText().toString().length()!=0?Integer.parseInt(login.getText().toString()):Integer.parseInt("0"));
            	        pw=(pswd.getText().toString().length()!=0?Integer.parseInt(pswd.getText().toString()):Integer.parseInt("0"));
            	        
            	       if(log==1&&pw==2)
            	           Display_Log(1);
            	        else
            	           Display_Log(2);
            	    }     
               });
      }  
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu, menu);
      return true;
    }

    @Override
   	public boolean onOptionsItemSelected(MenuItem item) {
     
   		switch(item.getItemId()) 
   		{
   		    case R.id.dev:
   		    	Intent mainIntent = new Intent(this, Developers.class);
   	         	mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   	         	mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   	         		startActivity(mainIntent);
   	         		return true;
   	     }			
   		
   		return false;
   	}
    public void Display_Log(int fl)
    {  	    
    	final Context mContext = this;
    	final Dialog dialog = new Dialog(mContext);
       	dialog.setContentView(R.layout.login_auth);
    	dialog.setTitle("Authentication");
    	
    	if(fl==1) 
    	{
    		TextView t1=(TextView)dialog.findViewById(R.id.textView1);
    		t1.setText("Login Successful");
    		ImageView i1=(ImageView)dialog.findViewById(R.id.imageView1);
    		i1.setBackgroundResource(R.drawable.green);
    		Button reset=(Button) dialog.findViewById(R.id.button1);
    		reset.setVisibility(View.GONE);
    		new Handler().postDelayed(new Runnable() {
                @Override  
                public void run() {
                	dialog.dismiss();
                	final ProgressDialog dialog1 = ProgressDialog.show(MainActivity.this, "", "Loading.", true);
        	        dialog1.show();
        	        
        	        new Handler().postDelayed(new Runnable() {
        	            @Override
        	            public void run() {
        	            	dialog1.dismiss();
        	            	          
        	            	final Intent mainIntent = new Intent(MainActivity.this, _My_Home_2.class);
        	            	//final Intent mainIntent = new Intent(MainActivity.this, _Control_Panel.class);
        	            	MainActivity.this.startActivity(mainIntent);
        	            	MainActivity.this.finish();
        	               }
        	        }, 2000);
        	  }
            }, 2000);
    	}         
    	if(fl==2)
    	{
    		TextView t1=(TextView)dialog.findViewById(R.id.textView1);
    		t1.setText("Login Not Successful");
    		ImageView i1=(ImageView)dialog.findViewById(R.id.imageView1);
    		i1.setBackgroundResource(R.drawable.red);
    		Button reset=(Button) dialog.findViewById(R.id.button1);
    		reset.setVisibility(View.VISIBLE);
    		reset.setOnClickListener(new View.OnClickListener()
            { 
         	 public void onClick(View view) 
               {        
         		   dialog.dismiss();
         	    }     
            });
    	}
    	dialog.show();
  }
}