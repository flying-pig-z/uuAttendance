package com.flyingpig.service.serviceImpl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.flyingpig.dataobject.constant.RoleConstants;
import com.flyingpig.dataobject.dto.StudentImportExcelModel;
import com.flyingpig.dataobject.entity.UserRoleRelation;
import com.flyingpig.framework.cache.core.CacheUtil;
import com.flyingpig.mapper.StudentMapper;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.mapper.UserMapper;
import com.flyingpig.mapper.UserRoleRelationMapper;
import com.flyingpig.service.StudentService;
import com.flyingpig.service.excel.StudentImportExcelModelListener;
import com.flyingpig.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flyingpig.dataobject.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.flyingpig.dataobject.constant.RedisConstants.USER_INFO_KEY;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;
    @Autowired
    CacheUtil cacheUtil;

    @Override
    public void addStudent(User user, Student student) {
        userMapper.insert(user);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("no", student.getNo());
        Integer userId = userMapper.selectOne(userQueryWrapper).getId();
        student.setUserid(userId);
        studentMapper.insert(student);
        //插入关系表中，赋予学生身份和权限
        userRoleRelationMapper.insert(new UserRoleRelation(userId, RoleConstants.STUDENT));
    }

    @Override
    public Map<String, Object> getStudentInfoByUserId(Long userid) {
        Map<String, Object> target = new HashMap<>();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("userid", userid);
        Student student = studentMapper.selectOne(studentQueryWrapper);
        User user = cacheUtil.get(USER_INFO_KEY + userid, User.class);
        target.put("id", student.getId());
        target.put("no", user.getNo());
        target.put("name", user.getName());
        target.put("gender", user.getGender());
        target.put("grade", student.getGrade());
        target.put("class", student.getClasS());
        target.put("major", student.getMajor());
        target.put("college", user.getCollege());
        return target;
    }

    @Override
    public void addStudentByExcel(MultipartFile studentInfoExcel) {

        // 需要读取的sheet数量
        int numberOfSheets = ExcelUtil.getExcelSheetNumber(studentInfoExcel);

        System.out.println(numberOfSheets);
        // 创建一个固定大小的线程池，大小与sheet数量相同
        ExecutorService executor = Executors.newFixedThreadPool(numberOfSheets);

// 遍历所有sheets
        for (int sheetNo = 0; sheetNo < numberOfSheets; sheetNo++) {
            // 在Java lambda表达式中使用的变量需要是final
            int finalSheetNo = sheetNo;
            // 向线程池提交一个任务
            executor.submit(() -> {
                // 每个线程需要独立的InputStream
                try (InputStream inputStream = studentInfoExcel.getInputStream()) {
                    // 使用EasyExcel读取指定的sheet
                    EasyExcel.read(inputStream, StudentImportExcelModel.class, new StudentImportExcelModelListener(this))
                            .sheet(finalSheetNo)
                            .doRead();
                } catch (IOException e) {
                    log.error("读取excel文件出错: ", e);
                }
            });
        }

        // 启动线程池的关闭序列
        executor.shutdown();

        // 等待所有任务完成，或者在等待超时前被中断
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // 如果等待过程中线程被中断，打印异常信息
            e.printStackTrace();
        }

    }
    private String generateRandom32WithTimestamp() {
        long timestamp = System.currentTimeMillis();
        String timestampStr = String.valueOf(timestamp);

        // 计算需要生成的随机数字长度
        int randomLength = 32 - timestampStr.length();

        // 一次性生成对应长度的随机数字
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < randomLength; i++) {
            randomNumbers.append(ThreadLocalRandom.current().nextInt(0, 10));
        }

        return timestampStr + randomNumbers.toString();
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<StudentImportExcelModel> batch) {
        List<User> users = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<UserRoleRelation> userRoleRelations = new ArrayList<>();
        for (StudentImportExcelModel studentImportExcelModel : batch) {
            Integer userId = Integer.parseInt(generateRandom32WithTimestamp());
            User user = new User(userId, studentImportExcelModel.getNo(),
                    new BCryptPasswordEncoder().encode(studentImportExcelModel.getNo()), studentImportExcelModel.getName(),
                    studentImportExcelModel.getGender(), studentImportExcelModel.getCollege(), 1);
            Student student = new Student(userId, studentImportExcelModel.getNo(), studentImportExcelModel.getName(),
                    studentImportExcelModel.getGrade(), studentImportExcelModel.getClasS(),
                    "计算机与大数据学院/软件学院", userId);
            UserRoleRelation userRoleRelation = new UserRoleRelation(userId, RoleConstants.STUDENT);
            users.add(user);
            students.add(student);
            userRoleRelations.add(userRoleRelation);
        }
        userMapper.saveBatch(users);
        userRoleRelationMapper.saveBatch(userRoleRelations);
        studentMapper.saveBatch(students);
    }


}
