/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransfer;

import static filetransfer.Binaryconvert.Binaryconvert;
import static filetransfer.Bitdestuffing.Bitdestuffing;
import static filetransfer.ExtraDel.ExtraDel;
import static filetransfer.IntBinaryString.IntBinaryString;
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
    public static String list[] = new String[100];
    public static int llen = 0;
    public static Socket Socllist[] = new Socket[100];
    public static int socklen = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Server() throws Exception {
        new TCPServer();
        //WorkerThread w=new WorkerThread();
    }

    public class TCPServer {

        class TCPthread implements Runnable {

            Thread t;

            TCPthread() {
                //txt.appendText("enter");
                t = new Thread(this);
                t.start();
            }

            public void run() {
                try {
                    ServerSocket welcomeSocket = new ServerSocket(5678);
                    System.out.println("Server started successfully");
                    while (true) {
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

        public WorkerThread(Socket connectionSocket) {
            //this.id=id;
            this.connectionSocket = connectionSocket;

        }

        @Override
        public void run() {

            try {

                DataInputStream in = null;
                DataOutputStream out = null;

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                {
                    in = new DataInputStream(connectionSocket.getInputStream());
                    out = new DataOutputStream(connectionSocket.getOutputStream());
                    String clientId = in.readUTF();
                    System.out.println(clientId);
                    int flag = 0;
                    for (int i = 0; i < llen; i++) {
                        //System.out.println("hill");
                        if (list[i].equals(clientId)) {
                            System.out.println("Client already connected");
                            out.write(2);
                            out.flush();
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        out.write(4);
                        out.flush();
                        list[llen++] = clientId;
                        Socllist[socklen++] = connectionSocket;
                        //System.out.println("hi");
                        System.out.println(list.toString());

                        String receiverId = in.readUTF();
                        Socket receiverSocket = null;
                        int dec = 0;
                        for (int i = 0; i < llen; i++) {
                            //System.out.println("hill");
                            if (list[i].equals(receiverId)) {
                                dec = 1;
                                receiverSocket = Socllist[i];
                                out.write(50);
                                out.flush();
                                break;
                            }
                        }

                        if (dec == 1) {
                            String s = in.readUTF();
                            int flen = in.read();
                            if (flen <= 100 * 1024 * 100) {
                                out.write(100);
                                out.flush();
                                String fdes = "E:\\CLI" + s;
                                try {
                                    Random chunksize = new Random();
                                    int chunk = (chunksize.nextInt(flen) / 10) + 1;
                                    //int chunk=80;
                                    System.out.println("chunk" + chunk);
                                    out.write(chunk);
                                    out.flush();
                                    FileOutputStream fout = new FileOutputStream(fdes);
                                    int count = 0;
                                    String flagvar = "01111110";
                                    int chunkcount = 1;
                                    byte[] bytes = new byte[(chunk * 2) + 5];
                                    while ((count = in.read(bytes)) > 0) {
                                        //fout.write(bytes, 0, count);
                                        //int orglen=in.read();
                                        //System.out.println(orglen);
                                        //String checksum = in.readUTF();
//                                        System.out.println("bytes");
//                                        for (int i = 0; i < bytes.length; i++) {
//                                            System.out.println(Integer.toBinaryString(bytes[i] & 255 | 256).substring(1));
//                                        }
                                        String z = "";
                                        for (int i = 0; i < bytes.length; i++) {
                                            z = z + Binaryconvert(bytes[i]);
                                        }
                                        //System.out.println("got");
                                        //System.out.println(z);

//                                        String x = "";
//                                        for (int i = 8; i < z.length(); i++) {
//                                            x = x + z.charAt(i);
//                                        }
                                        String org = "";
                                        org = ExtraDel(bytes);
                                        //System.out.println("mod");
                                        //System.out.println(org);

                                        String pay = org.replace("01111110", "");
                                        //System.out.println("payload");
                                        //System.out.println(pay);
                                        byte[] b;
                                        b = Bitdestuffing(pay);
                                        //System.out.println("b");
//                                        for (int i = 0; i < b.length; i++) {
//                                            System.out.println(Integer.toBinaryString(b[i] & 255 | 256).substring(1));
//                                        }

                                        String data = "";
                                        for (int i = 0; i < b.length; i++) {
                                            data = data + Binaryconvert(b[i]);
                                        }

                                        String serial = "";
                                        for (int i = 0; i < 8; i++) {
                                            serial = serial + data.charAt(i);
                                        }

                                        String checksum = "";
                                        for (int i = 8; i < 16; i++) {
                                            checksum = checksum + data.charAt(i);
                                        }

                                        //if (checksum == checksum2) {
                                        System.out.println(chunkcount + " has reached");
                                        String main = "";
                                        for (int i = 16; i < data.length(); i++) {
                                            main = main + data.charAt(i);
                                        }
                                        //System.out.println("main");
                                        //System.out.println(main);
                                        int countOne = 0;
                                        for (int i = 0; i < main.length(); i++) {
                                            if (main.charAt(i) == '1') {
                                                countOne++;
                                            }
                                        }

                                        String chk = "";
                                        chk = IntBinaryString(countOne);

                                        if (chk.equals(checksum)) {

                                            byte[] last;
                                            last = new byte[main.length() / 8];

                                            int byteCount = 0;
                                            int localCount = 0;
                                            for (int i = 0; i < main.length(); i++) {
                                                if (i % 8 == 0 && i != 0) {
                                                    byteCount++;
                                                    localCount = 0;
                                                }

                                                if (main.charAt(i) == '1') {
                                                    last[byteCount] = (byte) (last[byteCount] | (1 << (7 - localCount)));
                                                }
                                                localCount++;

                                            }

//                                        System.out.println("last");
//                                        for (int i = 0; i < last.length; i++) {
//                                            System.out.println(Integer.toBinaryString(last[i] & 255 | 256).substring(1));
//                                        }

                                            String ack = flagvar + serial + flagvar;
//                                        System.out.println("acknowledgement");
//                                        System.out.println(ack);
                                            out.writeUTF(ack);
                                            out.flush();
                                            chunkcount++;
                                            
                                            fout.write(last, 0, last.length);
                                        }
                                        
                                        
                                        //}

                                        else {
                                        System.out.println("Wrong Frame");
                                        }
                                        //System.out.println(bytes);
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
                                    byte[] bytesrec = new byte[150];
                                    while ((c = fin.read(bytesrec)) > 0) {
                                        fout.write(bytesrec, 0, c);
                                    }
                                    out.close();
                                    in.close();
                                    fout.close();
                                } catch (Exception e) {
                                    e.printStackTrace(System.out);
                                }

                            } else {
                                out.write(-1);
                                out.flush();
                            }

                        } else {
                            out.write(5);
                            out.flush();
                            System.out.println("Receiver not present");
                        }

                        connectionSocket.close();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }

        }

    }

}
