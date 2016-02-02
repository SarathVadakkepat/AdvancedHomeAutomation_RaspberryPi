/***************************************************************
 *  Project 		: Advanced Home Automation
 *  Date 		: 30 March, 2013
 *  Program Name 	: CameraInterface.cpp
 *  Authors		: Sarath, Prahlad, Prachet.
 *  Description 	: Detects the presence or absence of subject of interest which in this case
 *                        is Human presence, thereby connecting to a server program running on a web
 *                        server and finally to a processor system.
 *  Compile 		:  g++ -I/usr/local/include/opencv -I/usr/include -I/usr/local/include/opencv2
 *                         -I/usr/include/opencv2 -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP
 *  		           -MF"/home/sarathv/Opencvdemo.d" -MT"/home/sarathv/Opencvdemo.d" -o
 *  			   "/home/sarathv/Opencvdemo.o" "/home/sarathv/Opencvdemo.cpp"
 *
 *  			   g++ -L/usr/local/lib -L/usr/lib -o "Opencvdemo" /home/sarathv/Opencvdemo.o
 *  			   -lopencv_core -lopencv_highgui -lopencv_objdetect -lopencv_contrib
 *  			   -lopencv_video
 *  Developed in 	: Eclipse Juno for C++
 *  Uses 		: OpenCV libraries
 *****************************************************************/
extern "C"
 {
    #include <pthread.h>
    #include <unistd.h>
 }

#include <iostream>
#include <opencv2/opencv.hpp>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <malloc.h>
#include <sys/time.h>

using namespace std;
using namespace cv;

void _initiate_connection();
void _initiate_poweroff_procedures();
void _send_host(int);
void _initiate_poweron_procedures();
void _set_IP();
int _current_state_room=0;
void recv_da();
void  * function2(void * argument);

//Socket Variables
struct hostent *_host;
struct sockaddr_in _server_addr;
int _sock, _bytes_recieved;

//Data
char _data_off[1024]="6100\0";
char _data_on[1024]="6200\0"; 
char _test_data[1024]="0000\0";

//Connection
char _ip_address[1024]="192.168.1.45";
int _port=5001;

//Timer
timeval t1, t2;
double _time_out;
double _time_out_user;

int bytes_recieved;      
char receive_data[1024]; 

int main (int argc, const char * argv[])
{
    _set_IP();
    _initiate_connection();
    cout<<"Enter timeout in seconds \n";
    cin>>_time_out_user;
   
    pthread_t p1;
    pthread_create( &p1, NULL, function2,NULL);

    VideoCapture cap(CV_CAP_ANY);
    gettimeofday(&t1, NULL);
    //VideoCapture cap("video1.3gp");
    cap.set(CV_CAP_PROP_FRAME_WIDTH, 320);
    cap.set(CV_CAP_PROP_FRAME_HEIGHT, 240);
    if (!cap.isOpened())
        return -1;

    Mat _video_input;
    HOGDescriptor _hog_human_detect;
    _hog_human_detect.setSVMDetector(HOGDescriptor::getDefaultPeopleDetector());

    namedWindow("video capture", CV_WINDOW_AUTOSIZE);
    while (true)
    {
    	gettimeofday(&t2, NULL);
    	_time_out = (t2.tv_sec - t1.tv_sec);
    	if(_time_out>=_time_out_user)
    		if(_current_state_room==2||_current_state_room==0)
    		   _initiate_poweroff_procedures();

        cap >> _video_input;
        if (!_video_input.data)
            continue;

        vector<Rect> found, found_filtered;
        _hog_human_detect.detectMultiScale(_video_input, found, 0, Size(8,8), Size(32,32), 1.05, 2);

        size_t i, j;
        for (i=0; i<found.size(); i++)
        {
            Rect r = found[i];
            for (j=0; j<found.size(); j++)
                if (j!=i && (r & found[j])==r)
                    break;
            if (j==found.size())
                found_filtered.push_back(r);
        }

        for (i=0; i<found_filtered.size(); i++)
        {
      	    Rect r = found_filtered[i];
            r.x += cvRound(r.width*0.1);
	    r.width = cvRound(r.width*0.8);
	    r.y += cvRound(r.height*0.06);
	    r.height = cvRound(r.height*0.9);

	    rectangle(_video_input, r.tl(), r.br(), cv::Scalar(0,255,0), 2);
	    gettimeofday(&t1, NULL);
	    if(_current_state_room==1||_current_state_room==0)
	     _initiate_poweron_procedures();
	}

     imshow("video capture", _video_input);
     if (waitKey(50) >= 0)
     break;
    }
    return 0;
}

void _set_IP()
{
	//cout<<"Enter IP \n";
	//cin>>_ip_address;
	cout<<"Enter Port \n";
	cin>>_port;

	return;
}

void _initiate_poweroff_procedures()
{
    cout<<"The is nobody in the room \n" ;
    cout<<"Initiating shutdown Procedures..\n";
    _send_host(2);
    cout<<"Shutdown complete\n\n";
    _current_state_room=1;
    gettimeofday(&t1, NULL);
    return;
}

void _initiate_poweron_procedures()
{
    cout<<"Somebody entered the room\n";
    cout<<"Initiating Switch On Procedures..\n";
    _send_host(1);
    cout<<"Switch On complete\n\n";
    gettimeofday(&t1, NULL);
    _current_state_room=2;
    return;
}

void _send_host(int code)
{
	if(code==1)
             send(_sock,_data_on,strlen(_data_on), 0);
        if(code==2)
             send(_sock,_data_off,strlen(_data_off), 0);
        
    return;
}

void * function2(void * argument)
{
   while (1)
   {
      bytes_recieved = recv(_sock,receive_data,1024,0);                     
      receive_data[bytes_recieved] = '\0';
                 
      if (strlen(receive_data)!= 0) 
       {                                          
         int n=receive_data[0]-48;
         int n1=receive_data[1]-48;
         if(n==9)
          {
            if(n1==0)
              _current_state_room=1;
            if(n1>0)
              _current_state_room=2;
              
            gettimeofday(&t1, NULL);
           }
       }
    }
return 0;
}

void _initiate_connection()
{
	_host = gethostbyname(_ip_address);
	if ((_sock = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
	         perror("Socket");
	         exit(1);
	}
	_server_addr.sin_family = AF_INET;
	_server_addr.sin_port = htons(_port);
	_server_addr.sin_addr = *((struct in_addr *)_host->h_addr);
	bzero(&(_server_addr.sin_zero),8);
        if (connect(_sock, (struct sockaddr *)&_server_addr,
	                    sizeof(struct sockaddr)) == -1)
         {
	            perror("Connect");
	            exit(1);
         }
     	send(_sock,_test_data,strlen(_test_data), 0);
}
