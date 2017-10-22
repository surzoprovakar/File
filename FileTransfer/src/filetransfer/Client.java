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
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Raju
 */
public class Client implements Runnable{

    private static Socket socket=null;
    Client(Socket client) {
        socket=client;
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        while (true) {            
            
        
        // TODO code application logic here
        //Socket socket = null;
        
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String clientId=reader.readLine();
        
        
        
            
            InetAddress ipAddress = InetAddress.getLocalHost();
            System.out.println(ipAddress);
            
            socket = new Socket(ipAddress,5678);
            
            byte[] bytes;
            
            DataInputStream in = null;
            DataOutputStream out = null;
            
            in = new DataInputStream(socket.getInputStream());
            out=new DataOutputStream(socket.getOutputStream());
            out.writeUTF(clientId);
            out.flush();
            int conncheck=in.read();
            //System.out.println(conncheck);
            if (conncheck==2) {
                System.out.println("Client already connected");
            }
            else {
                System.out.println("1.Send File");
                System.out.println("2.Receive File");
                String cho=reader.readLine();
                int choice=Integer.parseInt(cho);
                if(choice==1) {
                    //System.out.println(choice);
                    String receiverId=reader.readLine();
                    out.writeUTF(receiverId);
                    out.flush();
                    int dec=in.read();
                    System.out.println(dec);
                    if (dec==50) {
                        String fadd=reader.readLine();
                        File file = new File(fadd);
                        
                        long length = file.length();
                        System.out.println(length);
                                
                        out.writeUTF(file.getName());
                        out.flush();
                        out.write((int) length);
                        out.flush();
                        int getres=in.read();
                        if(getres==100) {
                            int chunk=in.read();
                            bytes=new  byte[chunk];
                            try (InputStream fin = new FileInputStream(file); OutputStream fout = socket.getOutputStream()) {

                                int count;
                                while ((count = fin.read(bytes)) > 0) {
                                    fout.write(bytes, 0, count);
                                }

                            } 
                        }

                        else {
                            System.out.println("Too large file");
                        }
                    }
                    else {
                        System.out.println("Receiver is not present");
                    }
                }
                        
                else if(choice==2) {
                    //System.out.println(choice);  
                     try {
                            
                            String fdes=reader.readLine();
                            FileOutputStream fout=new FileOutputStream(fdes);
                            int count=0;
                            byte[] bytesrec = new byte[150];
                            while ((count = in.read(bytesrec)) > 0) {
                                fout.write(bytesrec, 0, count);

                            }
                            out.close();
                            in.close();
                            fout.close();


                        } catch (FileNotFoundException ex) {
                            System.out.println("File not found. ");
                        }   
                }
                else {
                    System.out.println("Invalid Selection");
                }
            }
        }
    }
    

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
