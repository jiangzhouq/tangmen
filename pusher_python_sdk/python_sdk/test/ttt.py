import sys
import time
i = 1
while True:
	filea = open('./a.txt','w')
	fileb = open('./b.txt','a')
	filea.write(str(i))
	filea.close
	fileb.write(str(i))
	fileb.close
	i = i + 1
	time.sleep(1)
