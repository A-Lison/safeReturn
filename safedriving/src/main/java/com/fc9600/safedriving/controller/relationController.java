package com.fc9600.safedriving.controller;

import java.util.List;
import java.util.Map;

import com.fc9600.safedriving.model.Relation;
import com.fc9600.safedriving.model.relationChange;
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
@RequestMapping("/relation")

public class relationController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    // 删除亲属
    @GetMapping("/delete/{relation}/{id}")
    public boolean delete(
            @PathVariable("relation") String relation,
            @PathVariable("id") String id) {
        String formName = "relation" + id;
        String sql = "delete from " + formName + " where relation = '" + relation + "';";
        jdbcTemplate.update(sql);
        return true;
    }

    // 查询亲属关系
    @GetMapping("/list/{id}")
    public result sList(@PathVariable("id") String id) {
        String formName = "relation" + id;
        String sql = "select * from " + formName + ";";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        result res = new result();

        if (list.size() != 0) {
            res.data = list;
            res.msg = "查询亲属关系成功";
            res.code = 0;
        } else {
            res.msg = "还没有绑定亲属关系";
            res.code = -1;
        }
        return res;
    }

    // 修改亲属关系
    @PostMapping(path = "change")
    public boolean changeRelation(@RequestBody relationChange rc) {
        String formName = "relation" + rc.id;
        String sql = "update " + formName + " set relation ='" + rc.newRe + "',phone='" + rc.phone
                + "' where relation='"
                + rc.oldRe + "';";
        jdbcTemplate.update(sql);
        return true;
    }

    // 添加亲属
    @PostMapping(path = "/add")
    public boolean healthAdd(@RequestBody Relation relation) {
        String formName = "relation" + relation.id;
        String sql = "select * from " + formName + " where relation ='" + relation.relation + "' or phone= '"
                + relation.num + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list.size() == 1) {
            return false;
        }
        sql = "insert into " + formName + " (relation,phone) value ('" + relation.relation + "','" + relation.num
                + "');";
        jdbcTemplate.update(sql);
        return true;
    }
}
