package com.jymf.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Comparator;

/**
 * 文件下载工具类,用于从一个URL下载相应内容
 * @author Zhang
 * @version 0.1
 */
public class DownloadTool {
    public static byte[] download(String urlString) throws IOException{
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        byte[] bs = new byte[1024];
        int len;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }

        os.close();
        is.close();

        return os.toByteArray();
    }
}
