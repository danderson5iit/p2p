import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConcurrencyTest implements Runnable{
    String portnum=null;
    String directory=null;
    String peerId=null;
    
    long responseTime=0;
    long start =0;
    long endTime=0;
    
    BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
    ConcurrencyTest(String portnum,String directory,String peerId)
     {
     	this.portnum=portnum;
     	this.directory=directory;
     	this.peerId=peerId;
     }
     public void run(){
     	System.out.println("Current Thread: " + Thread.currentThread().getId());
     	try
     	{
     	ServerInterface index = (ServerInterface) Naming.lookup("index_server");
     	System.out.println("Connected to Server.");
     	File directoryList = new File(directory);
     	String[] store = directoryList.list();
     	int counter=0;
     	while(counter<store.length)
     	{
     		File currentFile = new File(store[counter]);
     		try {
     			index.registerFiles(peerId, currentFile.getName(),portnum,directory);
     		} catch (RemoteException ex) {
     Logger.getLogger(StartClient.class.getName()).log(Level.SEVERE, null, ex);
	}
		counter++;
		}
		// method to search for the file
		start = System.nanoTime();
		peerDownload("/home/seed/p2p_OG/testing","4","test.txt");
		endTime= System.nanoTime();
		responseTime= endTime - start;
		System.out.println("Time Elapsed: " + responseTime);  
		}    
		catch (Exception e){    
		   System.out.println("ClientInterface exception: " + e);    
		}
	}
	
public void peerDownload(String srcDir,String peerid,String fileName) throws NotBoundException, RemoteException, MalformedURLException, IOException{
	String source = srcDir+"//"+fileName;
	//directory where file will be copied
	String target = directory;
	InputStream is = null;
    	OutputStream os = null;
    try {
        File srcFile = new File(source);
        File destFile = new File(target);
        System.out.println("file "+destFile);
        if(!destFile.exists())
        {
        	destFile.createNewFile();
        }
        is = new FileInputStream(srcFile);
        os = new FileOutputStream(target+"//"+srcFile.getName());
        
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    } 
      catch(Exception e)
            {
            e.printStackTrace();
            }
    finally {
        is.close();
        os.close();
    }
  }
public static void main(String [] args) throws IOException{
    
    String port1="4001";
    String port2="4003";
    String port3="4005";
    String d1="/home/seed/p2p_OG/peer1";
    String d2="/home/seed/p2p_OG/peer2";
    String d3="/home/seed/p2p_OG/peer3";
          
     try{
         LocateRegistry.createRegistry(Integer.parseInt(port1)); 
         LocateRegistry.createRegistry(Integer.parseInt(port2)); 
         LocateRegistry.createRegistry(Integer.parseInt(port3)); 
         
       FileInterface fi1 = new FileFunction(d1);
       FileInterface fi2= new FileFunction(d2);
       FileInterface fi3 = new FileFunction(d3);
       
       Naming.rebind("rmi://localhost:"+port1+"/FileServer", fi1);
       Naming.rebind("rmi://localhost:"+port2+"/FileServer", fi2);
       Naming.rebind("rmi://localhost:"+port3+"/FileServer", fi3);
    
 } catch(Exception e) {
         System.err.println("FileServer exception: "+ e.getMessage());
         e.printStackTrace();
      }
  
      Thread thread1 = new Thread(new ConcurrencyTest(port1,d1,"1"));  
     Thread thread2 = new Thread(new ConcurrencyTest(port2,d2,"2"));  
     Thread thread3 = new Thread(new ConcurrencyTest(port3,d3,"3"));  
     thread1.start();
     thread2.start();
     thread3.start();
	}
    
}
