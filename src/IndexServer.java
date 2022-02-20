
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
   private ArrayList<FileConstructor> FileServer;
   public IndexServer() throws RemoteException    
   {    
      super();    
     FileServer=new ArrayList<FileConstructor>();
   }    
    
     public synchronized void registerFiles(String peerId, String fileName,String portnum,String srcDir) throws RemoteException {
        FileConstructor new_file = new FileConstructor();
        new_file.peerId=peerId;
        new_file.FileName=fileName;
        new_file.portNumber=portnum;
        new_file.SrcDirectory=srcDir;
    	this.FileServer.add(new_file);
    	
    	System.out.println("Registered file: "+ new_file.FileName +" from client with peerID "+ new_file.peerId);
    	System.out.println("Port #: "+ new_file.portNumber +" 	Directory: "+ new_file.SrcDirectory);
        
     }
     
     public synchronized void deregisterFiles(String peerId, String fileName,String portnum,String srcDir) throws RemoteException {
        FileConstructor rf = new FileConstructor();
        rf.peerId=peerId;
        rf.FileName=fileName;
        rf.portNumber=portnum;
        rf.SrcDirectory=srcDir;
    	this.FileServer.remove(rf);   
    	
    	System.out.println("Deregistered "+ rf.FileName +" from client with peerID "+ rf.peerId);
    	System.out.println("Port #: "+ rf.portNumber +" 	Directory: "+ rf.SrcDirectory);
     }
   
    public ArrayList<FileConstructor> search(String filename) throws RemoteException {
        ArrayList<FileConstructor> MatchList= new ArrayList<FileConstructor>();
        for(int i=0;i<this.FileServer.size();i++)
        {
            if(filename.equalsIgnoreCase(FileServer.get(i).FileName))
            {
                MatchList.add(FileServer.get(i));
            }
        }
       return (MatchList) ; 
    }
}    
