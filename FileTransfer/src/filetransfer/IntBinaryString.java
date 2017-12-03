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
public class IntBinaryString {
    
    public static String IntBinaryString(int n) {
        String s = "";
        int i=0;
        while (n > 0) {
            s = ((n % 2) == 0 ? "0" : "1") + s;
            n = n / 2;
            i++;
        }
        //System.out.println(i);
        for(int j=0;j<8-i;j++) {
            s='0'+s;
        }
        //System.out.println(s);
        return s.toString();
    } 
    
}
