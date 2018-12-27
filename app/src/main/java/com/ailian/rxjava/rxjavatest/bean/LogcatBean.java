package com.ailian.rxjava.rxjavatest.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wumh on 2018/12/27.
 */
public class LogcatBean {
    private String content;
    private String tag;
    private String logTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");

    public LogcatBean(String tag,String content) {
        this.content = content;
        this.tag = tag;
        this.logTime = sdf.format(new Date());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
