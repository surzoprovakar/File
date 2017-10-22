/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransfer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;

/**
 *
 * @author Raju
 */
public class Server implements Initializable {

    //private static ServerSocket welcomeSocket=null;
    public static String list[]=new String[100];
    public static int llen=0;
    public static Socket Socllist[]=new Socket[100];
    public static int socklen=0;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public Server() throws Exception {
        new TCPServer();
        //WorkerThread w=new WorkerThread();
    }
    
    public class TCPServer  {
        
        
        class TCPthread implements  Runnable{ 
            Thread t;
            
            TCPthread(){
                //txt.appendText("enter");
                t=new Thread(this);
                t.start();
            }
            
            public void run() {
                try {
                    ServerSocket welcomeSocket = new ServerSocket(5678);
                    System.out.println("Server started successfully");
                    while(true)
                    {
                        try {
                            
                            Socket connectionSocket = welcomeSocket.accept();
                            
                            //System.out.println("Client "+"["+id+"]"+" joined with port "+connectionSocket);
                            WorkerThread wt = new WorkerThread(connectionSocket);
                            Thread t = new Thread(wt);
                            t.start();
                            //c++;
                            //System.out.println("Client : "+id+" got connected. Total threads : "+c);
                            //id++;
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        TCPServer() throws Exception {

            new TCPthread();
            //txt.appendText("Hello");
            // TODO code application logic here
        }
    }
    
     class WorkerThread implements Runnable {
        //private int id;
        private Socket connectionSocket;
        
        public WorkerThread(Socket connectionSocket) 
        {
            //this.id=id;
            this.connectionSocket=connectionSocket;

        }

        @Override
        public void run() {
                           
                
            
            try {
                
                DataInputStream in = null;
                DataOutputStream out = null;
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                 { 
                    in = new DataInputStream(connectionSocket.getInputStream());
                    out=new DataOutputStream(connectionSocket.getOutputStream());
                    String clientId=in.readUTF();
                    System.out.println(clientId);
                    int flag=0;
                    for (int i=0;i<llen;i++){
                        //System.out.println("hill");
                        if(list[i].equals(clientId)) {
                            System.out.println("Client already connected");
                            out.write(2);
                            out.flush();
                            flag=1;
                            break;
                        }
                    }
                    if (flag==0) {
                        out.write(4);
                        out.flush();
                        list[llen++]=clientId;
                        Socllist[socklen++]=connectionSocket;
                        //System.out.println("hi");
                        System.out.println(list.toString());
                        
                        String receiverId=in.readUTF();
                        Socket receiverSocket=null;
                        int dec=0;
                        for (int i=0;i<llen;i++){
                            //System.out.println("hill");
                            if(list[i].equals(receiverId)) {
                                dec=1;
                                receiverSocket=Socllist[i];
                                out.write(50);
                                out.flush();
                                break;
                            }
                        }
                        
                        if(dec==1) {
                            String s=in.readUTF();
                            int flen=in.read();
                            if (flen<=100*1024) {
                                out.write(100);
                                out.flush();
                                String fdes="D:\\CLI"+s;
                             try {
                                Random chunksize = new Random();
                                int chunk=chunksize.nextInt(flen);
                                out.write(chunk);
                                out.flush();
                                FileOutputStream fout=new FileOutputStream(fdes);
                                int count=0;
                                byte[] bytes = new byte[chunk];
                                while ((count = in.read(bytes)) > 0) {
                                    fout.write(bytes, 0, count);

                                }
                                
                                
                                   
                                 out.close();
                                 in.close();
                                 fout.close();



                            } catch (FileNotFoundException ex) {
                                System.out.println("File not found. ");
                            }
                             
                            File file = new File(fdes);
                            try (InputStream fin = new FileInputStream(fdes); OutputStream fout = receiverSocket.getOutputStream()) {

                                int c;
                                byte[] bytesrec=new byte[150];
                                while ((c = fin.read(bytesrec)) > 0) {
                                    fout.write(bytesrec, 0, c);
                                }
                                out.close();
                                in.close();
                                fout.close();
                            }
                            catch (Exception e) {
                                e.printStackTrace(System.out);
                            }
                             
                             
                            }
                            else{
                                out.write(-1);
                                out.flush();
                            }

                        }
                        else {
                            out.write(5);
                            out.flush();
                            System.out.println("Receiver not present");
                        }
                         
                        connectionSocket.close();
                        
                        }
                    }
            }
            catch (Exception e) {
              e.printStackTrace(System.out);
            }
          
        }
     
    }
    

   
}
