package com.example.demo.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketClient {

    public static WebSocketClient client;
    private static Logger logger = LoggerFactory.getLogger(WebsocketClient.class);

    public static void main(String[] args) throws InterruptedException {
        try {
            client = new WebSocketClient(new URI(
                "wss://www.ccyunchina.com/socket.io/?EIO=3&transport=websocket&sid=1b233701-0c02-4b4b-af7d-4923ea17c296"),
                new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    logger.info("握手成功");
                }

                @Override
                public void onMessage(String msg) {
                    logger.info("收到消息==========" + msg);
                    if (msg.equals("over")) {
                        client.close();
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    logger.info("链接已关闭");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    logger.info("发生错误已关闭");
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        client.connect();
        // logger.info(client.getDraft());
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            if (client.getReadyState() == WebSocket.READYSTATE.CLOSED) {
                break;
            }
            logger.info("正在连接...");
            Thread.sleep(1000);
        }
        // 连接成功,发送信息
        client.send("哈喽,连接一下啊");
    }

}