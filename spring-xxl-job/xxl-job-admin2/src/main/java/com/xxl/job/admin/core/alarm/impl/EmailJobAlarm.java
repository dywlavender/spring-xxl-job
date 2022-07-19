package com.xxl.job.admin.core.alarm.impl;

import com.xxl.job.admin.core.alarm.JobAlarm;
import com.xxl.job.admin.core.util.I18nUtil;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.sql.Struct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName EmailJobAlarm
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/19 14:21
 * @Version 1.0
 **/
public class EmailJobAlarm implements JobAlarm {
    private static Logger logger = LoggerFactory.getLogger(EmailJobAlarm.class);

    @Override
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog) {
        boolean alarmResult = true;

        if (info !=null&&info.getAlarmEmail!=null && info.getAlarmEmail().trim().length()>0){
            String alarmContent = "Alarm JOb LogId="+jobLog.getId();
            if (jobLog.getTriggerCode() != ReturnT.SUCCESS_CODE){
                alarmContent += "<br>TriggerMsg=<br>" + jobLog.getTriggerMsg();
            }
            if (jobLog.getHandleCode>0 && jobLog.getHandleCode()!=ReturnT.SUCCESS_CODE){
                alarmContent += "<br>HandleCode=" + jobLog.getHandleMsg();
            }

            XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().load(Integer.valueOf(info.getJobGroup()));
            String person = I18nUtil.getString("admin_name_full");
            String title = I18nUtil.getString("jobconf_monitor");
            String content = MessageFormat.format(loadEmainJobAlarmTemplate(),
                    group!=null?group.getTitle():"null",
                    info.getId(),
                    info.getJobDesc(),
                    alarmContent);

            Set<String> emailSet = new HashSet<>(Arrays.asList(info.getAlarmEmail().split(",")));
            for (String email:emailSet){
                try{
                    MimeMessage mineMessage = XxlJobAdminConfig.getAdminCofing().getMailSender().createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mineMessage,true);
                    helper.setFrom(XxlJobAdminConfig.getAdminConfig().getEmailFrorm(),personal);
                    helper.setTo(email);
                    helper.setSubject(title);
                    helper.setText(content, true);
                    XxlJobAdminConfig.getAdminconfig().getMailSender().send(mineMessage);
                } catch (Exception e) {
                    logger.error(">>> xxl_job");
                    alarmResult = false;
                }
            }
        }
        return alarmResult;
    }

    private static final String loadEmailJobAlarmTemplate(){
        String mailBodyTemplate = "<h5>" + I18nUtil.getString("jobconf_monitor_detail") + "：</span>" +
                "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n" +
                "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >" +
                "      <tr>\n" +
                "         <td width=\"20%\" >"+ I18nUtil.getString("jobinfo_field_jobgroup") +"</td>\n" +
                "         <td width=\"10%\" >"+ I18nUtil.getString("jobinfo_field_id") +"</td>\n" +
                "         <td width=\"20%\" >"+ I18nUtil.getString("jobinfo_field_jobdesc") +"</td>\n" +
                "         <td width=\"10%\" >"+ I18nUtil.getString("jobconf_monitor_alarm_title") +"</td>\n" +
                "         <td width=\"40%\" >"+ I18nUtil.getString("jobconf_monitor_alarm_content") +"</td>\n" +
                "      </tr>\n" +
                "   </thead>\n" +
                "   <tbody>\n" +
                "      <tr>\n" +
                "         <td>{0}</td>\n" +
                "         <td>{1}</td>\n" +
                "         <td>{2}</td>\n" +
                "         <td>"+ I18nUtil.getString("jobconf_monitor_alarm_type") +"</td>\n" +
                "         <td>{3}</td>\n" +
                "      </tr>\n" +
                "   </tbody>\n" +
                "</table>";

        return mailBodyTemplate;
    }
}
