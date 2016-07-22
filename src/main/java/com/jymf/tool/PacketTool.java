package com.jymf.tool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 打包工具类
 * @author Zhang
 * @version 0.1
 */
public class PacketTool {

    public static byte[] pack(int copeCode,String jsonString,byte[] content){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream ops = new DataOutputStream(baos);
            ops.write(new String("JY").getBytes("utf-8"));    //插入JY
            ops.writeShort(copeCode);                                //插入操作码2
            byte[] jsonBytes = jsonString.getBytes("utf-8");
            ops.writeShort((short)jsonBytes.length);
            ops.write(jsonBytes);
            if (content != null) {
                ops.write(content);
            }
            ops.writeInt(CrcTool.crc32(baos.toByteArray()));  //插入CRC32校验码
            return baos.toByteArray();
        } catch (IOException e) {
            // TODO: 15/12/19 log 内部组包错误,基本不会遇到
            e.printStackTrace();
            return null;
        }
    }
}
