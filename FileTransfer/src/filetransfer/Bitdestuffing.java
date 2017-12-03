/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransfer;

/**
 *
 * @author Raju
 */
public class Bitdestuffing {

    public static byte[] Bitdestuffing(String s) {

        String res = new String();
        int counter = 0;
        int destuff = 0;
        int del = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                counter++;
                res = res + s.charAt(i);

            } else {
                res = res + s.charAt(i);
                counter = 0;
            }
            if (counter == 5) {
                if ((i + 2) < s.length()) {
                    res = res + s.charAt(i + 2);
                    destuff++;
                }
                i = i + 2;
                counter = 1;
            }
        }
        //System.out.println("destuff");
        //System.out.println(res.length());
        //System.out.println(res);

        if (destuff % 8 == 0) {
            del = destuff / 8;
        } else {
            del = destuff / 8 + 1;
        }
        int len = res.length();
        byte[] b;

        b = new byte[(s.length() / 8) - del];
        
        
       
        int byteCount = 0;
        int localCount = 0;
        for (int i = 0; i < ((s.length() / 8) - del)*8; i++) {
            if (i % 8 == 0 && i != 0) {
                byteCount++;
                localCount = 0;
            }

            if (res.charAt(i) == '1') {
                b[byteCount] = (byte) (b[byteCount] | (1 << (7 - localCount)));
            }
            localCount++;

        }
        return b;

    }

}
