// Java implementation for multithreaded chat client 
// Save file as Client.java 
import java.io.*; 
import java.net.*; 
import java.util.*; 
import java.lang.*;
import com.chilkatsoft.CkZip;
public class CClient  
{ 
    final static int ServerPort = 1234; 
    
    
    private static class diffieHellman
    {
      public int dhmGenerator;
      public int dhmModPrime; 
      public int dhmMixKey;
      public int dhmGoldKey;
      public boolean encryptActive=false;
      public diffieHellman()
      {
        dhmPubKey=1;
        dhmMixKey=2;
        dhmGoldKey=3;
      }
      public void initreq()
      {
        //Generate Public and MixKey1
        dhmGenerator=5;
        dhmModPrime=3;
      }
      public void initack(int pubkey,int mixkey1)
      {
        //Recieved Pubkey and MixKey1.Process Goldkey. Send MixKey2.
        
      }
      public int ackrecd(int ackreq)
      {
        int goldkey=0;
        //Recieved AckReq. Process Goldkey.

        return goldkey;
      }
    };
    public static String incrChar(String word,int x) {
        StringBuffer b = new StringBuffer();
        char[] chars = word.toCharArray();
        for (char c : chars) {
            if(c != ' ')
                c = (char) (c + x);
            b.append(c);
        }
        return b.toString();
    }
    public static String decrChar(String word,int x) {
        StringBuffer b = new StringBuffer();
        char[] chars = word.toCharArray();
        for (char c : chars) {
            if(c != ' ')
                c = (char) (c - x);
            b.append(c);
        }
        return b.toString();
    }
    public static void main(String args[]) throws UnknownHostException, IOException  
    { 
        System.load("libchilkat.so");
        Scanner scn = new Scanner(System.in); 
        diffieHellman dhm=new diffieHellman();
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
                        if(dhm.encryptActive && !msg.equals("logout"))
                        {
                          StringTokenizer st = new StringTokenizer(msg, "#"); 
                          String MsgToSend = st.nextToken(); 
                          String recipient = st.nextToken(); 
                          MsgToSend=incrChar(MsgToSend,dhm.dhmGoldKey);
                          msg=MsgToSend+"#"+recipient;
                        }

                        if(msg.contains("go secure") && !(dhm.encryptActive))
                        {
                          System.out.println("Requesting user to use DHM Encryption...");
                          dhm.initreq();
                          msg=dhm.dhmPubKey+"."+dhm.dhmMixKey+"@"+msg;
                          //Person initiates DHM encryption.
                        }
                        else if(msg.contains("secure") && !(dhm.encryptActive))
                        {
                          //Sender replies confirming encryption. MixKey of sender is parsed in.
                          System.out.println("Accepting Client Request to use DHM Encryption. You are now secure");
                          msg=dhm.dhmMixKey+"@"+msg;
                          dhm.encryptActive=true;
                        }
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
                boolean logmsg=false;
                while (true) { 
                    try { 
                        // read the message sent to this client 
                        String msg = dis.readUTF(); 
                        
                        if(msg.contains("go secure") && !(dhm.encryptActive))
                        {
                          System.out.println("Your partner is requesting to go secure. Please confirm by typing secure.");
                          //DHM init req recd.
                          dhm.initack(0,0);
                          logmsg=true;
                        }

                        else if(msg.contains("secure") && !(dhm.encryptActive))
                        {
                          //Parse out the MixKey of sender.
                          System.out.println("You are now secure.");
                          dhm.ackrecd(0);
                          logmsg=true;
                          dhm.encryptActive=true;
                        }

                        if(dhm.encryptActive)
                        {
                          StringTokenizer st = new StringTokenizer(msg, ":"); 
                          String SenderName = st.nextToken(); 
                          logmsg=true;
                          String SenderMsg = st.nextToken(); 
                          SenderMsg=decrChar(SenderMsg,dhm.dhmGoldKey);
                          msg=SenderName+" : "+SenderMsg;
                        }
                    
                          System.out.println(msg); 
                          logmsg=false;
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