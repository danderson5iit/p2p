How to run:

Compilation:

javac -d {working directory} filename.java

example:

javac ClientInterface.java ConcurrencyTest.java FileConstructor.java FileFunction.java FileInterface.java IndexServer.java PeerClient.java ServerInterface.java StartClient.java StartServer.java 

Start the server:

java StartServer

Open new terminal to start the client:

java StartClient


Testing notes:
- I copied the first half of peer1's files into peer2 and the second half into peer3 so there would
be some overlap when picking peers. You can copy some more files into different peer directories like
some between peers 2 and 3 so there is more of a variety when picking peers for the test cases.
- If theres a duplicate peer number when it asks to display a peer, something like
"Pick the peer you want to download"
1
2
2
3

That is because theres a bug where I added the new registration so that registers every file as new and
created duplicate entries. There is some commented out code where I tried to fix this but couldn't get it
to work the way I wanted so I just left it in. Maybe we just try and structure the test cases so this
case doesnt happen so we don't have to explain that, or I could talk about it in the improvements 
section. This will only happen if you try and download a file that a peer already has.

We should probably have a test case for each peer to peer file download to prove it works between all of
them, make sure to take screenshots of both the confirmation message on the client side and the server 
output that will update the files registered to the server. If you're testing the deregister function 
then make sure to screenshot the client server where it says 'deregistered file from peer' so they know
it's working. 


Cases that dont have working properly:
- files from a deregistered peer are still accessible by the other registered peers until the connection
is broken by ctrl C
    - this could be fine, we could just make sure to fully disconnect the server in the test cases and 
    then show that an error occurs when another peer tries to download from one that is already 
    disconnected. For this you would just register the peers, take screenshots of the files registerd,
    deregister one of the peers by typing exit, take screenshots of the deregistered outputs, type ctrl C
    to disconnect peer and then try and download from the deregistered peer. It will still show the peer 
    number as an option but it will error out before the download can happen. We can put this in the 
    improvements for better error handling for this case.

