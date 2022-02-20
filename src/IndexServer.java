
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
   private ArrayList<FileDetails> FileServer;
   public IndexServer() throws RemoteException    
   {    
      super();    
     FileServer=new ArrayList<FileDetails>();
   }    
    
     public synchronized void registerFiles(String peerId, String fileName,String portnum,String srcDir) throws RemoteException {
        FileDetails fd = new FileDetails();
        fd.peerId=peerId;
        fd.FileName=fileName;
        fd.portNumber=portnum;
        fd.SrcDirectory=srcDir;
    	this.FileServer.add(fd);
    	
    	System.out.println("Registered file: "+ fd.FileName +" from client with peerID "+ fd.peerId);
    	System.out.println("Port #: "+ fd.portNumber +" 	Directory: "+ fd.SrcDirectory);
        
     }
     
     public synchronized void deregisterFiles(String peerId, String fileName,String portnum,String srcDir) throws RemoteException {
        FileDetails rf = new FileDetails();
        rf.peerId=peerId;
        rf.FileName=fileName;
        rf.portNumber=portnum;
        rf.SrcDirectory=srcDir;
    	this.FileServer.remove(rf);   
    	
    	System.out.println("Deregistered "+ rf.FileName +" from client with peerID "+ rf.peerId);
    	System.out.println("Port #: "+ rf.portNumber +" 	Directory: "+ rf.SrcDirectory);
     }
   
    public ArrayList<FileDetails> search(String filename) throws RemoteException {
        ArrayList<FileDetails> FilesMatched= new ArrayList<FileDetails>();
        for(int i=0;i<this.FileServer.size();i++)
        {
            if(filename.equalsIgnoreCase(FileServer.get(i).FileName))
            {
                FilesMatched.add(FileServer.get(i));
            }
        }
       return (FilesMatched) ; 
    }
}    
