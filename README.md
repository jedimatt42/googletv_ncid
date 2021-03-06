googletv_ncid
=============

## Network Caller ID client for Google TV

Requires an NCID server running somewhere on your home network.
See http://ncid.sourceforge.net/ for NCID server software.

Configuration accepts host and port values for NCID server.

The application reads incoming caller id information from the NCID server, and displays in a 
blue translucent overlay at the bottom of the TV for a short period of time. 

The application is fairly robust. It will automatically re-establish a connection to the NCID server
if it fails or the server goes offline and returns. The connection is established at boot on the google tv 
device. An ongoing notification indicates the connected status.

I have tested this on the Sony NSZ-GS7 Internet Player with Google TV.

The binary .APK for a Google TV branded device is available in the google play store:
  https://play.google.com/store/apps/details?id=net.cwfk.ig88.ncid

A binary for less official android TV mini pc devices can be found at:
  http://www.mediafire.com/file/fcx2n76t52liz99/gtv_ncid.apk

## Known deficiencies:
  + PLEX pauses playback when overlay appears. User has to wait for overlay to dismiss and then press play again.

## Acknowledgements
basis for application icon from http://hadezign.com

-M@

