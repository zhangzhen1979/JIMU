package com.thinkdifferent.convertvideo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertVideoConfig {

    public static String outPutPath;
    @Value("${convert.video.outPutPath}")
    public void setOutPutPath(String strOutPutPath) {
        ConvertVideoConfig.outPutPath = strOutPutPath;
    }

    public static String ffmpegPath;
    @Value("${convert.video.ffmpegPath}")
    public void setFfmpegPath(String strFfmpegPath) {
        ConvertVideoConfig.ffmpegPath = strFfmpegPath;
    }

}
