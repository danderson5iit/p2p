import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.rmi.Remote;
import java.rmi.RemoteException;
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

public class PeerClient extends UnicastRemoteObject implements ClientInterface, Runnable
{
	public String portno=null;
    public String directoryName=null;
    public String fileTobeSearched=null;

    //private ClientInterface peerServer;

	protected PeerClient() throws RemoteException
	{
		super();
	}

	public PeerClient(String portno, String directoryName) throws RemoteException{
		this.portno=portno;
		this.directoryName=directoryName;
	}
		public void run(){
		String peerID=null;
		String peerID2=null;

		try
		{
			ServerInterface index = (ServerInterface) Naming.lookup("index_server");
			System.out.println();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line;
			System.out.println("Please enter the peer ID associated with this new client: ");
			peerID = br.readLine();
			peerID2 = peerID;

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
						//new edit
						downloadFromPeer(peerID, peerID2, arr, index, portno, directoryName,fileTobeSearched);
            //downloadFromPeer(peerID,arr);
            String nextMove = null;
            System.out.println("Press any key to start new download, type 'exit' to quit");

            nextMove = br.readLine();
            while(nextMove != null){
            	if(nextMove.equals("exit")){
            		System.out.println("Exiting...");
            		String[] rmfiles = directoryList.list();
            		int del_count=0;
            		while(del_count<rmfiles.length){
						File currFile = new File(rmfiles[del_count]);
						try {
							index.deregisterFiles(peerID, currFile.getName(),portno,directoryName);
						} catch (RemoteException ex) {
							Logger.getLogger(StartClient.class.getName()).log(Level.SEVERE, null, ex);
				}
				del_count++;
			}
            	System.out.println("Peer successfully deregistered. Type 'ctrl C' to exit.");
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
                downloadFromPeer(peerID,peerID2,next_arr,index,portno,directoryName,fileTobeSearched);
                System.out.print("Press any key to start new download, type 'exit' to quit");
            	nextMove = br.readLine();
            }
			}	catch (Exception e){
				System.out.println("ClientInterface exception: " + e);
			}
}

/*
public void regi() {
	try {
		index.registerFiles(peerID, currentFile.getName(),portno,directoryName);
	} catch (RemoteException ex) {
		Logger.getLogger(StartClient.class.getName()).log(Level.SEVERE, null, ex);
	}
}
*/


//downloadFromPeer(peerID, arr, index, portno, directoryName,currentFile);
public void downloadFromPeer(String peerid, String peerid2, ArrayList<FileDetails> arr,ServerInterface index,String portno,String directoryName,String currentFile) throws NotBoundException, RemoteException, MalformedURLException, IOException{
  //get port
	String portForAnotherClient=null;
	String sourceDir=null;
	for(int i=0;i<arr.size();i++){
      if(peerid.equals(arr.get(i).peerId)){
          portForAnotherClient=arr.get(i).portNumber;
          sourceDir=arr.get(i).SourceDirectoryName;
	}
	//peerid, arr, index, portno, directoryName, currentFile
	try {
		index.registerFiles(peerid2,currentFile,portno,directoryName);
	} catch (RemoteException ex) {
		Logger.getLogger(StartClient.class.getName()).log(Level.SEVERE, null, ex);
	}

}
	//ClientInterface peerServer = (ClientInterface) Naming.lookup("rmi://localhost:"+portForAnotherClient+"/FileServer");

	String source = sourceDir+"//"+fileTobeSearched;
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
}
