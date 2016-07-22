package com.jymf.service.fdfs;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 由于DFS服务器的storage_server只能在内网访问,这个Mock类模拟Fdfs的输入输出,便于在本地测试
 */
public class FdfsServiceMock {

    public String upLoadByteArray(byte[] data, int offset, int length, String ext, HashMap<String,String> map) throws IOException,MyException {
        return "/M00/01/SKFDJSHKDK02931";

    }
}
