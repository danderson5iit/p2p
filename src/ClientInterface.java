
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface ClientInterface extends Remote {
	public boolean peerDownload(String peerid,ArrayList<FileDetails> arr) throws NotBoundException, RemoteException, MalformedURLException, IOException;
}
