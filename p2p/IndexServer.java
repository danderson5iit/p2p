
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IndexServer extends UnicastRemoteObject implements ServerInterface    
{       
   private ArrayList<FileDetails> Files;
   public IndexServer() throws RemoteException    
   {    
      super();    
     Files=new ArrayList<FileDetails>();
   }    
    
     public synchronized void registerFiles(String peerId, String fileName,String portno,String srcDir) throws RemoteException {
        FileDetails fd = new FileDetails();
        fd.peerId=peerId;
        fd.FileName=fileName;
        fd.portNumber=portno;
        fd.SourceDirectoryName=srcDir;
    	this.Files.add(fd);
    	
    	System.out.println("Registered "+ fd.FileName +" to client with peerID "+ fd.peerId);
    	System.out.println("Port #: "+ fd.portNumber +" 	Directory: "+ fd.SourceDirectoryName);
        
     }
     
     public synchronized void deregisterFiles(String peerId, String fileName,String portno,String srcDir) throws RemoteException {
        FileDetails rf = new FileDetails();
        rf.peerId=peerId;
        rf.FileName=fileName;
        rf.portNumber=portno;
        rf.SourceDirectoryName=srcDir;
    	this.Files.remove(rf);   
    	
    	System.out.println("Deregistered "+ rf.FileName +" from client with peerID "+ rf.peerId);
    	System.out.println("Port #: "+ rf.portNumber +" 	Directory: "+ rf.SourceDirectoryName);
     }
   
    public ArrayList<FileDetails> search(String filename) throws RemoteException {
        ArrayList<FileDetails> FilesMatched= new ArrayList<FileDetails>();
        for(int i=0;i<this.Files.size();i++)
        {
            if(filename.equalsIgnoreCase(Files.get(i).FileName))
            {
                FilesMatched.add(Files.get(i));
            }
        }
       return (FilesMatched) ; 
    }

   
    public void calculateAvgResponseTime(String fileName, String peerId,String portNo, String srcDir) throws RemoteException{
    
    }
}    
