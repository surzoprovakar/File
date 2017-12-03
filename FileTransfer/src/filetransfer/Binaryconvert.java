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
public class Binaryconvert {

    public static String Binaryconvert(byte n) {
    
            StringBuilder sb = new StringBuilder("00000000");

            for (int bit = 0; bit < 8; bit++) {

            if (((n >> bit) & 1) > 0) {

            sb.setCharAt(7 - bit, '1');

            }

            }

            //System.out.print(sb.toString());
            return sb.toString();
    }

    
}
