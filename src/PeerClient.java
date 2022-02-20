import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.OutputStream;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class PeerClient extends UnicastRemoteObject implements ClientInterface, Runnable
{       
	public String portnum =null;
    public String directory =null;
    public String download_file =null;
    
    private ServerInterface peerServer;
    
	protected PeerClient() throws RemoteException    
	{    
		super();    
	} 
   
	public PeerClient(String portnum, String directory, ServerInterface peerServer) throws RemoteException{
		this.portnum=portnum;
		this.directory=directory;
		this.peerServer = peerServer;
	}
		public void run(){    
		try   
		{    
			//System.out.println("Current Thread: " + Thread.currentThread().getId()); 
			
			String peerID=null;
			String peerID_download=null;
			ServerInterface index = (ServerInterface) Naming.lookup("index_server");    	   
			System.out.println("Please enter the peer ID associated with this new client: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			peerID = br.readLine();
			File dir_file = new File(directory);
			String[] dir_list = dir_file.list();
			
			int counter=0;
			while(counter<dir_list.length){
			File currentFile = new File(dir_list[counter]);
				try {
					index.registerFiles(peerID, currentFile.getName(),portnum,directory);
				} catch (RemoteException ex) {
Logger.getLogger(StartClient.class.getName()).log(Level.SEVERE, null, ex);
				}
				counter++;
			}
			
			// search
			ArrayList<FileDetails> found = new ArrayList<FileDetails>();
			System.out.println("Please enter the file you would like to download: ");
			download_file = br.readLine();
			found = index.search(download_file);
			while(found.size() == 0){
				System.out.println("File not found. Try Again");
				download_file = br.readLine();
				found = index.search(download_file);
			}
			System.out.println("The following PeerIDs contain the file: ");
			for(int i = 0; i < found.size(); i++) {
				System.out.println(found.get(i).peerId);
			}
			System.out.println("Please enter the ID for the peer you would like to download from: ");
			peerID_download= br.readLine();
			if(peerDownload(peerID_download,found)){
                	index.registerFiles(peerID_download, download_file,portnum,directory);
                }
            String nextMove = null;
            System.out.println("Press any key to start new download, type 'exit' to quit");
            
            nextMove = br.readLine();
            while(nextMove != null){
            	if(nextMove.equals("exit")){
            		System.out.println("Exiting...");
            		String[] rmfiles = dir_file.list();
            		int del_count=0;
            		while(del_count<rmfiles.length){
						File currFile = new File(rmfiles[del_count]);
						try {
							index.deregisterFiles(peerID, currFile.getName(),portnum,directory);
						} catch (RemoteException ex) {
Logger.getLogger(StartClient.class.getName()).log(Level.SEVERE, null, ex);
				}
				del_count++;
			}
            	System.out.println("Peer successfully deregistered. Type 'ctrl C' to exit.");
            	break;
            	
            	}
            	ArrayList<FileDetails> found2 = new ArrayList<FileDetails>();
				System.out.println("Please enter the file you would like to download: ");
				download_file = br.readLine();
				found2=index.search(download_file);
				while(found2.size() == 0){
					System.out.println("File not found. Try Again");
					download_file = br.readLine();
					found2=index.search(download_file);
				}
				System.out.println("The following PeerIDs contain the file: ");
				for(int i = 0; i < found2.size(); i++) {
					System.out.println(found2.get(i).peerId);
					}
				System.out.println("Please enter the ID for the peer you would like to download from: ");
				peerID_download= br.readLine();
                if(peerDownload(peerID_download,found2)){
                	index.registerFiles(peerID_download, download_file,portnum,directory);
                }
                System.out.print("Press any key to start new download, type 'exit' to quit");
            	nextMove = br.readLine();
            }   
			}	catch (Exception e){
				System.out.println("ClientInterface exception: " + e);
			}       
}

public boolean peerDownload(String peerid,ArrayList<FileDetails> arr) throws NotBoundException, RemoteException, MalformedURLException, IOException{
  //get port
  	long start = System.nanoTime();
  	 
	String peerPort=null;
	String sourceDir=null;
	
	for(int i=0;i<arr.size();i++){
      if(peerid.equals(arr.get(i).peerId)){
          peerPort=arr.get(i).portNumber;
          sourceDir=arr.get(i).SrcDirectory;
		}
	}
	
	String source = sourceDir + "//" + download_file;
	String target = directory;
	InputStream is = null;
    OutputStream os = null;
    
   	File srcFile = new File(source);
    File destFile = new File(target);
   
// if(!destFile.exists()){
    	try {
        System.out.println("downloading file... ");
        is = new FileInputStream(srcFile);
        os = new FileOutputStream(target + "//" + srcFile.getName());
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        System.out.println(srcFile.getName() + " Successfully Downloaded.");
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("Download time = " + timeElapsed + "ns");
    } 
      catch(Exception e)
            {
            e.printStackTrace();
            }  
    finally {
        is.close();
        os.close();
    }
    return true;
    //}else{
    	//System.out.println("Duplicate File");
    	//return false;
  //}
}
}

