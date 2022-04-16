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
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sheetNamedf = new SimpleDateFormat("yyyy-MM-dd");

    // 查询单车次的健康数据
    @GetMapping("list/{id}/{num}")
    public result returnDate(
            @PathVariable("id") String id,
            @PathVariable("num") String num) {
        result res = new result();
        res.data = new Object();
        String sql = "select * from driver" + id + " where num = '" + num + "';";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql);
        res.code = list1.size();
        res.data = list1;
        res.msg = num + "危险驾驶行为记录";
        return res;
    }

    // 查询所有车次
    @GetMapping("/searchAll/{id}")
    public result searchAll(
            @PathVariable("id") String id) {
        result res = new result();
        String formName = "driver" + id;
        String sql = "select num from " + formName + " group by num having num != 'new';";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql);
        res.data = new result();
        res.code = list1.size();
        res.data = list1;
        res.msg = "有危险驾驶行为的车次";
        return res;
    }

    // 查询指定时间段的所有车次
    @GetMapping("/search/{id}/{num1}/{num2}")
    public result excel(@PathVariable("id") String id,
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws ParseException {
        result res = new result();
        res.data = new Object();
        Date date = df.parse(num1);
        String start = sheetNamedf.format(date);
        date = df.parse(num2);
        String end = sheetNamedf.format(date);
        String formName = "driver" + id;
        String sql = "select num from (SELECT * FROM " + formName + " WHERE num BETWEEN '" + start +
                " 00:00:00.000' AND '" + end + " 23:59:59.999')A group by num;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println(sql);
        System.out.println(list);
        res.data = list;
        res.code = list.size();
        res.msg = "该时间段所有危险行为";
        return res;
    }
}
