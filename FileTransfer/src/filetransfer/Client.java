/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransfer;

import static filetransfer.Binaryconvert.Binaryconvert;
import static filetransfer.Bitstuffing.Bitstuffing;
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
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Raju
 */
public class Client implements Runnable {

    private static Socket socket = null;

    Client(Socket client) {
        socket = client;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        while (true) {

            // TODO code application logic here
            //Socket socket = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String clientId = reader.readLine();

            InetAddress ipAddress = InetAddress.getLocalHost();
            System.out.println(ipAddress);

            socket = new Socket(ipAddress, 5678);

            byte[] bytes;

            DataInputStream in = null;
            DataOutputStream out = null;

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(clientId);
            out.flush();
            int conncheck = in.read();
            //System.out.println(conncheck);
            if (conncheck == 2) {
                System.out.println("Client already connected");
            } else {
                System.out.println("1.Send File");
                System.out.println("2.Receive File");
                String cho = reader.readLine();
                int choice = Integer.parseInt(cho);
                if (choice == 1) {
                    //System.out.println(choice);
                    String receiverId = reader.readLine();
                    out.writeUTF(receiverId);
                    out.flush();
                    int dec = in.read();
                    //System.out.println(dec);
                    if (dec == 50) {
                        String fadd = reader.readLine();
                        File file = new File(fadd);

                        long length = file.length();
                        System.out.println(length);

                        out.writeUTF(file.getName());
                        out.flush();
                        out.write((int) length);
                        out.flush();
                        int getres = in.read();
                        if (getres == 100) {
                            int chunk = in.read();
                            bytes = new byte[chunk];
                            try (InputStream fin = new FileInputStream(file); OutputStream fout = socket.getOutputStream()) {
                                int count;
                                int chunkCount = 1;
                                String flag = "01111110";
                                while ((count = fin.read(bytes)) > 0) {
                                    //fout.write(bytes, 0, count);
//                                    System.out.println("bytes");
//                                    for (int i = 0; i < bytes.length; i++) {
//                                        System.out.println(Integer.toBinaryString(bytes[i] & 255 | 256).substring(1));
//                                    }

                                    String serial = "";
                                    serial = IntBinaryString(chunkCount);
//                                    System.out.println(chunkCount);
//                                    System.out.println(serial);
                                    String z = "";
                                    //System.out.println(z.length());
                                    for (int i = 0; i < bytes.length; i++) {
                                        z = z + Binaryconvert(bytes[i]);
                                    }

                                    int checkOne = 0;
                                    for (int i = 0; i < z.length(); i++) {
                                        if (z.charAt(i) == '1') {
                                            checkOne++;
                                        }
                                    }
                                    //System.out.println(checkOne);
                                    String checksum = "";
                                    checksum = IntBinaryString(checkOne);
                                    //System.out.println(checksum);

                                    //out.writeUTF(checksum);
                                    //out.flush();
                                    String y = "";
                                    y = serial + checksum + z;
                                    //String payload=Bitstuffing(y);
                                    //String data=flag+payload+flag;

                                    String data = Bitstuffing(y);
                                    int n = data.length();

                                    byte[] b;
                                    if (n % 8 == 0) {
                                        b = new byte[n / 8];
                                    } else {
                                        b = new byte[(n / 8) + 1];
                                    }

                                    int byteCount = 0;
                                    int localCount = 0;
                                    for (int i = 0; i < data.length(); i++) {
                                        if (i % 8 == 0 && i != 0) {
                                            byteCount++;
                                            localCount = 0;
                                        }

                                        if (data.charAt(i) == '1') {
                                            b[byteCount] = (byte) (b[byteCount] | (1 << (7 - localCount)));
                                        }
                                        localCount++;

                                    }
//                                    System.out.println("b");
//                                    for (int i = 0; i < b.length; i++) {
//                                        System.out.println(Integer.toBinaryString(b[i] & 255 | 256).substring(1));
//                                    }
                                    byte[] arr = new byte[b.length + 2];
                                    int p = 126;
                                    byte q;
                                    q = (byte) p;
                                    arr[0] = q;
                                    arr[b.length + 1] = q;

                                    for (int i = 0; i < b.length; i++) {
                                        arr[i + 1] = b[i];
                                    }
                                    fout.write(arr, 0, arr.length);
                                    //for(int i=0;i<b.length;i++) System.out.print(b[i]);

//                                    System.out.println("Original data");
//                                    System.out.println(z);
//                                    System.out.println(z.length());
                                    //String y="";
                                    //y=Bitstuffing(z);
                                    //System.out.println("Stuffed data");
                                    //System.out.println(payload);
                                    //System.out.println(payload.length());
                                    //System.out.println("data");
                                    //System.out.println(data);
                                    String ack = in.readUTF();
                                    String ackfinal = "";
                                    for(int i=8;i<16;i++) ackfinal=ackfinal+ack.charAt(i);

                                    //System.out.println(ack);
                                    //String checkAck=flag+ack+flag;
                                    if (!ackfinal.equals(serial)) {
                                        System.out.println("Wrong chunk,Serial mismatched");
                                        break;
                                    }
                                    //}
                                    chunkCount++;
                                }

                            }
                        } else {
                            System.out.println("Too large file");
                        }
                    } else {
                        System.out.println("Receiver is not present");
                    }
                } else if (choice == 2) {
                    //System.out.println(choice);  
                    try {

                        String fdes = reader.readLine();
                        FileOutputStream fout = new FileOutputStream(fdes);
                        int count = 0;
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
                } else {
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
