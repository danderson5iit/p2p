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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StartClient implements Runnable{
	String portno=null;
    String directoryName=null;
    String fileTobeSearched=null;
    BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
	StartClient(String portno,String directoryName){
		this.portno=portno;
		this.directoryName=directoryName;
	}
	public void run(){        
		String peerID=null;
		try   
		{    
			ServerInterface index = (ServerInterface) Naming.lookup("index_server");    
		   
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line;
			System.out.println("Please enter the peer ID associated with this new client: ");
			peerID = br.readLine();
			File directoryList = new File(directoryName);
			String[] store = directoryList.list();
			int counter=0;
			while(counter<store.length){
			File currentFile = new File(store[counter]);
				try {
					index.registerFiles(peerID, currentFile.getName(),portno,directoryName);
				} catch (RemoteException ex) {
Logger.getLogger(StartClient.class.getName()).log(Level.SEVERE, null, ex);
				}
				counter++;
			}
			// method to search for the file
			ArrayList<FileDetails> arr = new ArrayList<FileDetails>();
			System.out.println("Please enter the file you would like to download: ");
			fileTobeSearched = br.readLine();
			arr=index.search(fileTobeSearched);
			while(arr.size() == 0){
				System.out.println("File not found. Try Again");
				fileTobeSearched = br.readLine();
				arr=index.search(fileTobeSearched);
			}
			System.out.println("The following PeerIDs contain the file: ");
			for(int i = 0; i < arr.size(); i++) {
				System.out.println(arr.get(i).peerId);
			}
			System.out.println("Please enter the ID for the peer you would like to download from: ");
			peerID= br.readLine();
            downloadFromPeer(peerID,arr);
            String nextMove = null;
            System.out.println("Press any key to start new download, type 'exit' to quit");
            nextMove = br.readLine();
            while(nextMove != null){
            	if(nextMove.equals("exit")){
            		System.out.println("Exiting...");
            		break;
            	}
            	ArrayList<FileDetails> next_arr = new ArrayList<FileDetails>();
				System.out.println("Please enter the file you would like to download: ");
				fileTobeSearched = br.readLine();
				next_arr=index.search(fileTobeSearched);
				while(next_arr.size() == 0){
					System.out.println("File not found. Try Again");
					fileTobeSearched = br.readLine();
					next_arr=index.search(fileTobeSearched);
				}
				System.out.println("The following PeerIDs contain the file: ");
				for(int i = 0; i < arr.size(); i++) {
					System.out.println(next_arr.get(i).peerId);
					}
				System.out.println("Please enter the ID for the peer you would like to download from: ");
				peerID= br.readLine();
                downloadFromPeer(peerID,next_arr);
                System.out.print("Press any key to start new download, type 'exit' to quit");
            	nextMove = br.readLine();
            }   
			}	catch (Exception e){
				System.out.println("ClientInterface exception: " + e);
			}       
}
public void downloadFromPeer(String peerid,ArrayList<FileDetails> arr) throws NotBoundException, RemoteException, MalformedURLException, IOException{
  //get port
	String portForAnotherClient=null;
	String sourceDir=null;
	for(int i=0;i<arr.size();i++){
      if(peerid.equals(arr.get(i).peerId)){
          portForAnotherClient=arr.get(i).portNumber;
          sourceDir=arr.get(i).SourceDirectoryName;
		}
	}
	ClientInterface peerServer = (ClientInterface) Naming.lookup("rmi://localhost:"+portForAnotherClient+"/FileServer");
	
	String source = sourceDir+"//"+fileTobeSearched;//directory where file will be copied
	String target =directoryName;
	InputStream is = null;
    OutputStream os = null;
    
    try {
        File srcFile = new File(source);
        File destFile = new File(target);
        if(!destFile.exists())
        {
        	System.out.println("Could not find file.");
            destFile.createNewFile();
        }
        
        System.out.println("downloading file... ");
        is = new FileInputStream(srcFile);
        
        os = new FileOutputStream(target+"//"+srcFile.getName());
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        System.out.println(srcFile.getName() + " Successfully Downloaded.");
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
    
     BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
     String portno=null;
     System.out.println("Please enter a port number to register new Peer: ");
     portno=inp.readLine();
     
     System.out.println("Enter the file directory path: ");
     String directoryName = inp.readLine();
     try{
     	LocateRegistry.createRegistry(Integer.parseInt(portno));
     	ClientInterface fi = new FileImpl(directoryName);
     	Naming.rebind("rmi://localhost:"+portno+"/FileServer", fi);
 } catch(Exception e) {
         System.err.println("FileServer exception: "+ e.getMessage());
         e.printStackTrace();
      }
	new StartClient(portno,directoryName).run();
	}
}
