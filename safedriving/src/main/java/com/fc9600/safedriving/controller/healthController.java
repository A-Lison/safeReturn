package com.fc9600.safedriving.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fc9600.safedriving.model.HealthInfoPost;
import com.fc9600.safedriving.model.healthSearch;
import com.fc9600.safedriving.model.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class healthController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sheetNamedf = new SimpleDateFormat("yyyy-MM-dd");

    // 传输数据
    // insert into formName( ) values('2017-03-02 15:22:22');
    @PostMapping(path = "/healthAdd")
    public result addhealthinfo(@RequestBody HealthInfoPost healthinfo) {
        result res = new result();
        for (int i = 0; i < healthinfo.data.size(); i++) {
            String formName = "health" + healthinfo.id;
            System.out.println(formName);
            String sql = "insert into " + formName +
                    "(num,time,heart,press,heat,longitude,latitude)values ('new','"
                    + healthinfo.data.get(i).time + "',"
                    + healthinfo.data.get(i).heart + ","
                    + healthinfo.data.get(i).press + ","
                    + healthinfo.data.get(i).heat + ","
                    + healthinfo.data.get(i).longitude + ","
                    + healthinfo.data.get(i).latitude
                    + ");";
            jdbcTemplate.update(sql);
        }
        if (healthinfo.sign == 1) {
            res.msg = "运行中";
            res.code = 1;
        } else {
            // 用于传输最后一段数据
            String formName = "health" + healthinfo.id;
            String en = healthinfo.data.get(healthinfo.data.size() - 1).time;
            // String end = en.substring(0, en.length() - 9);
            String sql = "update " + formName + " set num='" + en + "' where num='new';";
            jdbcTemplate.update(sql);
            formName = "driver" + healthinfo.id;
            sql = "update " + formName + " set num='" + en + "' where num='new';";
            jdbcTemplate.update(sql);
            formName = "alcohol" + healthinfo.id;
            sql = "update " + formName + " set num='" + en + "' where num='new';";
            jdbcTemplate.update(sql);
            res.msg = "数据传输结束";
            res.code = 0;
        }
        return res;
    }

    // 检查是否传输完成
    @GetMapping("/check/{id}")
    public result check(@PathVariable("id") String id) {
        String fName = "health" + id;
        String sql = "select * from " + fName + " where num='new';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        result res = new result();
        if (list.size() != 0) {
            res.msg = "上次传输未正常结束";
            res.code = -1;
        } else {
            res.msg = "数据传输成功";
            res.code = 0;
        }
        return res;
    }

    // 查询单车次的健康数据
    @GetMapping("list/{id}/{num}")
    public result returnDate(
            @PathVariable("id") String id,
            @PathVariable("num") String num) {
        result res = new result();
        String sql = "select * from health" + id + " where num = '" + num + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        res.code = list.size();
        res.data = list;
        res.msg = num + "的健康数据";
        return res;
    }

    // 查询所有车次
    @GetMapping("/searchAll/{id}")
    public result searchAll(
            @PathVariable("id") String id) {
        String formName = "health" + id;
        String sql = "select num from " + formName + " group by num having num != 'new';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        result healthinfo = new result();
        healthinfo.code = list.size();
        healthinfo.data = list;
        healthinfo.msg = "所有的健康记录";
        return healthinfo;
    }

    // 查询最近n天的车次
    @GetMapping("/searchN/{id}/{n}")
    public result searchMonth(
            @PathVariable("id") String id,
            @PathVariable("n") int n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String formName = "health" + id;
        Date en = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, n * (-1));
        String end = df.format(en);
        Date st = c.getTime();
        String start = df.format(st);
        String sql = "select num from (SELECT * FROM " + formName + " WHERE num BETWEEN '" + start +
                ".000' AND '" + end + ".999')A group by num;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        result res = new result();
        res.code = list.size();
        res.msg = "近" + n + "天的健康记录";
        res.data = list;
        return res;
    }

    // 按照日期查询搜索 //年-月-日
    @PostMapping(path = "/search")
    public result searchFromAll(
            @RequestBody healthSearch healthinfo) {
        String formName = "health" + healthinfo.id;
        String s[] = { "", "", "" };
        if (healthinfo.year != 0) {
            s[0] = "" + healthinfo.year;
            if (healthinfo.year < 10)
                s[0] = "0" + s[0];
        } else {
            s[0] = "%";
        }
        if (healthinfo.month != 0) {
            s[1] = "" + healthinfo.month;
            if (healthinfo.month < 10)
                s[1] = "0" + s[1];
        } else {
            s[1] = "%";
        }
        if (healthinfo.day != 0) {
            s[2] = "" + healthinfo.day;
            if (healthinfo.day < 10)
                s[2] = "0" + s[2];
        } else {
            s[2] = "%";
        }
        String t = " num like '%";
        for (int i = 0; i < 3; i++) {
            t += s[i];
            if (i != 2) {
                t += "-";
            }
        }
        String sql = "select num from " + formName + " group by num having" + t + "%';";
        System.out.println(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        result res = new result();
        res.code = list.size();
        res.data = list;
        res.msg = "搜索到的健康记录";
        return res;
    }

    // 查询指定时间段的所有车次
    @GetMapping("/searchFT/{id}/{num1}/{num2}")
    public result excel(@PathVariable("id") String id,
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws java.text.ParseException {
        result res = new result();
        Date date = df.parse(num1);
        String start = sheetNamedf.format(date);
        date = df.parse(num2);
        String end = sheetNamedf.format(date);
        String formName = "health" + id;
        String sql = "select num from (SELECT * FROM " + formName + " WHERE num BETWEEN '" + start +
                " 00:00:00.000' AND '" + end + " 23:59:59.999')A group by num;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        res.data = list;
        res.code = list.size();
        res.msg = "该时间段所有车次的健康记录";
        return res;
    }
}
