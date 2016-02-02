import socket
import RPi.GPIO as GPIO  
import time  

port=int(input("Enter Port\n"))
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("192.168.1.45", port+1))

inp_1=12
inp_3=22
inp_2m=18
inp_2l=16

inp_1_prev=0
inp_3_prev=0

inp_2m_prev=0
inp_2l_prev=0

GPIO.setmode(GPIO.BOARD) 

GPIO.setup(inp_1,GPIO.IN,pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(inp_2m,GPIO.IN,pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(inp_2l,GPIO.IN,pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(inp_3,GPIO.IN,pull_up_down=GPIO.PUD_DOWN)

def send(data): 
       client_socket.send(data) 
       return 

print "Switch Interface Activated..."

while True:
        
        time.sleep(0.05)       
        input_1=GPIO.input(inp_1)
        input_3=GPIO.input(inp_3)

        input_2m=GPIO.input(inp_2m)     
        input_2l=GPIO.input(inp_2l)
        
        # Device 1
        if((inp_1_prev==0) and input_1):
          inp_1_prev=1
          send("710")
          continue      
   
        if((inp_1_prev==1) and (not input_1)):
          inp_1_prev=0
          continue
          
        #Device 3
        if((inp_3_prev==0) and input_3):
          inp_3_prev=1
          send("730")
          continue
   
        if((inp_3_prev==1) and (not input_3)):
          inp_3_prev=0
          continue
 
        #Device 2
        if((inp_2m_prev==0) and input_2m):
          inp_2m_prev=1
          send("721")
          continue

        if((inp_2m_prev==1) and (not input_2m)):
          inp_2m_prev=0
          continue
        
        if((inp_2l_prev==0) and input_2l):
          inp_2l_prev=1
	  send("722")     
          continue

	if((inp_2l_prev==1) and (not input_2l)):
         inp_2l_prev=0
         continue
