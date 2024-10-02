package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.dto.ClassAttendance;
import com.flyingpig.dataobject.dto.StudentAttendance;
import com.flyingpig.dataobject.entity.CourseDetail;
import com.flyingpig.mapper.CourseAttendanceMapper;
import com.flyingpig.mapper.CourseDetailMapper;
import com.flyingpig.service.ExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
public class ExportServiceImpl implements ExportService {
    @Autowired
    CourseAttendanceMapper courseAttendanceMapper;
    @Autowired
    CourseDetailMapper courseDetailMapper;

    @Override
    public File exportStudentAttendance(Integer teaUserid, String courseName, Integer semester, String studentNo) {
        int batchSize = 500; // 每次查询500条
        int offset = 0;
        File outputFile = null;

        //获取excel模板
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/studentAttendance.xlsx");

        try {
            //基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            //获得Excel文件中的叫做studentAttendance的那一页
            XSSFSheet sheet = excel.getSheet("studentAttendance");
            boolean isFirstBatch = true; // 标记是否是第一次写入，修改标题时使用

            while (true) {
                // 分页查询数据
                List<StudentAttendance> batchList = courseAttendanceMapper.pageStudentAttendanceByCourseIdAndStudentNo(teaUserid, courseName, semester, studentNo, offset, batchSize);
                if (batchList.isEmpty()) {
                    break; // 如果查询结果为空，则退出循环
                }

                // 修改标题为姓名+学生课程考勤情况（+课程名称+）
                if (isFirstBatch && !batchList.isEmpty()) {
                    sheet.getRow(0).getCell(0).setCellValue(batchList.get(0).getStudentName() +
                            "学生" + semester + "学期" + courseName + "课程考勤情况");
                    isFirstBatch = false;
                }

                // 添加内容
                for (StudentAttendance studentAttendance : batchList) {
                    // 每一行每一行的填充数据
                    XSSFRow newRow = sheet.createRow(sheet.getLastRowNum() + 1);
                    newRow.createCell(0).setCellValue(studentNo);
                    newRow.createCell(1).setCellValue(studentAttendance.getStudentName());
                    newRow.createCell(2).setCellValue("第" + studentAttendance.getWeek() + "周");
                    newRow.createCell(3).setCellValue("星期" + studentAttendance.getWeekday());
                    String status = switch (studentAttendance.getStatus()) {
                        case 0 -> "尚未考勤";
                        case 1 -> "已签到";
                        case 2 -> "缺勤";
                        case 3 -> "请假";
                        default -> "异常数据";
                    };
                    newRow.createCell(4).setCellValue(status);
                }

                // 清空当前list，释放内存
                batchList.clear();

                // 更新偏移量
                offset += batchSize;
            }
            // 创建临时文件
            outputFile = File.createTempFile("studentAttendance", ".xlsx");
            FileOutputStream out = new FileOutputStream(outputFile);
            excel.write(out);
            //关闭资源
            out.flush();
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFile;
    }
    @Override
    public File exportCourseAttendance(Integer teaUserid, String courseName, Integer semester, Integer week, Integer weekday, Integer beginSection, Integer endSection) {
        // 查询数据
        QueryWrapper<CourseDetail> courseDetailQueryWrapper = new QueryWrapper<>();
        courseDetailQueryWrapper.eq("course_teacher", teaUserid)
                .eq("course_name", courseName)
                .eq("semester", semester);
        if (week != null) {
            courseDetailQueryWrapper.eq("week", week);
        }
        if (weekday != null) {
            courseDetailQueryWrapper.eq("weekday", weekday);
        }
        if (beginSection != null) {
            courseDetailQueryWrapper.eq("section_start", beginSection);
        }
        if (endSection != null) {
            courseDetailQueryWrapper.eq("section_end", endSection);
        }
        List<CourseDetail> courseDetailList = courseDetailMapper.selectList(courseDetailQueryWrapper);
        List<ClassAttendance> classAttendanceList = courseAttendanceMapper.listStudentAttendanceByCourseIdList(courseDetailList);

        File outputFile = null;

        // 获取excel模板
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/courseAttendance.xlsx");

        // 基于模板制作
        try {
            // 基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            // 获得Excel文件中的叫做courseAttendance的那一页
            XSSFSheet sheet = excel.getSheet("courseAttendance");
            // 添加内容
            for (int i = 0; i < classAttendanceList.size(); i++) {
                // 获取那一行的数据
                ClassAttendance classAttendance = classAttendanceList.get(i);
                // 每一行每一行的填充数据
                XSSFRow newRow = sheet.createRow(sheet.getLastRowNum() + 1);
                newRow.createCell(0).setCellValue(classAttendance.getStudentNo());
                newRow.createCell(1).setCellValue(classAttendance.getStudentName());
                newRow.createCell(2).setCellValue(classAttendance.getSignedCount() + "次");
                newRow.createCell(3).setCellValue(classAttendance.getNocheckCount() + "次");
                newRow.createCell(4).setCellValue(classAttendance.getAbsentCount() + "次");
                newRow.createCell(5).setCellValue(classAttendance.getLeaveCount() + "次");
            }
            // 创建临时文件
            outputFile = File.createTempFile("courseAttendance", ".xlsx");
            FileOutputStream out = new FileOutputStream(outputFile);
            excel.write(out);
            // 关闭资源
            out.flush();
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFile;
    }
}
