from socket import *
from thread import *
import time
import socket
 
p=int(input("Enter Port\n"))

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("192.168.1.45", p))
client_socket.send("0000")
print "Connected"

p_s=2
a=0
port=p+1

dc={}
ct=0

st=0
dev_counter=0

inp_1_prev=0
inp_3_prev=0
inp_2m_prev=0
inp_2l_prev=0

#For switch
dev_1_state=1
dev_3_state=1
dev_2_state=1
dev_2_inten=0

data_go="0000"
data_go_s="0000"

def hello(data):
   for i, sock in dc.items():
      add=dc[i]
      add.send(str(data)+"\n")
   return   

def update_seven(data):
    d="0000"
    global data_go_s

    if(data[0]=="2"):
       if(data[1]=="1"):
          d="820"
       if(data[1]=="2"):
          d="823"
       if(data[1]=="3"):
          f=data[2]+data[3]
          pc1=int(f)
          if (pc1>0 and pc1<=50): 
           d="821"
          if (pc1>50 and pc1<=75): 
           d="822"
          if (pc1>75 and pc1<=100): 
           d="823"
     
    else :
       d="8"+data[0]+str(int(data[1])-1)
     
    data_go_s=d
    return

def for_sw(dat):
  inp=int(dat[1])
   
  global data_go
  global dev_1_state
  global dev_3_state
  global dev_2_state 
  global dev_2_inten
  
  while 1:
    if(inp==1):
       if(dev_1_state==1):
               data_go="1299"
	       dev_1_state=2
               break
       if(dev_1_state==2):
               data_go="1100"
	       dev_1_state=1
               break
      
    if(inp==3):
       if(dev_3_state==1):
               data_go="3299"
	       dev_3_state=2
               break
       if(dev_3_state==2):
               data_go="3100"
	       dev_3_state=1
               break
     
    
    if(inp==2):
      if(dat[2]=="1"):
          if(dev_2_inten!=100 and dev_2_inten!=0):
               dev_2_inten=dev_2_inten+25
               if(dev_2_inten>=100):
                 data_go="23"+str(dev_2_inten-1)
               else :
                 data_go="23"+str(dev_2_inten)
               break

          if(dev_2_state==2 and dev_2_inten==0):
               dev_2_inten=dev_2_inten+25
               if(dev_2_inten>=100):
                 data_go="23"+str(dev_2_inten-1)
               else :
                 data_go="23"+str(dev_2_inten)
               break

	  if(dev_2_state==1):
               data_go="2299"
 	       dev_2_inten=100
	       dev_2_state=2
               break
          if(dev_2_state==2):
               data_go="2100"
	       dev_2_inten=0
	       dev_2_state=1
               break
 
      if(dat[2]=="2"):      
          if(dev_2_state==2):
            dev_2_inten=dev_2_inten-25
            if(dev_2_inten>0):
                 data_go="23"+str(dev_2_inten)
            else :
                 dev_2_inten=0
                 data_go="2300" 
            break
    data_go="0000"
    break

  send(data_go,1)
  return

def send(send_data, cd):
    client_socket.send(send_data)
    if(cd==1):
       hello(send_data)
    time.sleep(0.2)
    update_seven(send_data) 
    client_socket.send(data_go_s)
    time.sleep(0.5)
    global dev_counter
    dev_counter=0
    if(dev_1_state==2):
        dev_counter=dev_counter+1
    if(dev_2_state==2):
        dev_counter=dev_counter+1
    if(dev_3_state==2):
        dev_counter=dev_counter+1

    tocam="9"+str(dev_counter)+"00"
    hello(tocam)
    return

def index_123(da):
    
    global dev_1_state
    global dev_3_state
    global dev_2_state
    global data_go

    ch=da[0]
    ch1=da[1]
    ch2=da[2]+da[3]

    if(ch=="1"):
      if(ch1=="1"):
        dev_1_state=1
      else :
        dev_1_state=2
    
    if(ch=="3"):
      if(ch1=="1"):
        dev_3_state=1
      else :
        dev_3_state=2

    if(ch=="2"):
      if(ch1=="1"):
        dev_2_state=1
      else :
        dev_2_state=2

    data_go=da
    send(data_go,1)
    return
         
def test_connections(conn) :
    global p_s
    data = conn.recv(512)
    global data_go

    if(data!=""):
     print "RECIEVED:" , data
          
     if(data[0]=="5"):
      if(data[1]=="1"):
        p_s=1
      if(data[1]=="2"):
        p_s=2
        
     if(data[0]=="1" or data[0]=="2" or data[0]=="3"):
       start_new_thread(index_123,(data,))
             
     if(data[0]=="7"):
        start_new_thread(for_sw,(data,))

     if(data[0]=="6" and data[1]=="1" and p_s==2):
      index_123("1100")
      index_123("2100")
      index_123("3100")
      start_new_thread(hello,("6100",))

     if(data[0]=="6" and data[1]=="2" and p_s==2):
      index_123("1299")
      start_new_thread(hello,("6200",))
          
def clientthread(conn):

     while True:
          test_connections(conn)  

while 1:
    host = ''  
    print "Server(Web) waiting on port ", port

    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind((host, port))
    sock.listen(5) 
   
    while True:
      conn, addr = sock.accept()
      start_new_thread(clientthread,(conn,)) 
      dc[ct]=conn
      ct=ct+1
 
conn.close()
sock.close()
