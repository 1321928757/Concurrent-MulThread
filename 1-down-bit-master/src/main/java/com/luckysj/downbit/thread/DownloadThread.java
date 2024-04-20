package com.luckysj.downbit.thread;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

import com.luckysj.downbit.DownloadMain;
import com.luckysj.downbit.util.FileUtils;
import com.luckysj.downbit.util.HttpUtls;
import com.luckysj.downbit.util.LogUtils;

/**
 * 多线程下载工具类
 * @author 牵着猫散步的鼠鼠-LiuShiJie
 */
public class DownloadThread implements Callable<Boolean> {

    /**
     * 每次读取的数据块大小
     */
    private static int BYTE_SIZE = 1024 * 100;
    /**
     * 下载链接
     */
    private String url;
    /**
     * 下载开始位置
     */
    private long startPos;
    /**
     * 要下载的文件区块大小
     */
    private Long endPos;
    /**
     * 标识多线程下载切分的第几部分
     */
    private Integer part;
    /**
     * 文件总大小
     */
    private Long contentLenth;

    public DownloadThread(String url, long startPos, Long endPos, Integer part, Long contentLenth) {
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.part = part;
        this.contentLenth = contentLenth;
    }

    @Override
    public Boolean call() throws Exception {
        // 1.路径格式校验
        if (url == null || url.trim() == "") {
            throw new RuntimeException("下载路径不正确");
        }

        // 2.定义分块文件名，格式为原文件名+temp+序号，如qq.temp1,qq.temp2,qq.temp3
        String httpFileName = HttpUtls.getHttpFileName(url);
        if (part != null) {
            httpFileName = httpFileName + DownloadMain.FILE_TEMP_SUFFIX + part;
        }

        // 3.判断以前是否下载过
        Long localFileContentLength = FileUtils.getFileContentLength(httpFileName);
        LogThread.LOCAL_FINISH_SIZE.addAndGet(localFileContentLength);
        if (localFileContentLength >= endPos - startPos) {
            LogUtils.info("{} 已经下载完毕，无需重复下载", httpFileName);
            LogThread.DOWNLOAD_FINISH_THREAD.addAndGet(1);
            return true;
        }
        // 如果刚好是最后一个任务，不需要指明文件结尾位置
        if (endPos.equals(contentLenth)) {
            endPos = null;
        }

        // 4 获取连接
        HttpURLConnection httpUrlConnection = HttpUtls.getHttpUrlConnection(url, startPos + localFileContentLength, endPos);
        // 4.1获得输入流
        try (InputStream input = httpUrlConnection.getInputStream(); BufferedInputStream bis = new BufferedInputStream(input);
             RandomAccessFile oSavedFile = new RandomAccessFile(httpFileName, "rw")) {
            oSavedFile.seek(localFileContentLength);
            // 4.2 缓冲区
            byte[] buffer = new byte[BYTE_SIZE];
            int len = -1;
            // 4.3 读取文件到本地文件末尾
            while ((len = bis.read(buffer)) != -1) {
                oSavedFile.write(buffer, 0, len);
                LogThread.DOWNLOAD_SIZE.addAndGet(len);
            }
        } catch (FileNotFoundException e) {
            LogUtils.error("ERROR! 要下载的文件路径不存在 {} ", url);
            return false;
        } catch (Exception e) {
            LogUtils.error("下载出现异常");
            e.printStackTrace();
            return false;
        } finally {
            httpUrlConnection.disconnect();
            LogThread.DOWNLOAD_FINISH_THREAD.addAndGet(1);
        }
        return true;
    }

}
