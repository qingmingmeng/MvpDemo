package com.example.zj.mvpdemo.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 功能：md5加密
 * 创建：sunqiujing
 * 日期: on 16/4/8 19:32
 */
public class MD5Utile
{

    public static String string2MD5(String inStr)
    {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    public static String getMD5(String info)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++)
            {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1)
                {
                    strBuf.append("0").append(
                        Integer.toHexString(0xff & encryption[i]));
                } else
                {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e)
        {
            return "";
        } catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }

    public static String md5(String string)
    {
        byte[] hash;
        try
        {
            hash = MessageDigest.getInstance("MD5").digest(
                string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash)
        {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * @param str
     * @return
     */
    public static String encrypt(String str)
    {
        MessageDigest messageDigest = null;

        try
        {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++)
        {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                    Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    /**
     * 给字符串加密
     */
    public static String convertMD5(String inStr)
    {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++)
        {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);

    }


    public static void main(String args[])
    {
        String s = "tangfuqiang";
        System.out.println("" + "123");
        System.out.println("MD5" + string2MD5("123"));
        // System.out.println("onvertMD5( + convertMD5(s));
        // System.out.println("onvertMD5( + convertMD5(convertMD5(s)));

    }

    public final static String MD5(byte[] md)
    {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};
        try
        {
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e)
        {
            return null;
        }
    }

    public static String md5sum(String filename)
    {
        InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try
        {
            fis = new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0)
            {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
//            return toHexString(md5.digest());
            return MD5(md5.digest());
        } catch (Exception e)
        {
            System.out.println("error");
            return null;
        }
    }

    /**
     * @param fileInputStream
     * @return
     */
    public static String md5sum(FileInputStream fileInputStream)
    {
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try
        {

            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fileInputStream.read(buffer)) > 0)
            {
                md5.update(buffer, 0, numRead);
            }
            fileInputStream.close();
//            return toHexString(md5.digest());
            return MD5(md5.digest());
        } catch (Exception e)
        {
            System.out.println("error");
            return null;
        }
    }
}
