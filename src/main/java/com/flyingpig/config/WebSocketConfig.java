package com.flyingpig.config;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Configuration
@Slf4j
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

    // 创建ServerEndpointExporter的Bean，用于自动注册WebSocket端点
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 建立握手时，连接前的操作
     * 在这个方法中可以修改WebSocket握手时的配置信息，并将一些额外的属性添加到用户属性中
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 获取用户属性
        final Map<String, Object> userProperties = sec.getUserProperties();

        // 获取HTTP请求头信息
        Map<String, List<String>> headers = request.getHeaders();

        // 通过"Authorization"键从请求头中获取对应的token，并存储在用户属性中
        List<String> header1 = headers.get("Authorization");
        userProperties.put("Authorization", header1.get(0));
    }

    /**
     * 初始化端点对象，也就是被@ServerEndpoint所标注的对象
     * 在这个方法中可以自定义实例化过程，比如通过Spring容器获取实例
     */
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return super.getEndpointInstance(clazz);
    }

    /**
     * 获取指定会话的指定请求头的值
     *
     * @param session     WebSocket会话对象
     * @param headerName  请求头名称
     * @return 请求头的值
     */
    public static String getHeader(Session session, String headerName) {
        // 从会话的用户属性中获取指定请求头的值
        final String header = (String) session.getUserProperties().get(headerName);

        // 如果请求头的值为空或空白，则记录错误日志，并关闭会话
        if (StrUtil.isBlank(header)) {
            log.error("获取header失败，不安全的链接，即将关闭");
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return header;
    }
}


