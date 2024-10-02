package com.flyingpig.service.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.flyingpig.dataobject.dto.StudentImportExcelModel;
import com.flyingpig.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

// 自定义的ReadListener，用于处理从Excel读取的数据
@Slf4j
public class StudentImportExcelModelListener implements ReadListener<StudentImportExcelModel> {
    // 设置批量处理的数据大小
    private static final int BATCH_SIZE = 1000;
    // 用于暂存读取的数据，直到达到批量大小
    private List<StudentImportExcelModel> batch = new ArrayList<>();

    private StudentService studentService;

    // 构造函数，注入MyBatis的Mapper
    public StudentImportExcelModelListener(StudentService studentService) {
        this.studentService = studentService;
    }

    // 每读取一行数据都会调用此方法
    @Override
    public void invoke(StudentImportExcelModel studentImportExcelModel, AnalysisContext analysisContext) {
        log.info("读取到数据: {}", studentImportExcelModel);

        //检查数据的合法性及有效性
        if (validateData(studentImportExcelModel)) {
            //有效数据添加到list中
            batch.add(studentImportExcelModel);
        } else {
            // 处理无效数据，例如记录日志或跳过
        }
        // 当达到批量大小时，处理这批数据
        if (batch.size() >= BATCH_SIZE) {
            processBatch();
        }
    }


    // 检查数据库中是否已存在该数据
    private boolean validateData(StudentImportExcelModel data) {
        return true;
    }



    // 所有数据读取完成后调用此方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 如果还有未处理的数据，进行处理
        if (!batch.isEmpty()) {
            processBatch();
        }
    }

    // 处理一批数据的方法，重试次数超过 3，进行异常处理
    private  void processBatch() {
        int retryCount = 0;
        // 重试逻辑
        while (retryCount < 3) {
            try {
                // 尝试批量插入
                studentService.batchInsert(batch);
                // 清空批量数据，以便下一次批量处理
                batch.clear();
                break;
            } catch (Exception e) {
                // 重试计数增加
                retryCount++;
                // 如果重试3次都失败，记录错误日志
                if (retryCount >= 3) {
                    // 记录异常信息和导致失败的数据
                    log.error(e.getMessage());
                    log.error(batch.toString());
                }
            }
        }
    }



}


