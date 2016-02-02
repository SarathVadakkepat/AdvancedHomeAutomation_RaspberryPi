package com.psp.home;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
    
public class _Rooms_2 extends Activity implements OnSeekBarChangeListener{
                 
	private SeekBar bar2;
	TextView texview2;
	public BufferedWriter _out_base;
	public BufferedReader _in_base;
	Socket _base;  
	String _msg_base="0000", _in_msg="0000", prev_sent="0000", prev_recv="0000";
	        
	String _ip="192.168.1.44";
	int _port=5091;   
	int _ctr=0;
	int _tracked_device;
	TextView _t1,_t2,_t3,_t4,_t11,_t22,_t33,_t44, _cam_input;
	      
     int intensity, cstate=1, pc=2, device=0, textview=3, units=4, count_down, count_up;
	 int apps[][]=new int[3][5];
	 long elapsedMillis=0;
	 
    { 	for(int i=1;i<=3;i++)
    	{
    		apps[i-1][0]=i;
    		apps[i-1][1]=1;
    		apps[i-1][2]=00;
    		apps[i-1][4]=0;
    	}    
     }
    
     long timers[][]=new long[3][3];
     int uni[]=new int[3];
     String unit="";
     
     int prev_inten=0;
     
   // DATA PROTOCOL
   /*
    *  --> A B C D
    *   A - Device ID   0-9
    *   B - ON/OFF/INTENSITY  1/2/3
    *   CD - Percentage of intensity
    */   
	  
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        take();
        setContentView(R.layout.rooms_2); 
        ActionBar an=getActionBar();
        an.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_action));
        
        _t1 = (TextView) findViewById(R.id.textView6); 
        _t2 = (TextView) findViewById(R.id.TextView01); 
        _t3 = (TextView) findViewById(R.id.TextView02); 
        _t4= (TextView) findViewById(R.id.textView3); 
        _cam_input= (TextView) findViewById(R.id.TextView03); 
        apps[0][3]=_t1.getId();
        apps[1][3]=_t2.getId();
        apps[2][3]=_t3.getId();
        
        update_all();
        
         RelativeLayout bd1=(RelativeLayout) findViewById(R.id.relativeLayout1);
         bd1.setOnClickListener(new View.OnClickListener()  {   
        	 public void onClick(View view) 
              {    
        		 show(apps[0][cstate],apps[0][device]);
        	  }     
           });
            
        RelativeLayout bd2=(RelativeLayout) findViewById(R.id.relativeLayout2);
        bd2.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		 show(apps[1][cstate],apps[1][device]);
        	  }        
           });   
        
        RelativeLayout bd3=(RelativeLayout) findViewById(R.id.relativeLayout3);
        bd3.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		 show(apps[2][cstate],apps[2][device]);
             }     
           });
       
        Button off=(Button) findViewById(R.id.button2);
        off.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {  
        		set_on_off(1);
        		}     
           });
              
        Button on=(Button) findViewById(R.id.button1);
        on.setOnClickListener(new View.OnClickListener()
           { 
        	 public void onClick(View view) 
              {    
        		 set_on_off(2);
        	  }     
           });
        
        final	ToggleButton toggle_saver = (ToggleButton) findViewById(R.id.toggleButton1);
        toggle_saver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      	        if (isChecked) 
      	        {
      	      	      toggle_saver.setTextColor(0xFF268307);
      	      	      _msg_base="5200";
      	      	      Toast.makeText(getApplicationContext(), "Power Saver Enabled.", Toast.LENGTH_SHORT).show();
      	      	      send_data();
      	        }        
      	        else
      	        {    
      	           	 toggle_saver.setTextColor(0xFFa60607);
      	             _msg_base="5100";
      	             Toast.makeText(getApplicationContext(), "Power Saver Disabled.", Toast.LENGTH_SHORT).show();
      	             send_data();
      	        } 
      	    }   
      	});  
        toggle_saver.setChecked(true);
        _cam_input.setText(" nobody ");
   }   
	
	@Override
   	public boolean onOptionsItemSelected(MenuItem item) {
     
   		switch(item.getItemId()) 
   		{
   		    case R.id.menu_share:
   		    	unit="";
   		    	make_arr();
   		    	Intent mainIntent = new Intent(this, _kWh_Manager.class);
   	         	mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   	         	mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   	            mainIntent.putExtra("array", unit); 
   	         		startActivity(mainIntent);
   	         		return true;
   	
   	     }			
   		
   		return false;
   	} 
	  
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_main, menu);
	       return true;
	    }
	public void update_all()
	{
		for(int i=1;i<=3;i++)
		{
			update(i,apps[i-1][cstate]);
			try {
	           	  Thread.sleep(100);
	           	} catch (InterruptedException ie) {
	           }   
		}
          	
       for(int i=1;i<=3;i++)
            update_text(apps[i-1][cstate], (TextView) findViewById(apps[i-1][textview]));
    }
    
    public void set_on_off(int oo)
    {
    	for(int i=1;i<=3;i++)
    	{
           	update(i,oo);
           	try {
           	  Thread.sleep(1500);
           	} catch (InterruptedException ie) {
           }
      }   
    }  
    
    public void update_text(int cd, TextView tv)
    {
    	switch(cd)
    	{
    	case 1: tv.setText("OFF");
    			tv.setTextColor(0xFFa60607);
    			break;
    	case 2: tv.setText("ON");
    			tv.setTextColor(0xFF268307);
    			break;
    	}
    	return;
    }     
    public void input_update(String up)
    {
    	String x=up;
    	if(x.length()==3)
    		x=x+"0";
    	if(Integer.parseInt(String.valueOf(x.charAt(0)))<=3)
    	{
    	apps[Integer.parseInt(String.valueOf(x.charAt(0)))-1][cstate]=Integer.parseInt(String.valueOf(x.charAt(1)));
    	apps[Integer.parseInt(String.valueOf(x.charAt(0)))-1][pc]=Integer.parseInt(String.valueOf(x.charAt(2)+String.valueOf(x.charAt(3))));
    	update_all();
    	}
    	if(Integer.parseInt(String.valueOf(x.charAt(0)))==6 && Integer.parseInt(String.valueOf(x.charAt(1)))==2)
    	{ update(1,2);
    	  
    	  _cam_input.setText(" someone ");
    	
    	}
    	if(Integer.parseInt(String.valueOf(x.charAt(0)))==6 && Integer.parseInt(String.valueOf(x.charAt(1)))==1)
    	{
    		set_on_off(1);
    	
    	 _cam_input.setText(" nobody ");
    	  
    	}
    	  
    }  
    public void update(int dev, int state)  
    {
       if(apps[dev-1][cstate]!=state && apps[dev-1][cstate]!=3 )
    	{   
    		apps[dev-1][cstate]=state;
    		if(apps[dev-1][cstate]==1)
    			apps[dev-1][pc]=0;
    		if(apps[dev-1][cstate]==2)
    			apps[dev-1][pc]=99;
    		
    		update_text(state, (TextView) findViewById(apps[dev-1][textview]));
    	   	
    	   	_msg_base=String.valueOf(apps[dev-1][device])+String.valueOf(apps[dev-1][cstate])+String.valueOf(apps[dev-1][pc]);
        	
    	   	if(_msg_base.length()==3)
    	   		_msg_base=_msg_base+"0"; 
    	   	if(Integer.parseInt(_msg_base)!=Integer.parseInt(prev_sent))
            	 send_data();
    	   	prev_sent=_msg_base;
    	   	   
            if(state==2)
            {
               	timers[dev-1][0]=SystemClock.elapsedRealtime();
                
            }
            if(state==1)   
            {
            	 timers[dev-1][1]=SystemClock.elapsedRealtime();
            	 long dif=timers[dev-1][1]-timers[dev-1][0];
            	 apps[dev-1][units]=apps[dev-1][units]+(int)(dif/1000);
            }
        }   
    	if(state==1 && apps[dev-1][cstate]==3)
    	{
    		apps[dev-1][cstate]=state;
    		update_text(state, (TextView) findViewById(apps[dev-1][textview]));
    		
            _msg_base=String.valueOf(apps[dev-1][device])+String.valueOf(apps[dev-1][cstate])+String.valueOf(apps[dev-1][pc]);
        	
    	   	if(_msg_base.length()==3)
    	   		_msg_base=_msg_base+"0"; 
    	   	if(Integer.parseInt(_msg_base)!=Integer.parseInt(prev_sent))
            	 send_data();
    	   	prev_sent=_msg_base;
    	}
    	if(state==3 && prev_inten!=intensity)
    	{
    	 apps[dev-1][pc]=intensity;
    	 _msg_base=String.valueOf(apps[dev-1][device])+String.valueOf(apps[dev-1][cstate])+String.valueOf(apps[dev-1][pc]);
    	 if(_msg_base.length()==3)
 	   		_msg_base=String.valueOf(_msg_base.charAt(0))+String.valueOf(_msg_base.charAt(1))+"0"+String.valueOf(_msg_base.charAt(2)); 
    	 
    	 if(Integer.parseInt(_msg_base)!=Integer.parseInt(prev_sent))
            send_data();
        
         prev_inten=apps[dev-1][pc];
         prev_sent=_msg_base;
      }
 	        
      	_ctr=0;
    	for(int i=0;i<3;i++)
    		if(apps[i][1]!=1)
    			_ctr++;
    	_t4.setText(" "+String.valueOf(_ctr)+" ");
    }
    
    public void take()
    {   
    final Context mContext = this; 
    	final Dialog dialog = new Dialog(mContext);
       	dialog.setContentView(R.layout.ip);
    	dialog.setTitle("Action");
    	Button on1=(Button) dialog.findViewById(R.id.button1);
        on1.setOnClickListener(new View.OnClickListener()
            {    
            	 public void onClick(View view) 
                  {          
            		 dialog.dismiss();
            		 EditText login=(EditText) dialog.findViewById(R.id.editText1);
             	     EditText pswd=(EditText) dialog.findViewById(R.id.editText2);
             	     _ip=login.getText().toString();
             	     _port=Integer.parseInt(pswd.getText().toString());
             	     connect();
                  }     
               });
          dialog.show();
   }
    public void adv_control(int de)
    { 
    	final int de1=de;
    	final Context mContext = this; 
    	final Dialog dialog = new Dialog(mContext);
             
    	dialog.setContentView(R.layout.adv_app_control);
    	dialog.setTitle("Advanced");
    	Button set_timer=(Button) dialog.findViewById(R.id.button1);
    	set_timer.setOnClickListener(new View.OnClickListener()
            { 
            	 public void onClick(View view) 
                  {          
            		 dialog.dismiss();
            		 EditText start=(EditText) dialog.findViewById(R.id.editText1);
             	     EditText stop=(EditText) dialog.findViewById(R.id.editText2);
             	     count_up=Integer.parseInt(start.getText().toString().length()!=0?start.getText().toString():"0")*1000;
             	     count_down=Integer.parseInt(stop.getText().toString().length()!=0?stop.getText().toString():"0")*1000;
             	   
             	    if(count_down!=0)
             	      timer(count_down, 1, de1);
             	    if(count_up!=0)
              	      timer(count_up, 2, de1);
             	   }     
               });
    	dialog.show();
    }
    public void show(int st, int d)
    {      
    	final int mm=d;
    	final Context mContext = this; 
    	final Dialog dialog = new Dialog(mContext);
                
    	dialog.setContentView(R.layout.app_pop);
    	dialog.setTitle("Action");
        texview2 = (TextView) dialog.findViewById(R.id.textView3); 
        bar2 = (SeekBar) dialog.findViewById(R.id.s1); 
        bar2.setOnSeekBarChangeListener(this);
        bar2.setEnabled(false);
        
         final	TextView state=(TextView) dialog.findViewById(R.id.textView2);
         state.setTextColor(0xFFFF0000);
         
         final Button show=(Button) dialog.findViewById(R.id.button1);
         final Button timer=(Button) dialog.findViewById(R.id.Button01);
         show.setEnabled(false);
         
         final Button ok=(Button) dialog.findViewById(R.id.button2);
        ok.setOnClickListener(new View.OnClickListener()
     	{   @Override
             public void onClick(View view)
             { 	 
     		  dialog.dismiss();
     	    }   
     	});
        
        _tracked_device=mm;
         final	ToggleButton toggle = (ToggleButton) dialog.findViewById(R.id.toggleButton1);
         toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
       	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
       	        if (isChecked) 
       	        {
       	           state.setText("ON");
       	           state.setTextColor(0xFF268307);
       	           toggle.setTextColor(0xFFa60607);
       	           show.setEnabled(true);
    			   update(mm,2);
    			   bar2.setProgress(100);
    			   if(mm==1 || mm==3)
    		         show.setEnabled(false);
    		    }    
       	        else
       	        {    
       	           state.setText("OFF");
       	           state.setTextColor(0xFFa60607); 
       	           toggle.setTextColor(0xFF268307);
       	           update(mm,1);
       	           show.setEnabled(false);
       	          bar2.setEnabled(false);
       	         bar2.setProgress(0);
       	        } 
       	    }   
       	});  
      
       
      if(st==2||st==3)  
          {
    	      toggle.performClick();
    	      if(st==3)
    	      {   update(mm,3);
    	           bar2.setProgress(apps[mm-1][pc]);
    	      }
          }
      show.setOnClickListener(new View.OnClickListener()
    	{   @Override
            public void onClick(View view)
            { 	 
    		   bar2.setEnabled(bar2.isEnabled()?false:true);
    		   _tracked_device=mm;
    	    }   
    	});
      
      timer.setOnClickListener(new View.OnClickListener()
  	{   @Override
          public void onClick(View view)  { 	 
  		   adv_control(mm);
  		  }   
  	});
      
    dialog.show();
    }
     
   @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
    		boolean fromUser) {
       	texview2.setText(progress+" %");
    }
        
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
      }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    	
    	seekBar.setSecondaryProgress(seekBar.getProgress());
    	intensity=seekBar.getProgress();
    	if(intensity==100)
    		intensity=99;
        update(_tracked_device,3);
   }
    
   public void send_data()
    {
    	Thread send_ser=new Thread(new Runnable(){
	    public void run()  
	    { 
	    	try { 
	    		
	    		if( _out_base!=null)
	    		{
	    			if(Integer.parseInt(prev_recv)!=6100 &&Integer.parseInt(prev_recv)!=6200)
	    			{
	    			    _out_base.write(_msg_base);
                        _out_base.flush();
	    		   }
	    			prev_recv="0000";
	    		}
            } 
		catch (UnknownHostException e)
		   { }  
		catch (IOException e)
		   { } 
	     }
        });
     send_ser.start();
    }  
   
    public void recv_data()
    {
    	Thread recv_ser=new Thread(new Runnable(){
	    public void run()
	    {        
	    	while(_in_base!=null)
	    	{   
	    	  try { 
	    	  		_in_msg=_in_base.readLine();
	       	  		if(_in_msg!=null)
	    		    {  
	       	  			prev_recv=_in_msg;
	    	  			 runOnUiThread(new Runnable()
	    		       {
	    		    	 @Override
	    		    	 public void  run()
	    		    	 {  
	    		    		 input_update(_in_msg);
	    		    	 }
	    		       });  
	    	        }  
	    	     }  
		catch (UnknownHostException e)
		  { }  
		catch (IOException e)
		  { }
	      }
	     }
      });
     recv_ser.start();
    }      
    
    public void connect()
    {   
    	Thread connect_ser=new Thread(new Runnable(){
	    public void run()
	    {     
	    	try { 
	    	  	 _base = new Socket(_ip, _port);
		    	 _in_base = new BufferedReader(new InputStreamReader(_base.getInputStream()));
                 _out_base = new BufferedWriter(new OutputStreamWriter(_base.getOutputStream())); 
		         recv_data();
		        } 
		catch (UnknownHostException e)
		  {  }      
		catch (IOException e)
		  {  } 
	    } 
      });
        connect_ser.start();       
    }   
    
    public void timer(int t, int sta_sto, int d)
    {   
    	final int code=sta_sto;
    	final int devc=d;
    	final int ti=t;
    	 
    	new CountDownTimer(ti, ti) {
    		
      	     public void onTick(long millisUntilFinished) { 
      	     }
    
    	     public void onFinish() {
    	         if(code==1)
    	        	update(devc,1);
    	         if(code==2)
    	        	update(devc,2);
    	        	        
    	      }
    	}.start();
    	
     }
    
    public void make_arr()
    {
    	for(int i=0;i<3;i++)
    	   unit=unit+String.valueOf(apps[i][4])+" ";
     }
   }