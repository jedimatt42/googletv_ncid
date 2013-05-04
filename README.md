googletv_ncid
=============

## Network Caller ID client for Google TV

Requires an NCID server running somewhere on your home network.
Configuration accepts host and port values for NCID server.

The application reads incoming caller id information from the NCID server, and displays in a 
blue translucent overlay at the bottom of the TV for a short period of time. 

The application is fairly robust. It will automatically re-establish a connection to the NCID server
if it fails or the server goes offline and returns. The connection is established at boot on the google tv 
device. An ongoing notification indicates the connected status.

I have tested this on the Sony NSZ-GS7 Internet Player with Google TV.

## Known deficiencies:
  + No custom launcher icon.
  + PLEX pauses playback when overlay appears. User has to wait for overlay to dismiss and then press play again.

-M@

