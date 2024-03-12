package com.wdbyte.downbit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.CRC32;

import com.wdbyte.downbit.thread.DownloadThread;
import com.wdbyte.downbit.thread.LogThread;
import com.wdbyte.downbit.util.FileUtils;
import com.wdbyte.downbit.util.HttpUtls;
import com.wdbyte.downbit.util.LogUtils;
import com.wdbyte.downbit.util.ThunderUtils;

/**
 * 多线程下载
 * 断点续传下载 demo
 * @author 牵着猫散步的鼠鼠-LiuShiJie
 */
public class DownloadMain {
    // 下载线程数量
    public static int DOWNLOAD_THREAD_NUM = 5;
    // 下载线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(DOWNLOAD_THREAD_NUM + 1);
    // 临时文件后缀
    public static String FILE_TEMP_SUFFIX = ".temp";

    // 支持的 URL 协议
    private static HashSet<String> PROTOCAL_SET = new HashSet();

    static {
        PROTOCAL_SET.add("thunder://");
        PROTOCAL_SET.add("http://");
        PROTOCAL_SET.add("https://");
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要下载的链接:");
        String url = scanner.nextLine();
        long count = PROTOCAL_SET.stream().filter(prefix -> url.startsWith(prefix)).count();
        if (count == 0) {
            LogUtils.info("不支持的协议类型");
            return;
        }
        LogUtils.info("要下载的链接是:{}", url);
        new DownloadMain().download(ThunderUtils.toHttpUrl(url));
    }

    public void download(String url) throws Exception {
        String fileName = HttpUtls.getHttpFileName(url);
        long localFileSize = FileUtils.getFileContentLength(fileName);
        // 获取网络文件具体大小
        long httpFileContentLength = HttpUtls.getHttpFileContentLength(url);
        if (localFileSize >= httpFileContentLength) {
            LogUtils.info("{}已经下载完毕，无需重新下载", fileName);
            return;
        }
        List<Future<Boolean>> futureList = new ArrayList<>();
        if (localFileSize > 0) {
            LogUtils.info("开始断点续传 {}", fileName);
        } else {
            LogUtils.info("开始下载文件 {}", fileName);
        }
        LogUtils.info("开始下载时间 {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        long startTime = System.currentTimeMillis();
        // 任务切分
        splitDownload(url, futureList);
        LogThread logThread = new LogThread(httpFileContentLength);
        Future<Boolean> future = executor.submit(logThread);
        futureList.add(future);
        // 开始下载
        for (Future<Boolean> booleanFuture : futureList) {
            booleanFuture.get();
        }
        LogUtils.info("文件下载完毕 {}，本次下载耗时：{}", fileName, (System.currentTimeMillis() - startTime) / 1000 + "s");
        LogUtils.info("结束下载时间 {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        // 文件合并
        boolean merge = merge(fileName);
        if (merge) {
            // 清理分段文件
            clearTemp(fileName);
        }
        LogUtils.info("本次文件下载结束，下载位置为" + fileName);
        System.exit(0);
    }

    /**
     * 切分下载任务到多个线程
     *
     * @param url
     * @param futureList
     * @throws IOException
     */
    public void splitDownload(String url, List<Future<Boolean>> futureList) throws IOException {
        long httpFileContentLength = HttpUtls.getHttpFileContentLength(url);
        // 任务切分
        long size = httpFileContentLength / DOWNLOAD_THREAD_NUM;
        long lastSize = httpFileContentLength - (httpFileContentLength / DOWNLOAD_THREAD_NUM * (DOWNLOAD_THREAD_NUM - 1));
        for (int i = 0; i < DOWNLOAD_THREAD_NUM; i++) {
            long start = i * size;
            Long downloadWindow = (i == DOWNLOAD_THREAD_NUM - 1) ? lastSize : size;
            Long end = start + downloadWindow;
            if (start != 0) {
                start++;
            }
            DownloadThread downloadThread = new DownloadThread(url, start, end, i, httpFileContentLength);
            Future<Boolean> future = executor.submit(downloadThread);
            futureList.add(future);
        }
    }

    public boolean merge(String fileName) throws IOException {
        LogUtils.info("开始合并文件 {}", fileName);
        byte[] buffer = new byte[1024 * 10];
        int len = -1;
        try (RandomAccessFile oSavedFile = new RandomAccessFile(fileName, "rw")) {
            for (int i = 0; i < DOWNLOAD_THREAD_NUM; i++) {
                try (BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(fileName + FILE_TEMP_SUFFIX + i))) {
                    while ((len = bis.read(buffer)) != -1) { // 读到文件末尾则返回-1
                        oSavedFile.write(buffer, 0, len);
                    }
                }
            }
            LogUtils.info("文件合并完毕 {}", fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean clearTemp(String fileName) {
        LogUtils.info("开始清理临时文件 {}{}0-{}", fileName, FILE_TEMP_SUFFIX, (DOWNLOAD_THREAD_NUM - 1));
        for (int i = 0; i < DOWNLOAD_THREAD_NUM; i++) {
            File file = new File(fileName + FILE_TEMP_SUFFIX + i);
            file.delete();
        }
        LogUtils.info("临时文件清理完毕 {}{}0-{}", fileName, FILE_TEMP_SUFFIX, (DOWNLOAD_THREAD_NUM - 1));
        return true;
    }

    /**
     * 使用CheckedInputStream计算CRC
     */
    public static Long getCRC32(String filepath) throws IOException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
        CRC32 crc = new CRC32();
        byte[] bytes = new byte[1024];
        int cnt;
        while ((cnt = inputStream.read(bytes)) != -1) {
            crc.update(bytes, 0, cnt);
        }
        inputStream.close();
        return crc.getValue();
    }

}
