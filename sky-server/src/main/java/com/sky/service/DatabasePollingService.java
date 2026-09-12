//package com.sky.service;
//
//import com.sky.entity.Employee;
//import com.sky.mapper.EmployeeMapper;
//import com.sky.mapper.TripJobLockMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.sql.SQLException;
//
///**
// * @author: Joey
// * @Description:
// * @date:2024/8/16 1:30
// */
//@Service
//public class DatabasePollingService {
////    @Autowired
////    private final JdbcTemplate jdbcTemplate;
//    @Autowired
//    private EmployeeMapper employeeMapper;
//
//    @Autowired
//    private TripJobLockMapper tripJobLockMapper;
//
//    @Autowired
//    private TestSchedulerManager testSchedulerManager;
//
//    private String lastValue;
//    private String lastCronValue;
//    private String lastCronValue2;
//
////    @Scheduled(fixedRate = 5000) // 每 5 秒执行一次
//    public void pollNotificationTable() throws SQLException {
//        Employee employee = employeeMapper.selectById(1);
//        handleFieldChange(employee.getSex());
////    }
//
//    private void handleFieldChange(String sex) {
//        if(lastValue == null || !lastValue.equals(sex)){
//            System.out.println("sex字段更新 old:"+lastValue+" , new:"+sex);
//            lastValue = sex;
//        }
//        // 这里编写处理字段变化的具体逻辑
//    }
//
////    @Scheduled(fixedRate = 10000) // 每 5 秒执行一次
////    public void pollNotificationTable2(){
////        String cron = tripJobLockMapper.getCron("updateEmployeeSexScheduler");
////        if(lastCronValue == null || !lastCronValue.equals(cron)){
////            System.out.println("cron字段更新 old:"+lastCronValue+" , new:"+cron);
////            lastCronValue = cron;
////            //修改定时任务
////            testSchedulerManager.configureAndScheduleTask("updateEmployeeSexScheduler");
////        }
////    }
//}
