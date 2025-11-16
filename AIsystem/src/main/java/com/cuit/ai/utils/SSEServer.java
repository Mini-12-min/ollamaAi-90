package com.cuit.ai.utils;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SSEServer {
    //使用map对象，关联用户id和sse服务连接
    private static Map<String, SseEmitter> sseClients = new ConcurrentHashMap<>();

    //统计当前在线总人数
    private  static AtomicInteger onlineCounts = new AtomicInteger(0);

    //连接sse服务器
    public static SseEmitter connect(String userId){
        //设置超时时间，0代表永不过期，默认三十秒，超时未完成就报错
        SseEmitter sseEmitter = new SseEmitter(0L);

        sseEmitter.onCompletion(completionCallback(userId));
        sseEmitter.onError(errorCallback(userId));
        sseEmitter.onTimeout(timeoutCallback(userId));

        sseClients.put(userId, sseEmitter);
        System.out.println("打印的当前创建新的sse连接，用户id为："+userId);

        onlineCounts.getAndIncrement();
        return sseEmitter;
    }

    //sse连接完成后的回调方法（关闭连接的时候调用）
    private static Runnable completionCallback(String userId) {
        return () -> {
            System.out.println("sse连接完成并结束，打印用户id："+userId);
            sseClients.remove(userId);
            System.out.println("sse连接被移除，移除用户id为："+userId);
            onlineCounts.getAndDecrement();
        };
    }
    //sse连接发生错误时
    private static Consumer<Throwable> errorCallback(String userId) {
        return Throwable ->{
            System.out.println("sse连接发生错误，用户id为："+userId);
            sseClients.remove(userId);
            System.out.println("sse连接被移除，移除用户id为："+userId);
            onlineCounts.getAndDecrement();
        };
    }
    //sse连接超时
    private static Runnable timeoutCallback(String userId) {
        return () -> {
            System.out.println("sse连接超时，打印用户id："+userId);
            sseClients.remove(userId);
            System.out.println("sse连接被移除，移除用户id为："+userId);
            onlineCounts.getAndDecrement();
        };
    }

    //发送单条消息
    public static void sendMessage(String userId,String message,SSEMsgType msgType){
        if (CollectionUtils.isEmpty(sseClients)){
            return;
        }
        if (sseClients.containsKey(userId)){
            SseEmitter sseEmitter = sseClients.get(userId);
            //发送单条消息
            sendEmitterMessage(sseEmitter,userId,message,msgType);

        }
    }

    //发送消息给所有人
    public static void sendMessageToAllUsers(String message){
        if (CollectionUtils.isEmpty(sseClients)){
            return;
        }
        sseClients.forEach((userId,sseEmitter)->{
            sendEmitterMessage(sseEmitter,userId,message,SSEMsgType.MESSAGE);
        });
    }

//使用sendEmitter推送消息
    private static void sendEmitterMessage(SseEmitter sseEmitter, String userId, String message, SSEMsgType msgType) {
        try {
            SseEmitter.SseEventBuilder msg=SseEmitter.event()
                    .id(userId)
                    .name(msgType.type)
                    .data(message);
            sseEmitter.send(msg);
        }catch (Exception e){
            System.out.println("用户"+userId+"的消息发生异常");
            sseClients.remove(userId);
            System.out.println("sse连接被移除，移除用户id为："+userId);
            onlineCounts.getAndDecrement();
        }
    }

    //主动切断，停止sse服务器和客户端
    public static void stopServer(String userId)  {
            if (CollectionUtils.isEmpty(sseClients)){
                return;
            }
            SseEmitter SseEmitter = sseClients.get(userId);
            if (SseEmitter != null){
                SseEmitter.complete();
                sseClients.remove(userId);
                System.out.println("sse 连接被移除"+userId);
                onlineCounts.getAndDecrement();
            }
    }

    //获得当前会话连接总数
    public static int getOnlineCount(){
        return onlineCounts.intValue();
    }

}
