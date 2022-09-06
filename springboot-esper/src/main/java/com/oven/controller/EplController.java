package com.oven.controller;

import com.espertech.esper.client.*;
import com.oven.config.EsperConfig;
import com.oven.listener.LocationListener1;
import com.oven.listener.LocationListener2;
import com.oven.util.EsperUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EplController {

    @Resource(name = "epServiceProvider")
    private EPServiceProvider provider;
    @Resource(name = "epAdministrator")
    private EPAdministrator epAdministrator;

    @RequestMapping("/start1")
    public String start1() {
        if (null != EsperConfig.STATEMENT_WRAP.get("1")) {
            return "esper1已经启动！";
        }

        String epl = "select location, phone from mobileLocation where location in ('2', '1', '3')";
        EPStatement statement = epAdministrator.createEPL(epl);
        statement.addListener(new LocationListener1());
        statement.start();
        EsperConfig.STATEMENT_WRAP.put("1", statement);
        System.out.println(" 111 启动成功！");
        return " 111 启动成功！";
    }

    @RequestMapping("/start2")
    public String start2() {
        if (null != EsperConfig.STATEMENT_WRAP.get("2")) {
            return "esper2已经启动！";
        }

        String epl = "select location, phone from mobileLocation where location in ('4', '3', '5')";
        EPStatement statement = epAdministrator.createEPL(epl);
        statement.addListener(new LocationListener2());
        statement.start();
        EsperConfig.STATEMENT_WRAP.put("2", statement);
        System.out.println(" 222 启动成功！");
        return " 222 启动成功！";
    }

    @RequestMapping("/stop1")
    public String stop1() {
        EPStatement statement = EsperConfig.STATEMENT_WRAP.get("1");
        if (null == statement) {
            return "esper1 not exist!";
        }

        statement.stop();
        EsperConfig.STATEMENT_WRAP.remove("1");
        return " 111 停止成功！";
    }

    @RequestMapping("/stop2")
    public String stop2() {
        EPStatement statement = EsperConfig.STATEMENT_WRAP.get("2");
        if (null == statement) {
            return "esper2 not exist!";
        }

        statement.stop();
        EsperConfig.STATEMENT_WRAP.remove("2");
        return " 222 停止成功！";
    }

    @RequestMapping("/start3")
    public String start3() {
        if (null != EsperConfig.STATEMENT_WRAP.get("3")) {
            return "esper3已经启动！";
        }

        String epl = "select ip,url,count(*) as num from accesslog.win:time_batch(5 sec) group by ip,url order by num desc";
        EPStatement statements = EsperUtil.getAdministrator().createEPL(epl);
        statements.addListener((newData, oldData, statement, runtime) -> {
            for (EventBean item : newData) {
                String ip = (String) item.get("ip");
                String url = (String) item.get("url");
                long num = (long) item.get("num");
                System.out.printf("5秒钟内用户%s访问接口%s累计%d次\n", ip, url, num);
            }
        });
        statements.start();
        EsperConfig.STATEMENT_WRAP.put("3", statements);
        System.out.println(" 333 启动成功！");
        return " 333 启动成功！";
    }

    @RequestMapping("/send")
    public String send(String location, String phone, String ip, String url) {
        EPRuntime runtime = provider.getEPRuntime();
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(url)) {
            map.put("ip", ip);
            map.put("url", url);
            EsperUtil.getProvider().getEPRuntime().sendEvent(map, "accesslog");
            System.out.println("发送 333 事件成功！");
            return "发送成功！";
        }
        map.put("location", location);
        map.put("phone", phone);

        runtime.sendEvent(map, "mobileLocation");
        return "发送成功！";
    }

}