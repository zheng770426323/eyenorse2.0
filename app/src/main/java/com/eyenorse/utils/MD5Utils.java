package com.eyenorse.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liujun on 2016/5/26.
 */
public class MD5Utils {
//    public static String getFileMD5Checksum(String filename) throws Exception {
//        byte[] b = createChecksum(filename);
//        String result = "";
//
//        for (int i = 0; i < b.length; i++) {
//            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
//        }
//        return result;
//    }

//    public static byte[] createChecksum(String filename) throws Exception {
//        InputStream fis = new FileInputStream(filename);
//
//        byte[] buffer = new byte[1024];
//        MessageDigest complete = MessageDigest.getInstance("MD5");
//        int numRead;
//
//        do {
//            numRead = fis.read(buffer);
//            if (numRead > 0) {
//                complete.update(buffer, 0, numRead);
//            }
//        } while (numRead != -1);
//
//        fis.close();
//        return complete.digest();
//    }

    public static String getFileMD5(String inputFile) throws IOException {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用DigestInputStream
            fileInputStream = new FileInputStream(new File(inputFile));
            digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0) {
                ;
            }
            // 获取最终的MessageDigest
            messageDigest = digestInputStream.getMessageDigest();
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } finally {
            try {
                digestInputStream.close();
            } catch (Exception e) {
            }
            try {
                fileInputStream.close();
            } catch (Exception e) {
            }
        }
    }

    //下面这个函数用于将字节数组换成成16进制的字符串

    public static String byteArrayToHex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs + "";
            }
        }
        // return hs.toUpperCase();
        return hs;

        // 首先初始化一个字符数组，用来存放每个16进制字符

      /*char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };



      // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

      char[] resultCharArray =new char[byteArray.length * 2];

      // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

      int index = 0;

      for (byte b : byteArray) {

         resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];

         resultCharArray[index++] = hexDigits[b& 0xf];

      }

      // 字符数组组合成字符串返回

      return new String(resultCharArray);*/

    }

    public static boolean checkFileMd5Validation(String filePath, String md5) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                String fileMD5 = getFileMD5(filePath);
                return fileMD5.equals(md5);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
