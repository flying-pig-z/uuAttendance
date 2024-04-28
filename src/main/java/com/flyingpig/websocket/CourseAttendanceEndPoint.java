package com.flyingpig.websocket;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.config.WebSocketConfig;
import com.flyingpig.dataobject.dto.StudentAttendanceNow;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.mapper.StudentMapper;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ServerEndpoint 注解的作用
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/studentNowAttendances/{userId}", configurator = WebSocketConfig.class)
public class CourseAttendanceEndPoint {
    //技术点一：注入mapper或service
    //这里注入如果按controller的方式直接注入无法注入成功，需要设置成静态变量然后写个方法赋值。
    private static StudentMapper studentMapper;

    @Autowired
    public void setStudentMapper(StudentMapper studentMapper) {
        CourseAttendanceEndPoint.studentMapper = studentMapper;
    }

    private static CourseAttendanceService courseAttendanceService;

    @Autowired
    public void setCourseAttendanceService(CourseAttendanceService courseAttendanceService) {
        CourseAttendanceEndPoint.courseAttendanceService = courseAttendanceService;
    }



    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的用userId
     */
    private String studentId;

    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     * 注意这里的key和value,设计的很巧妙，value刚好是本类对象 (用来存放每个客户端对应的MyWebSocket对象)
     */
    private static ConcurrentHashMap<String, CourseAttendanceEndPoint> webSocketSet = new ConcurrentHashMap<>();
    //线程
    private ScheduledExecutorService scheduler;
    //存储该学生用户获取到的课程信息
    private StudentAttendanceNow lastCourseInfo = new StudentAttendanceNow();


    /**
     * 群发
     *
     * @param message
     */
    public void groupSending(String message) {
        for (String name : webSocketSet.keySet()) {
            try {
                webSocketSet.get(name).session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定发送
     *
     * @param studentId
     * @param message
     */
    public void appointSending(String studentId, String message) {
        System.out.println(webSocketSet.get(studentId).session);
        try {
            webSocketSet.get(studentId).session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接建立成功调用的方法
     * session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void OnOpen(Session session, EndpointConfig config, @PathParam("userId") String userId) {
        log.info("----------------------------------");
        //技术点二：获取请求头中的token,通过配置可以使用session.getUserProperties().get("")获取请求头
        // 获取用户属性
        //获取HttpSession对象
        final String Authorization = (String) session.getUserProperties().get("Authorization");
        System.out.println(Authorization);

        Claims claims = JwtUtil.parseJwt(Authorization);
        userId = claims.getSubject();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<Student>();
        studentQueryWrapper.eq("userid", userId);
        Student student = studentMapper.selectOne(studentQueryWrapper);
        //为这个websocket的studentId和session变量赋值，因为后面还要用到
        studentId = student.getId().toString();// studentId是用来表示唯一客户端，如果需要指定发送，需要指定发送通过studentId来区分
        this.session = session;//这句话一定要写，不然后面就会报错session为空

        //将studentId及其对应的websocket实例保存在属于类的webSocketSet中，便于后续发送信息时候可以知道要发送给哪个实例
        webSocketSet.put(studentId, this);
        //打印websocket信息
        log.info("[WebSocket] 连接成功，当前连接人数为：={}", webSocketSet.size());
        log.info("----------------------------------");
        log.info("");
        //技术点三：建立线程，每隔一段时间检查数据库并更新客户端的数据
        //建立线程，每个一段时间检查客户端对应在websocket中的数据有没有改变，有改变将信息重新发给客户端
        scheduler = Executors.newScheduledThreadPool(1);
        //一分钟推送一次
        scheduler.scheduleAtFixedRate(this::checkDatabaseAndUpdateClients, 0, 10, TimeUnit.SECONDS);
    }

    //每个一段时间检查客户端对应在websocket中的数据有没有改变，有改变将信息重新发给客户端
    private void checkDatabaseAndUpdateClients() {
        // 模拟查询最新的课程信息
        // 假设从数据库或其他数据源中查询得到最新的课程信息
        System.out.println("666");
        try {
            //获取当前的信息
            StudentAttendanceNow currentCourseInfo = courseAttendanceService.getStudentAttendanceNow(studentId);
            //与之前的信息进行比较,如果说当前数据与上次存储的数据相比发生了改变,那就替换掉原来的数据并把新的数据发送给客户端
            if (!lastCourseInfo.equals(currentCourseInfo)) {
                lastCourseInfo = currentCourseInfo;
                System.out.println(studentId);
                appointSending(studentId, JSON.toJSONString(currentCourseInfo));
            }
        } catch (Exception e) {
            //防止没有查询到数据等异常
            System.out.println(e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void OnClose() {
        //退出时将用户从记录中删除，并在log中打印退出信息
        webSocketSet.remove(this.studentId);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", webSocketSet.size());
        // 关闭定时任务
        scheduler.shutdown();
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void OnMessage(Session session, String message) {
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误");
        error.printStackTrace();
    }
}
