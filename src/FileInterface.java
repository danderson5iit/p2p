
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileInterface extends Remote {
	public byte[] fileDownload(String fileName) throws RemoteException;
}
