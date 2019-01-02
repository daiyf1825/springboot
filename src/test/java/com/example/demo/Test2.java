package com.example.demo;

import org.joda.time.DateTime;
import org.junit.Test;

public class Test2 {

    @org.junit.Test
    public void test(){
        System.out.println("---->>>"+DateTime.now().minusDays(6).withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss"));

    }

    @Test
    public void test1() {
        String url = "http//static.kukr.com/images/admin/1001812786/20181108102306267.png";
        String src = url.substring(url.indexOf("m")+1);
        String srcUrl = "/data/kukr" + src;
        System.out.println("------->>>srcUrl   " + srcUrl);
    }
}
