package com.sky.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("trip_job_lock")
public class TripJobLock {

  private String jobName;
  private String isLock;
  private String jobCron;
  private String jobDesc;


  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }


  public String getIsLock() {
    return isLock;
  }

  public void setIsLock(String isLock) {
    this.isLock = isLock;
  }


  public String getJobCron() {
    return jobCron;
  }

  public void setJobCron(String jobCron) {
    this.jobCron = jobCron;
  }


  public String getJobDesc() {
    return jobDesc;
  }

  public void setJobDesc(String jobDesc) {
    this.jobDesc = jobDesc;
  }

}
