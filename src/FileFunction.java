import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

    public class FileFunction extends UnicastRemoteObject
  implements FileInterface {

   private String directory;

   public FileFunction(String dir) throws RemoteException{
      super();
      directory = dir;
   }

   public byte[] fileDownload(String new_file){
      try {
         File file = new File(directory+ "/" + new_file);
         byte buffer[] = new byte[(int)file.length()];
         BufferedInputStream input = new
      BufferedInputStream(new FileInputStream(directory+ "//" + new_file));
         input.read(buffer,0,buffer.length);
         input.close();
         return(buffer);
      } catch(Exception e){
         System.out.println("FileImpl: "+e.getMessage());
         e.printStackTrace();
         return(null);
      }
   }
}

 

