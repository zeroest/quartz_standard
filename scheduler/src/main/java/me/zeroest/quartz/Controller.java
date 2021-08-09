package me.zeroest.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {
    private final Scheduler scheduler;
    private final RemoteJobClassLoader remoteJobClassLoader;

    @GetMapping("/")
    public String add() throws ClassNotFoundException, SchedulerException {
        log.info("Init Runner executed.");
        JobKey jobKey = JobKey.jobKey("jobkey1", "jobgroup1");
        scheduler.deleteJob(jobKey);

        JobDetail jobDetail = buildJobDetail(jobKey);
        Trigger trigger = buildJobTrigger(jobKey);
        scheduler.scheduleJob(jobDetail, trigger);  // (1)

        return "OK";
    }

    private JobDetail buildJobDetail(JobKey jobKey) throws ClassNotFoundException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("key1", "value1");
        jobDataMap.put("key2", 2);

        return JobBuilder.newJob(remoteJobClassLoader.loadClass("me.zeroest.quartz.RemoteSimpleJob", Job.class))
                //return JobBuilder.newJob(SimpleJob.class)  // (2)
                .withIdentity(jobKey)
                .withDescription("Simple Quartz Job Detail")
                .usingJobData(jobDataMap)
                .build();
    }

    private Trigger buildJobTrigger(JobKey jobKey) {
        return TriggerBuilder.newTrigger()
                .forJob(jobKey)
                .withDescription("Simple Quartz Job Trigger")
//                .startNow()  // 스케줄링 되면 바로 실행되는 방식
                .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?"))
                .build();
    }
}
