import socket
import RPi.GPIO as GPIO  
import time  
import thread
import threading

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("", 4000))
server_socket.listen(5)

GPIO.setmode(GPIO.BOARD)

# Device Pin Setup

device_3_op=11

device_2_op_1=15
device_2_op_2=19
device_2_op_3=21

device_1_op_1=12
device_1_op_2=16
device_1_op_3=18

GPIO.cleanup()
GPIO.setwarnings(False)

GPIO.setup(device_3_op, GPIO.OUT)
GPIO.setup(device_2_op_1, GPIO.OUT)
GPIO.setup(device_2_op_2, GPIO.OUT)
GPIO.setup(device_2_op_3, GPIO.OUT)

GPIO.setup(device_1_op_1, GPIO.OUT)
GPIO.setup(device_1_op_2, GPIO.OUT)
GPIO.setup(device_1_op_3, GPIO.OUT)

def on(pin):  
       	GPIO.output(pin,GPIO.HIGH)  
       	return  

def off(pin):  
       	GPIO.output(pin,GPIO.LOW)  
       	return 

def speed(spe,pin, stop_event):
        print "hello ", spe
        sp1=float(spe)/1000
        while(not stop_event.is_set()):
           GPIO.output(pin,GPIO.HIGH)
           time.sleep(sp1)
           GPIO.output(pin,GPIO.LOW)
           time.sleep(0.1-sp1)
	return	

def device_1(data):  
       	if data[1]=="1":
      		off(device_1_op_1) 
	if data[1]=="2":
       		on(device_1_op_1)
       # if(data[0]=="3" :
         #   device_3(data)

       	return 

def device_2(data):  
       
       	if data[1]=="1" :
  		 off(device_2_op_1) 
	if data[1]=="2" :
  		 on(device_2_op_1)
                    
       	return 

t2_stop=threading.Event()
t2=threading.Thread(target=speed, args=(str(2),device_2_op_1, t2_stop))

def device_3(data):  
        
       	if data[1]=="1" :
   		off(device_3_op) 
       	if data[1]=="2" :
   		on(device_3_op)
       
       	return 

def process(data):  
        
       	if data[0]=="1" :
       		device_1(data) 
       	if data[0]=="2" :
       	        device_2(data)
       	if data[0]=="3" :
       		device_3(data)
       	return 

while 1:
        print "Waiting....."
	client_socket, address = server_socket.accept()
	while 1:
	  data = client_socket.recv(512)
	  if(data != "") :    
	    print "RECIEVED:" , data  
	    if(data[1]=="3"):
	      if(t2.isAlive()):
                t2_stop.set()
                print "Stopped"
              sp=data[2]+data[3]
              t2_stop=threading.Event()
              t2=threading.Thread(target=speed, args=(sp,device_2_op_1, t2_stop))
              t2.start()
            process(data)
       	                


GPIO.cleanup()
