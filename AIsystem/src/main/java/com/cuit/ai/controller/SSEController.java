package com.cuit.ai.controller;

import com.cuit.ai.utils.SSEMsgType;
import com.cuit.ai.utils.SSEServer;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;



@RestController
@RequestMapping("sse")
public class SSEController {
    //连接sse服务的接口
    @GetMapping("/connect")
    public SseEmitter connect(String userId) {
        return SSEServer.connect(userId);
    }

    //发送单条消息
    @GetMapping ("/sendMessage")
    public Object sendMessage(String userId, String message) {
        SSEServer.sendMessage(userId,message, SSEMsgType.MESSAGE);
        return "ok";
    }

    //发送消息给客户端所有用户
    @GetMapping("/sendMessageAll")
    public Object sendMessageAll(String message) {
        SSEServer.sendMessageToAllUsers(message);
        return "ok";
    }

    //add 事件流式输出
    @GetMapping("/sendMessageAdd")
    public Object sendMessageAdd(String userId, String message) throws InterruptedException {
        for (int i=1 ;i<=10;i++) {
            Thread.sleep(200);
            SSEServer.sendMessage(userId, message+"-"+i, SSEMsgType.ADD);
        }
        return "ok";
    }

    //主动切断，停止sse服务器和客户端
   @GetMapping("/stop")
    public Object stop(String userId) {
        SSEServer.stopServer(userId);
        return "ok";
   }

   //统计当前人数
    @GetMapping("/getOnlineCounts")
    public Object getOnlineCounts() {
        return SSEServer.getOnlineCount();
    }
}
