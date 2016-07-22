package com.jymf.tool;

import java.util.zip.CRC32;

/**
 * Crc校验工具类
 * @author Zhang
 * @version 0.1
 */
public class CrcTool {

    public static boolean isCorrectCrc32(byte[] data){
        CRC32 crc32 = new CRC32();
        crc32.update(data,0,data.length-4);
        return (int)crc32.getValue()==ByteArrayTool.bytesToInt(data,data.length-4);
    }

    public static int crc32(byte[] data){
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return (int)crc32.getValue();
    }
}

