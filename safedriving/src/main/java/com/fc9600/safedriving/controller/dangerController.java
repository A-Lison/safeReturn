package com.fc9600.safedriving.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fc9600.safedriving.model.Danger;
import com.fc9600.safedriving.model.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/danger")
// 查询危险行为的图片
public class dangerController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sheetNamedf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    // 查询单车次的健康数据
    @GetMapping("list/{id}/{num}")
    public Danger returnDate(
            @PathVariable("id") String id,
            @PathVariable("num") String num) {
        Danger danger = new Danger();
        danger.res_pic = new result();
        String sql = "select * from driver" + id + " where num = '" + num + "';";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql);
        danger.res_pic.code = list1.size();
        danger.res_pic.data = list1;
        danger.res_pic.msg = num + "危险驾驶行为记录";

        danger.res_alco = new result();
        sql = "select * from alcohol" + id + " where num = '" + num + "';";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql);
        danger.res_alco.code = list2.size();
        danger.res_alco.data = list2;
        danger.res_alco.msg = num + "的酒精超标记录";

        return danger;
    }

    // 查询所有车次
    @GetMapping("/searchAll/{id}")
    public Danger searchAll(
            @PathVariable("id") String id) {
        Danger danger = new Danger();
        String formName = "driver" + id;
        String sql = "select num from " + formName + " group by num having num !='new';";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql);
        danger.res_pic = new result();
        danger.res_pic.code = list1.size();
        danger.res_pic.data = list1;
        danger.res_pic.msg = "有危险驾驶行为的车次";
        formName = "alcohol" + id;
        sql = "select num from " + formName + " group by num having num !='new';";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql);
        danger.res_alco = new result();
        danger.res_alco.code = list2.size();
        danger.res_alco.data = list2;
        danger.res_alco.msg = "有疑似酒驾行为的车次";
        return danger;
    }

    // 查询指定时间段的所有车次
    @GetMapping("/search/{id}/{num1}/{num2}")
    public Danger excel(@PathVariable("id") String id,
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws ParseException {
        Danger danger = new Danger();
        danger.res_alco = new result();
        danger.res_pic = new result();
        Date date = df.parse(num1);
        String start = sheetNamedf.format(date);
        date = df.parse(num2);
        String end = sheetNamedf.format(date);
        String formName = "driver" + id;
        String sql = "select num from (SELECT * FROM " + formName + " WHERE num BETWEEN '" + start +
                ".000' AND '" + end + ".999')A group by num;";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql);
        danger.res_pic.data = list1;
        danger.res_pic.code = list1.size();
        danger.res_pic.msg = "该时间段所有危险驾驶行为";
        formName = "driver" + id;
        sql = "select num from (SELECT * FROM " + formName + " WHERE num BETWEEN '" + start +
                ".000' AND '" + end + ".999')A group by num;";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql);
        danger.res_alco.data = list2;
        danger.res_alco.code = list2.size();
        danger.res_alco.msg = "该时间段所有酒精超标记录";
        return danger;
    }
}
