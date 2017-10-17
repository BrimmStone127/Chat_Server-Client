import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer{
   
   //attributes
   Vector<PrintWriter> printarray = new Vector<PrintWriter>();
   int nullcounter;
   int personcounter;
   
   //main
   public static void main(String[]args){
      new ChatServer();
   }//end of main
   
   //constructor
   public ChatServer(){
      
      try{
         //create ServerSocket
         ServerSocket ss = new ServerSocket(16789);
         
         while( true ){      
            // wait for a client to connect, snore....
            System.out.println("Waiting for a client...");
                     
            Socket cs = ss.accept(); // This waits for client
                     
            System.out.println("We have a client: "+cs );
                     
            // create a thread for the client and start
            ThreadedServer ts = new ThreadedServer( cs );
            personcounter++;
            ts.start();
            }
            
          }catch(IOException ioe){
            System.out.println("IO ERROR");
          }
      
   }//end of constructor
   
   //Threaded InnerClass
   class ThreadedServer extends Thread{
      
      //attributes
      private Socket cs;
         
      //constructor
      public ThreadedServer( Socket _cs ){
         cs = _cs;
         System.out.println("Entered Thread");
      }
      
      public void run(){
         
         try{
            
            int numId = personcounter;
             // open the input stream 
            BufferedReader br = new BufferedReader(
                                 new InputStreamReader(
                                    cs.getInputStream()));
                                  
            // open the output stream
            PrintWriter pout = new PrintWriter(
                                 new OutputStreamWriter(
                                    cs.getOutputStream()));
            
            System.out.println("Welcomed");
            pout.println("Welcome to ClayChat");
            pout.flush();
                       
            //add printwriter to array
            printarray.add(pout);
            
            while(true){
               String newmsg;
               newmsg = br.readLine();
               System.out.println(newmsg);
               if (newmsg.equals(null)||newmsg.equals("")){
                  nullcounter++;
                  if(nullcounter > 5){
                     cs.close();
                  }
               }
               else{
                  for(PrintWriter pw : printarray){
                     pw.println(numId + ": " + newmsg);
                     pw.flush();
                  }
               }
            }//end of while loop
            
         }catch(IOException ioe){
            System.out.println("A USER HAS EXITED ");
         }      
      }//end of Run
   }//end of InnerClass
}//End of ChatServer