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
	static String portno=null;	
	static String directoryName=null;
	
	public static void main(String [] args) throws IOException{
		BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Please enter a port number to register new Peer: ");
		portno=inp.readLine();
		System.out.println("Enter the file directory path: ");
		String directoryName = inp.readLine();
		//String loc = "rmi//localhost:"+portnum+"/peerserver";
		PeerClient clientserver = new PeerClient(portno, directoryName);
		
		
		new Thread(new peerfunction()).start();
		new Thread(clientserver).start();
		System.out.println("Current Thread: " + Thread.currentThread().getId());    
		//Thread thread1 = new Thread(clientserver);
		//new Thread(thread1).start();	 	
	}
	static class peerfunction implements Runnable{
	
	public void run()
	{
		try{
		LocateRegistry.createRegistry(Integer.parseInt(portno));
		ClientInterface fi = new FileImpl(directoryName);
		Naming.rebind("rmi://localhost:"+portno+"/clientserver",fi);
	 	
	 	} catch(Exception e) {
	 		System.err.println("FileServer exception: "+ e.getMessage());
	 		e.printStackTrace();
	 	}
	 }

}
}

