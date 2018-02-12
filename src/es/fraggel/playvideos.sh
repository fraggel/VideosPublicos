#!/bin/sh
setterm -cursor off
PLAYER_OPTIONS="-b"
PLAYER="omxplayer"

# Where is the playlist
cp /home/pi/youtubeURLs.txt /home/pi/youtubeURLs.txt2
PLAYLIST_FILE=/home/pi/youtubeURLs.txt2
while [ true ]; do
        # Sleep a bit so it's possible to kill this
        sleep 1

        # Do nothing if the playlist doesn't exist
        if [ ! -f "${PLAYLIST_FILE}" ]; then
                echo "Playlist file ${PLAYLIST_FILE} not found"
                continue
        fi

        # Get the top of the playlist
        file=$(cat "${PLAYLIST_FILE}" | head -n1)

        # And strip it off the playlist file
        cat "${PLAYLIST_FILE}" | tail -n+2 > "${PLAYLIST_FILE}.new"
        mv "${PLAYLIST_FILE}.new" "${PLAYLIST_FILE}"

        # Skip if this is empty
        if [ -z "${file}" ]; then
                cp /home/pi/youtubeURLs.txt /home/pi/youtubeURLs.txt2
                continue
        fi

        # Check that the file exists
        #if [ ! -f "${file}" ]; then
        #        echo "Playlist entry ${file} not found"
        #        continue
        #fi

        echo
        echo "Playing ${file}..."
        echo

        omxplayer -b "$(youtube-dl -g -f 18 $file)"
	while ps ax | grep -v grep |grep omxplayer > /dev/null
			do
				sleep 1;
			done

        echo
        echo "Playback complete, continuing to next item on playlist."
        echo
done
