import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StartClient{
	static String portnum=null;	
	static String directory=null;
	static String peerId=null;
	static ServerInterface peerserver=null;
	
public static void main(String [] args) throws IOException, NotBoundException{
	try{
	ServerInterface peerserver = (ServerInterface) Naming.lookup("index_server"); 
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Please enter a port number to register new Peer: ");
	portnum=input.readLine();
	System.out.println("Enter the file directory path: ");
	String directory = input.readLine();
	System.out.println("Please enter the peer ID associated with this new client: ");
	String peerId = input.readLine();
	
	PeerClient clientserver = new PeerClient(portnum, directory, peerId, peerserver);
	LocateRegistry.createRegistry(Integer.parseInt(portnum));
	FileInterface fi = new FileFunction(directory);
	Naming.rebind("rmi://localhost:"+portnum+"/client_server", fi);
		
	Thread thread1 = new Thread(new PeerClient(portnum, directory, peerId, peerserver));
	thread1.start();  

	} catch(Exception e) {
         System.err.println("FileServer exception: "+ e.getMessage());
         e.printStackTrace();
      }
	}

}

