package com.jymf.bean.packet;

import com.jymf.exception.CrcCheckErrorException;
import com.jymf.exception.DataFormatException;
import com.jymf.exception.IllegalPackageException;
import com.jymf.service.*;
import com.jymf.tool.ByteArrayTool;
import com.jymf.tool.CrcTool;

import java.io.UnsupportedEncodingException;

/**
 * 通信数据包抽象类,主要完成了包得合法性和CRC校验,和一些通用信息的解析,以及根据操作码完成数据包处理器的选择
 * @author Zhang
 * @version 0.1
 */
public class BasePackage {

    protected byte[] originalData = null;            //通过socket取得的原始通信数据
    protected short copeCode = 0;                    //操作码
    protected short jsonSize = 0;                    //包头json串的字节长度
    protected String jsonString = null;              //从字节流中utf-8解码的到的json字符串
    protected int fileDataOffset = 0;                //文件数据字节流在全部数据里的偏移量

    /**
     * 构造函数
     * @param originalData byte[] 从socket中字节获得的原始数据字节数组
     */
    public BasePackage(byte[] originalData) {
        this.originalData = originalData;
    }

    /**
     * 完成原始数据字节流的数据合法性验证(JY)/CRC校验/操作码校验
     * @throws IllegalPackageException 抛出数据不合法异常
     * @throws CrcCheckErrorException  抛出CRC校验失败异常
     */
    public void check() throws IllegalPackageException,CrcCheckErrorException,DataFormatException {
        if (!(originalData[0]=='J' && originalData[1]=='Y')){
            throw new IllegalPackageException("数据包不合法");
        }
        if (!CrcTool.isCorrectCrc32(originalData)) {
            throw new CrcCheckErrorException("数据包Crc校验失败");
        }
        copeCode = ByteArrayTool.bytesToShort(originalData,2);
        if (copeCode<1||copeCode>4){
            throw new DataFormatException("数据格式错误:copCode不在处理范围内");
        }
    }

    /**
     * 数据准备,解析字节流中操作码和json传长度.并通过utf-8完成json字符串的解码,的到json串.并计算文件数据流的偏移量
     * @throws UnsupportedEncodingException
     */
    public void prepare () throws DataFormatException,UnsupportedEncodingException {
        jsonSize = ByteArrayTool.bytesToShort(originalData,4);
        if (jsonSize<=0) {
            throw new DataFormatException("数据格式错误:jsonsize小于0");
        }
        fileDataOffset = jsonSize + 6;
        try {
            jsonString = new String(originalData,6,jsonSize,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new UnsupportedEncodingException("数据格式错误:json数据编码错误");
        }
    }


    /**
     * 根据数据中的操作码选择相应的处理器
     * @return Handler 数据处理器,是上传处理和下载处理类的基类
     */
    public Handler selectHandler(){
        switch (copeCode){
            case 1:
                return new UploadHandler(this);
            case 2:
                return new DownloadHandler(this);
            case 3:
                return new DeleteHandler(this);
            case 4:
                return new FileaccessHandler(this);
            default:
                return null;
        }
    }

    public byte[] getOriginalData() {
        return originalData;
    }

    public void setOriginalData(byte[] originalData) {
        this.originalData = originalData;
    }

    public short getCopeCode() {
        return copeCode;
    }

    public void setCopeCode(short copeCode) {
        this.copeCode = copeCode;
    }

    public short getJsonSize() {
        return jsonSize;
    }

    public void setJsonSize(short jsonSize) {
        this.jsonSize = jsonSize;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public int getFileDataOffset() {
        return fileDataOffset;
    }

    public void setFileDataOffset(int fileDataOffset) {
        this.fileDataOffset = fileDataOffset;
    }
}