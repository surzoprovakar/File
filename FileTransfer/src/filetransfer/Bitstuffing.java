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
public class Bitstuffing {
    
    public static String Bitstuffing(String s) {
        String res = new String();
        int counter = 0;
        for(int i=0;i<s.length();i++) {                     
            if(s.charAt(i) == '1') {
                counter++;
                res = res + s.charAt(i);
            }
            else {
                res = res + s.charAt(i);
                counter = 0;
            }
            
            if(counter == 5) {
                res = res + '0';
                counter = 0;
            }
        }
        int len=res.length();
        if(len%8==0) {
            return res.toString();
        }
        else {
            int l=8-len%8;
            for(int i=0;i<l;i++) {
                res=res+'0';
            }
            return res.toString();
        }
        
        
    }
    
}
