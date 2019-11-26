// Java implementation for multithreaded chat client 
// Save file as Client.java 
  
import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
  
public class CClient  
{ 
    final static int ServerPort = 1234; 
    public int dhmPubKey;
    public int dhmMixKey;
    public boolean encryptActive;
    public class diffieHellman(int a)
    {
      public void initreq()
      {
        //Generate Public and MixKey1
        
      }
      public void ackreq(int pubkey,int mixkey1)
      {
        //Recieved Pubkey and MixKey1.Process Goldkey. Send MixKey2.
        int mixkey2;

        return mixkey2;
      }
      public int ackrecd(int ackreq)
      {
        int goldkey;
        //Recieved AckReq. Process Goldkey.
        return goldkey;
      }
      /*
      Here a=0 means encryption request is initiated by the user.
      Here a=1 means encryption request is recieved by the user.
      */
    }
    public static void main(String args[]) throws UnknownHostException, IOException  
    { 
        Scanner scn = new Scanner(System.in); 
          
        // getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost"); 
          
        // establish the connection 
        Socket s = new Socket(ip, ServerPort); 
          
        // obtaining input and out streams 
        DataInputStream dis = new DataInputStream(s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
  
        // sendMessage thread 
        Thread sendMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
                while (true) { 
  
                    // read the message to deliver. 
                    String msg = scn.nextLine(); 
                    
                    try { 
                        // write on the output stream 
                        dos.writeUTF(msg); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
          
        // readMessage thread 
        Thread readMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
  
                while (true) { 
                    try { 
                        // read the message sent to this client 
                        String msg = dis.readUTF(); 
                        System.out.println(msg); 
                        if(msg=="go secure")
                        {
                          //DHM init req recd.

                        }
                    } catch (IOException e) { 
  
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
  
        sendMessage.start(); 
        readMessage.start(); 
  
    } 
}