
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
     public byte[] downloadFile(String fileName) throws
   RemoteException;

}
