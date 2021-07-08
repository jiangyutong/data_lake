package com.boyoi.base.tools.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class SaticScheduleTask {

//    @Resource
//    private DeptService deptService;

    @Value("${timerSwitch}")
    private String timerSwitch;
//    @Scheduled(cron = "0/10 * * * * ?") //每10s执行一次任务
//    @Scheduled(cron = "0 */1 * * * ?") //每1分钟执行一次
//    @Scheduled(cron = "0 0 */1 * * ?") //每1小时执行一次任务
//    @Scheduled(cron = "0 */5 * * * ?")  //每5分钟执行一次

//    @Scheduled(cron = "0 0 0 * * ?") // 每天0点执行一次
//    @Scheduled(cron = "0 */1 * * * ?")  //每5分钟执行一次


//    @Scheduled(cron = "0 */1 * * * ?")  //每5分钟执行一次

}
