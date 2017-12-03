/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransfer;

import static filetransfer.Binaryconvert.Binaryconvert;

/**
 *
 * @author Raju
 */
public class ExtraDel {

//    public static String ExtraDel(String s) {
//        String sub = "01111110";
//        String tmp="";
//        int count = 0;
//        int i=0;
//        int index=0;
//        for (i = 0; i < s.length(); i++) {
//            if (s.charAt(i) == sub.charAt(count)) {
//                count++;
//            } else {
//                count = 0;
//            }
//            if (count == sub.length()) {
//                //System.out.println("Sub String");
//                index=i+1;
//                count=0;
//            }
//
//        }
//        //if (count != sub.length()) {
//            //System.out.println("Not");
//        //}
//        
//        System.out.println(index);
//        System.out.println(i);
//        for(i=0;i<index;i++) {
//            tmp=tmp+s.charAt(i);
//        }
//        return tmp.toString();
//    }
    public static String ExtraDel(byte[] b) {
        int count = 0;
        int index=0;
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xFF) == 126) {
                count++;
            }
            if (count == 2) {
                index = i + 1;
                break;
            }
        }

        String tmp = "";
        for (int i = 0; i < index; i++) {
            tmp = tmp + Binaryconvert(b[i]);
        }
        
        return tmp.toString();
    }
}
