package com.thinkdifferent.convertvideo.utils;

import cn.hutool.system.OsInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConvertVideoUtils {

    private static Logger logger = LoggerFactory.getLogger(ConvertVideoUtils.class);

    private String inputPath;

    private String outputPath;

    private String ffmpegPath;

    private String fileName;

    public ConvertVideoUtils(String inputPath, String outputPath, String ffmpegPath, String fileName) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.ffmpegPath = ffmpegPath;
        this.fileName = fileName;
    }

    public Boolean setVoidInfos() {
        if (!checkfile(inputPath)) {
            logger.info(inputPath + " is not file");
            return false;
        }
        if (process(inputPath, ffmpegPath, outputPath, fileName)) {
            logger.info("ok");
            return true;
        }
        return false;
    }


    public static boolean process(String inputPath, String ffmpegPath, String outputPath, String fileName) {
        int type = checkContentType(inputPath);
        boolean status = false;
        logger.info("直接转成mp4格式");
        status = processMp4(inputPath, ffmpegPath, outputPath, fileName);// 直接转成mp4格式
        return status;
    }


    private static int checkContentType(String inputPath) {
        String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length())
                .toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        }
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    private static boolean checkfile(String inputPath) {
        File file = new File(inputPath);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }

    private static boolean processMp4(String oldfilepath, String ffmpegPath, String outputPath, String fileName) {
        if (!checkfile(oldfilepath)) {
            logger.info(oldfilepath + " is not file");
            return false;
        }
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-y");
        command.add("-i");
        command.add(oldfilepath);
        command.add("-c:v");
        command.add("libx264");
        command.add("-mbd");
        command.add("0");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("-2");
        command.add("-pix_fmt");
        command.add("yuv420p");
        command.add("-movflags");
        command.add("faststart");
        command.add(outputPath + fileName + ".mp4");
        try {
            if (new OsInfo().isWindows()  ) {
                Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
                new PrintStream(videoProcess.getErrorStream()).start();
                new PrintStream(videoProcess.getInputStream()).start();
                videoProcess.waitFor();
            } else {
                logger.info("linux开始");
                StringBuilder test = new StringBuilder();
                for (String s : command) test.append(s).append(" ");
                logger.info(test.toString());
                // 执行命令
                Process p = Runtime.getRuntime().exec(test.toString());
                // 取得命令结果的输出流
                InputStream fis = p.getInputStream();
                // 用一个读输出流类去读
                InputStreamReader isr = new InputStreamReader(fis);
                // 用缓冲器读行
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                // 直到读完为止
                while ((line = br.readLine()) != null) {
                    logger.info("视频转换:{}", line);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        String inputPath = "D:/cvtest/001.MOV";
        String outputPath = "D:/cvtest/";
        String ffmpegPath = "C:/Program Files (x86)/FormatFactory/ffmpeg.exe";
        String fileName = "001-online";
        ConvertVideoUtils convertVideoUtils = new ConvertVideoUtils(inputPath, outputPath, ffmpegPath, fileName);
        boolean blnSuccess = convertVideoUtils.setVoidInfos();

        if(blnSuccess){
            logger.info("视频转换成功");
        }else{
            logger.info("视频转换失败");
        }
    }


}
