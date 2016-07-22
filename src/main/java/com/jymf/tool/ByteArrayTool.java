package com.jymf.tool;

/**
 * 字节数组和基本类型互转工具类
 * @author Zhang
 * @version 0.1
 */
public class ByteArrayTool {

    /**
     * @param num
     * @return 两位的字节数组
     */
    public static byte[] shortToBytes(short num) {
        int temp = num;
        byte[] bytes = new byte[2];
        for (int i = 1; i > -1; i--) {
            bytes[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最高位
            temp = temp >> 8; // 向右移8位
        }
        return bytes;
    }

    /**
     * @param bytes
     * @return 短整型
     */
    public static short bytesToShort(byte[] bytes) {
        short num = 0;
        for (int i = 1; i > -1; i--) {
            num |= (bytes[i] & 0xff) << (8 * (1 - i));
        }
        return num;
    }

    public static short bytesToShort(byte[] bytes, int offset) {
        short num = 0;
        for (int i = 1 + offset; i > offset - 1; i--) {
            num |= (bytes[i] & 0xff) << (8 * (1 + offset - i));
        }
        return num;
    }

    /**
     * @param num
     * @return 四位的字节数组
     */
    public static byte[] intToBytes(int num) {
        int temp = num;
        byte[] bytes = new byte[4];
        for (int i = 3; i > -1; i--) {
            bytes[i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8;
        }
        return bytes;
    }

    /**
     * @param bytes
     * @return 整型
     */
    public static int bytesToInt(byte[] bytes) {
        return bytesToInt(bytes, 0);
    }

    public static int bytesToInt(byte[] bytes, int offset) {
        int num = 0;
        for (int i = offset + 3; i > (offset - 1); i--) {
            num |= (bytes[i] & 0xff) << (8 * (3 + offset - i));
        }
        return num;
    }


    /**
     * @param num
     * @return 长整型
     */
    public static byte[] longToBytes(long num) {
        long temp = num;
        byte[] bytes = new byte[8];
        for (int i = 7; i > -1; i--) {
            bytes[i] = new Long(temp & 0xff).byteValue();
            temp = temp >> 8;
        }
        return bytes;
    }

    /**
     * @param bytes
     * @return 长整型
     */
    public static long bytesToLong(byte[] bytes) {
        long num = 0;
        for (int i = 7; i > -1; i--) {
            num |= (bytes[i] & 0xff) << (8 * (7 - i));
        }
        return num;
    }

    public static long bytesToLong(byte[] bytes,int offset) {
        long num = 0;
        for (int i = offset+7; i > offset-1; i--) {
            num |= (bytes[i] & 0xff) << (8 * (offset + 7 - i));
        }
        return num;
    }
}


