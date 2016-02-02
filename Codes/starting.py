import socket
import time
import os
#Acting Client Role


print "System Booting...."
print "Starting Programs....."

os.system("python /home/pi/Desktop/PSP/server_rpi.py")
time.sleep(2)

print "Server Driver started. "
os.system("python /home/pi/Desktop/PSP/web_rpi.py")
print " Connected to Server Driver"
time.sleep(1)


print "System is Healthy and ready for Operation"


