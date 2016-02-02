import socket
import RPi.GPIO as GPIO  
import time  
import thread
import threading
from thread import *

port=int(input("Enter port\n"))
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("", port))
server_socket.listen(5)

GPIO.setmode(GPIO.BOARD)

# Device Pin Setup
dev_o_1=11
dev_o_3=13
dev_2=15

dev2=0

seven_1=21
seven_3=23
seven_2m=26
seven_2l=24

GPIO.cleanup()
GPIO.setwarnings(False)

GPIO.setup(dev_o_1, GPIO.OUT,initial=GPIO.LOW)
GPIO.setup(dev_o_3, GPIO.OUT,initial=GPIO.LOW)
GPIO.setup(dev_2, GPIO.OUT,initial=GPIO.LOW)
GPIO.setup(seven_1, GPIO.OUT,initial=GPIO.LOW)

GPIO.setup(seven_3, GPIO.OUT,initial=GPIO.LOW)
GPIO.setup(seven_2m, GPIO.OUT,initial=GPIO.LOW)
GPIO.setup(seven_2l, GPIO.OUT,initial=GPIO.LOW)
GPIO.setup(8, GPIO.OUT, initial=GPIO.LOW)

data2="9999"
def decode(code):
        a=0
        b=0
    
        while 1: 
            if(code=="0"):
              a=0
              b=0
              break
            if(code=="1"):
              a=1
              b=0
              break
            if(code=="2"):
              a=0
              b=1
              break 
            if(code=="3"):
              a=1
              b=1
              break

        GPIO.output(seven_2l, a)
        GPIO.output(seven_2m, b)
        return         

def speed(spe,pin, stop_event):
        sp1=float(spe)/1000
        while(not stop_event.is_set()):
           GPIO.output(pin,GPIO.HIGH)
           time.sleep(sp1)
           GPIO.output(pin,GPIO.LOW)
           time.sleep(0.1-sp1)
	return	

t2_stop=threading.Event()
t2=threading.Thread(target=speed, args=(str(2),dev_2, t2_stop))

def seven(data):
        
        if data[1]=="1":
           if data[2]=="0":
             off(seven_1)
           if data[2]=="1":
             on(seven_1)
        if data[1]=="3":
           if data[2]=="0":
             off(seven_3)
           if data[2]=="1":
             on(seven_3)
        if data[1]=="2":
           decode(data[2])

def speed_control(data):
     global t2_stop
     global t2

     if(t2.isAlive()):
        t2_stop.set()
     sp=data[2]+data[3]
     t2_stop=threading.Event()
     t2=threading.Thread(target=speed, args=(sp,dev_2, t2_stop))
     t2.start()

def process(data):  
        
        global t2_stop
        global t2

       	if data[0]=="1" :
           if data[1]=="1":
                off(dev_o_1)
           if data[1]=="2":
                on(dev_o_1)

       	if data[0]=="2" :
           if data[1]=="1":
                if(t2.isAlive()):
                  t2_stop.set()
                off(dev_2)
           if data[1]=="2":
                on(dev_2)
           if data[1]=="3":
               speed_control(data)
                 
       	if data[0]=="3" :
           if data[1]=="1":
                off(dev_o_3)
           if data[1]=="2":
                on(dev_o_3)
        if data[0]=="8" :
                seven(data)
       	return 

def on(pin):
      GPIO.output(pin, GPIO.HIGH)
      return            

def off(pin):
      GPIO.output(pin, GPIO.LOW)
      return

while 1:
        print "Drivers Running....."
	client_socket, address = server_socket.accept()
	while 1:
	  data = client_socket.recv(512)
	  if(data != "") :
           if(data!=data2):
            data2=data    
	    print "Received @ Driver :" , data
            start_new_thread(process,(data,))
