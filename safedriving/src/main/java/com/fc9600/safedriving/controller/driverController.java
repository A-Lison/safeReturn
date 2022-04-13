package com.fc9600.safedriving.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fc9600.safedriving.model.healthSearch;
import com.fc9600.safedriving.model.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pic")
// 查询危险行为的图片
public class driverController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // 小程序地理坐标mysql:decimal数据类型

    // 服务器路径
    public static String path = "/www/wwwroot/safedriving/files/";
    public static String api = "http://119.91.89.21:8081/";

    // 本地测试路径
    // public static String path = "F:/picetest/";
    // public static String api = "localhost:8081/";

    // 图片的名字为时间
    @PostMapping(value = "/upload")
    public boolean fileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") int type,
            @RequestParam("id") String id,
            @RequestParam("time") String time) throws IllegalStateException, IOException {
        String fileName = file.getOriginalFilename();
        System.out.println(time);
        System.out.println(path);
        String dataDir = path + id + "img";
        System.out.println(dataDir);
        File dataFile = new File(dataDir);
        if (!dataFile.exists()) {
            dataFile.mkdirs();
        }
        file.transferTo(new File(dataFile, fileName));
        String img = api + id + "img/" + fileName;
        System.out.println(img);
        System.out.println(fileName);
        String formName = "driver" + id;
        String sql = "select * from health" + id + " ORDER BY time DESC LIMIT 1;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Object longitude = list.get(0).get("longitude");
        Object latitude = list.get(0).get("latitude");
        // System.out.println(longitude.getClass());
        sql = "insert into " + formName +
                "(num,time,type,img,longitude,latitude)values('new','"
                + time + "'," + type + ",'" + img + "'," + longitude + "," + latitude + ");";
        jdbcTemplate.update(sql);
        return true;
    }

    // 查询单车次的健康数据
    @GetMapping("list/{id}/{num}")
    public result returnDate(
            @PathVariable("id") String id,
            @PathVariable("num") String num) {
        result res = new result();
        String sql = "select * from driver" + id + " where num = '" + num + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        res.code = list.size();
        res.data = list;
        res.msg = num + "的危险记录";
        return res;
    }

    // 查询所有车次
    @GetMapping("/searchAll/{id}")
    public result searchAll(
            @PathVariable("id") String id) {
        String formName = "driver" + id;
        String sql = "select num from " + formName + " group by num having num != 'new';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        result healthinfo = new result();
        healthinfo.code = list.size();
        healthinfo.data = list;
        healthinfo.msg = "所有的危险记录";
        return healthinfo;
    }

    // 查询最近n天的车次
    @GetMapping("/searchN/{id}/{n}")
    public result searchMonth(
            @PathVariable("id") String id,
            @PathVariable("n") int n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String formName = "driver" + id;
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
        res.msg = "近" + n + "天的危险记录";
        res.data = list;
        return res;
    }

    // 按照日期查询搜索 //年-月-日
    @PostMapping(path = "/search")
    public result searchFromAll(
            @RequestBody healthSearch healthinfo) {
        String formName = "driver" + healthinfo.id;
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
        res.msg = "搜索到的危险记录";
        return res;
    }
}
