package com.fc9600.safedriving.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alco")
public class alcoholController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    private double alco;

    @GetMapping("/sendAlco/{alco}/{id}")
    public boolean sendAlco(@PathVariable("alco") double alco, @PathVariable("id") String id) {
        this.alco = alco;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(alco + " " + id);
        Date en = new Date();
        String date = df.format(en);
        System.out.println(en);
        String sql = "select * from health" + id + " ORDER BY time DESC LIMIT 1;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Object longitude = list.get(0).get("longitude");
        Object latitude = list.get(0).get("latitude");
        sql = "insert into driver" + id +
                "(num,time,type,img_or_alco,longitude,latitude)values('new','"
                + date + "','5',null," + longitude + "," + latitude + ");";
        System.out.println(sql);
        jdbcTemplate.update(sql);
        return true;
    }

    @GetMapping("/getAlco")
    public double getAlco() {
        return this.alco;
    }
}
