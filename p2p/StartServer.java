import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;    

public class StartServer implements Runnable   
{   
   public void run()    
   {    
      try   
      {
      	//System.setProperty("java.rmi.server.hostname","localhost");
         LocateRegistry.createRegistry(1099);    
         IndexServer index = new IndexServer();    
         Naming.rebind("index_server", index);    
         System.out.println("Index Server ready, Please register Peers.");    
      }    
      catch (Exception e)    
      {    
         System.out.println("Index Server Failed to start: " + e);    
      }    
   }    
   
   public static void main(String [] args)    
   {    
	 new StartServer().run();
   } 
}   



