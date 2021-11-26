package com.thinkdifferent.convertvideo.task;

import com.thinkdifferent.convertvideo.service.ConvertVideoService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Task implements RabbitTemplate.ConfirmCallback {
    // 日志对象，用于输出执行过程中的日志信息
    private static final Logger log = LoggerFactory.getLogger(Task.class);


    /**
     * 处理接收列表中的数据，异步多线程任务
     *
     * @param convertVideoService 创建PDF文件的Service对象
     * @param jsonInput 队列中待处理的JSON数据
     * @throws Exception
     */
    @Async("taskExecutor")
    public void doTask(ConvertVideoService convertVideoService, JSONObject jsonInput) {

        log.info("开始处理-转换MP4文件");
        long longStart = System.currentTimeMillis();

        log.info("MQ中存储的数据:" + jsonInput.toString());

        Map<String, String> mapReturn = new HashMap<>();


        try{
            JSONObject jsonSuccess = convertVideoService.ConvertVideo(jsonInput);
            if("success".equalsIgnoreCase(jsonSuccess.getString("flag"))){
                mapReturn.put("flag", "success");
                mapReturn.put("message", "Video Convert to Mp4 Success.");
            }else{
                mapReturn.put("flag", "error");
                mapReturn.put("message", "Video Convert to Mp4 Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("转换MP4异常");
            log.error(e.getMessage());

            mapReturn.put("flag", "error" );
            mapReturn.put("message", "Video Convert to Mp4 Error.");

        }
        long longEnd = System.currentTimeMillis();
        log.info("完成-转换MP4文件，耗时：" + (longEnd - longStart) + "毫秒");
    }


    /**
     * 回调反馈消费者消费信息
     * @param correlationData
     * @param b
     * @param msg
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String msg)
    {
        log.info(" 回调id:" + correlationData);
        if (b) {
            log.info("消息成功消费");
        } else {
            log.info("消息消费失败:" + msg);
        }
    }


}
