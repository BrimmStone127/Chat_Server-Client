import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatClient extends JFrame{
   //attributes
   private JTextArea  jtaChat;
   private JTextField jtfInput;
   private JButton    jbSend;
   private JPanel     jpTextPanel;
   private JPanel     jpSendPanel;
   PrintWriter pout;
   BufferedReader br;
  
    
   //Main
   public static void main(String[]args){                  
         new ChatClient();         
   }//End of Main
   
   //Constructo
   public ChatClient(){
      
      try{
         //create a socket
         Socket s = new Socket("localhost",16789);//129.21.108.36
         
         //create inputstream
         InputStream in = s.getInputStream();
         br = new BufferedReader(new InputStreamReader( in ));      
       
         // open the output stream
         OutputStream out = s.getOutputStream();
         pout = new PrintWriter(new OutputStreamWriter( out ));
                  
      }catch( UnknownHostException uhe){
            System.out.println("Unknown host, fix and try again.");
      }catch( IOException ioe){
            jtaChat.append("There has been an expected issue");
      }

   
      setTitle("Chat Client V.1.0");
      
      jtaChat = new JTextArea(10,30);
      
      jtaChat.setLineWrap( true );
      jtaChat.setWrapStyleWord( true );
      jtaChat.setEditable(false);
      
      jpSendPanel = new JPanel();
      jpTextPanel = new JPanel();
      
		jpTextPanel.setLayout(new GridLayout(1,1));
      
      JScrollPane jbSendPane = new JScrollPane(jtaChat);
      
      jpTextPanel.add(jbSendPane);
      
      jtfInput = new JTextField("Enter Message",20);
      jtfInput.setEditable(true);
      
      JButton jbExit = new JButton("Exit");
      jbExit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e){
            pout.println("This Client has Left.");
            pout.flush();
            System.exit(0);
            }
            });
      jbSend = new JButton("Send");
      jbSend.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent e) { 
         jbSend.setEnabled(false);
         String msg = jtfInput.getText();
         System.out.println("button pressed"+msg);
         pout.println(msg);
         pout.flush();
         jbSend.setEnabled(true);
        } 
      } );
      
      jpSendPanel.add(jtfInput);
      jpSendPanel.add(jbSend);
      jpSendPanel.add(jbExit);
      
      add(jpTextPanel,  BorderLayout.CENTER);
      add(jpSendPanel,  BorderLayout.SOUTH);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      InnerThread inn = new InnerThread(jtaChat, br);
      Thread it = new Thread(inn);
      it.start();
 
      pack();
		setLocationRelativeTo(null);
		setVisible(true);
      
      
      
   }//end of constructor
   
   //InnerClass
   class InnerThread extends JPanel implements Runnable{
      //attributes
      String incoming;
      
      public InnerThread(JTextArea _jta, BufferedReader _br){
         jtaChat = _jta;
         br = _br;
      }
      
      //run method
      public void run(){
         
         System.out.println("Entered Thread");
         
         while(true){
            
           try{       
               incoming = br.readLine();       
               System.out.println(incoming);            
               jtaChat.append(incoming);
               jtaChat.setText(jtaChat.getText() + "\n");           
           }catch(IOException ioe){jtaChat.append("THERE HAS BEEN AN UNEXPECTED ISSUE PLEASE ExIT..........");
           break;}
          }  
        
      }//end of run
   }//end of InnerClass     
}//End ChatClient 
