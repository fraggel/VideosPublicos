#!/bin/sh
#setterm -cursor off
SERVICE="omxplayer"
#while true; do
#	if ps ax | grep -v grep |grep $SERVICE > /dev/null
#	then
#		sleep 1;
#	else
		filename="/home/pi/playlist.txt"
		while read -r line
		do
		    $(line)
			while ps ax | grep -v grep |grep $SERVICE > /dev/null
			do
				sleep 1;
			done
			echo $line
		done < "$filename"
		
#	fi
#done
