package com.psp.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.StringTokenizer;

public class _List_Adapter extends BaseAdapter {
 
    private Activity activity;
    private  LayoutInflater inflater=null;
   public int flag=0,count=0;
    public int num;
    public String ab;
      
private String[] appliance_name = {
    		
            "Refrigerator","Television","Geyser","A/C","Chimney","Incandescent Bulb",
            "Tube Lights","CFLs","Fans","Computer","Calling Bell", "Misc Chargers etc"
   }; 

private String[][] mixed={ 
		
		{"00","Incandescent Bulb", String.valueOf(R.drawable.incandescent_bulb)},
		{"00","Fans",String.valueOf(R.drawable.fan)},
		{"00","A/C",String.valueOf(R.drawable.ac)},
		
		};

private String[][] mixed1={ 
		
		{"50","HALL", String.valueOf(R.drawable.hall)},
		{"34","BEDROOM",String.valueOf(R.drawable.bedroom1)},
		{"56","BEDROOM",String.valueOf(R.drawable.bedroom2)},
		{"12","BATHROOM",String.valueOf(R.drawable.bath1)},
		{"10","BATHROOM",String.valueOf(R.drawable.bath2)},
		{"45","KITCHEN",String.valueOf(R.drawable.kitchen)},
		};

    public _List_Adapter(Activity a, int flag, String ab) {
    	activity = a;
    	inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //this.num=num;
        //this.count=count;
    	this.flag=flag;
    	this.ab=ab;
    	
    	setArray();
    	sortArray();
    	
    }  
 
    public void setArray()
    {
    	StringTokenizer st = new StringTokenizer(ab);
    	 int i=0;
		
		while (st.hasMoreElements()) {
			mixed[i][0]=String.valueOf(st.nextElement());
			i++;
		}
    }
    public void sortArray()
    {
    	int n=3;
		boolean chk=true;
	
		for(int j=0;j<n;j++)
		{
			for(int i=0;i<n-1-j;i++)
			{
					int a=Integer.parseInt(mixed[i][0]);
					int b=Integer.parseInt(mixed[i+1][0]);
					if((!chk&&a>b)||(chk&&a<b))
					{
						String t=mixed[i][1];
						mixed[i][1]=mixed[i+1][1];
						mixed[i+1][1]=t;
						mixed[i][0]=String.valueOf(b);
						mixed[i+1][0]=String.valueOf(a);
						
						String t1=mixed[i][2];
						mixed[i][2]=mixed[i+1][2];
						mixed[i+1][2]=t1;
					}
			}
		}
		
		int n1=6;
		boolean chk1=true;
		for(int j=0;j<n1;j++)
		{
			for(int i=0;i<n1-1-j;i++)
			{
					int a=Integer.parseInt(mixed1[i][0]);
					int b=Integer.parseInt(mixed1[i+1][0]);
					if((!chk1&&a>b)||(chk1&&a<b))
					{
						String t=mixed1[i][1];
						mixed1[i][1]=mixed1[i+1][1];
						mixed1[i+1][1]=t;
						mixed1[i][0]=String.valueOf(b);
						mixed1[i+1][0]=String.valueOf(a);
						
						String t1=mixed1[i][2];
						mixed1[i][2]=mixed1[i+1][2];
						mixed1[i+1][2]=t1;
					}
			}
		}
	 }
    public int getCount() {
    	int re=0;
        if(flag==2) re=3;
        if(flag==3) re=6;
        
        return re;
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
   public View getView(int position, View convertView, ViewGroup parent) {
    	
    	  View vi=convertView;
          if(convertView==null)
        	  
          {
          	switch(flag)
          	{         	
        	case 2: vi = inflater.inflate(R.layout.kwh_listitem, null);
	             break;
        	case 3: vi = inflater.inflate(R.layout.kwh_sp_list, null);
            break;
     	
          }
          }
          if(flag==1)
          {
          	TextView t1=(TextView) vi.findViewById(R.id.textView1);
          	t1.setText(appliance_name[position]);
          	t1.setAllCaps(true);
          }
          if(flag==2)
          {
        	  TextView name=(TextView) vi.findViewById(R.id.textView1);
          	TextView use=(TextView) vi.findViewById(R.id.textView2);
          	
          	name.setText(mixed[position][1]);
          	use.setText(mixed[position][0]+" Units");
          	
          	ImageView image=(ImageView) vi.findViewById(R.id.imageView1);
          	image.setBackgroundResource(Integer.parseInt(mixed[position][2]));
          	ProgressBar bar=(ProgressBar) vi.findViewById(R.id.progressBar1);
          	int m=Integer.parseInt(mixed[0][0]);
          	
          	float ab=(float)m/100;
          
          	bar.setProgress((int)(Integer.parseInt(mixed[position][0])/ab));
          	
          } 
          
          if(flag==3)
          {
        	  TextView name=(TextView) vi.findViewById(R.id.textView1);
          	TextView use=(TextView) vi.findViewById(R.id.textView2);
          	
          	name.setText(mixed1[position][1]);
          	use.setText(mixed1[position][0]+" Units");
          	
          	ImageView image=(ImageView) vi.findViewById(R.id.imageView1);
          	image.setBackgroundResource(Integer.parseInt(mixed1[position][2]));
          	ProgressBar bar=(ProgressBar) vi.findViewById(R.id.progressBar1);
          	int m=Integer.parseInt(mixed1[0][0]);
          	    	float ab=(float)m/100;
          	
          	bar.setProgress((int)(Integer.parseInt(mixed1[position][0])/ab));
          	
          } 
        
        return vi;
    }
}