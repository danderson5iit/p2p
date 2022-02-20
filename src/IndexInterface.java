import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IndexInterface extends Remote{

 public void registerFiles(String peerId, String filename,String portno,String srcDir)throws RemoteException; 
 public void deregisterFiles(String peerId, String fileName,String portno,String srcDir) throws RemoteException;
}