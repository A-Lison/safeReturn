package com.fc9600.safedriving.controller;

import java.util.List;
import java.util.Map;

import com.fc9600.safedriving.model.Person;
import com.fc9600.safedriving.model.UserInfo;
import com.fc9600.safedriving.model.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/userInfo")
public class userinfoController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private String id = "";
    // public String uId; // 小程序的openId
    // public String uName; // uid用户名 默认的为 #+uid
    // public int uAge;
    // public int uSex; // 男：2 女：1 空：0（刚注册的时候）
    // public String pNum; // 手机号

    // public Object returnOb() {
    // // Object res = new Object();
    // userinfoController res = new userinfoController();
    // res.uId = this.uId;
    // res.uName = this.uName;
    // res.pNum = this.pNum;
    // res.uSex = this.uSex;
    // res.uAge = this.uAge;
    // return res;
    // }

    // 新增用户
    // @GetMapping(value = "/add/{id}/{name}/{age}/{sex}/{num}")
    // public boolean addUser(@PathVariable("id") String id,
    // @PathVariable("name") String name,
    // @PathVariable("age") int age,
    // @PathVariable("sex") int sex,
    // @PathVariable("num") String num) {
    // // 插入语句，注意时间问题
    // String sql;
    // System.out.println("创建新的用户");
    // sql = "insert into userinfo(id,name,age,sex,phone)values (" + id + "," + name
    // + "," + age + "," + sex + ","
    // + num + ")";
    // jdbcTemplate.update(sql);
    // System.out.println("添加新用户信息");
    // // 创建该用户的relation表（关系，号码）
    // sql = "create table relation" + id + "(relation varchar(20),phone
    // varchar(20));";
    // jdbcTemplate.update(sql);
    // System.out.println("创建用户关系表");
    // // 创建该用户的driver表（图片，违规类型，时间）
    // sql = "create table driver" + id + "(time datetime,type int,pic blob);";
    // jdbcTemplate.update(sql);
    // System.out.println("创建用户驾驶图表");
    // // 创建该用户的health表（心率，血压，时间，酒精。。。）
    // sql = "create table health" + id
    // + "(time datetime,heart int,press int,alcohol int);";
    // jdbcTemplate.update(sql);
    // System.out.println("创建用户健康表");
    // return true;
    // }

    // 查询用户
    // @GetMapping("/search/{id}")
    // public UserInfo search(@PathVariable("id") String id) {
    // UserInfo userinfo = new UserInfo();
    // String sql = "select * from userinfo where id = " + id + ";";
    // List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    // if (list.size() != 0 && list != null) {
    // for (Map<String, Object> m : list) {
    // for (String k : m.keySet()) {
    // System.out.println(k + " : " + m.get(k));
    // this.s = this.s + "@" + m.get(k);
    // }
    // }
    // String res[] = s.split("@");
    // this.s = "";
    // userinfo.id = res[1];
    // userinfo.name = res[2];
    // userinfo.sex = Integer.valueOf(res[3]);
    // userinfo.age = Integer.valueOf(res[4]);
    // userinfo.phone = res[5];
    // System.out.println("查询用户成功");
    // return userinfo;
    // }
    // System.out.println("查询用户失败");
    // return null;
    // }
    public result search(String id) {
        String sql = "select * from userinfo where openid = '" + id + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        result res = new result();
        if (list.size() != 0) {
            Map<String, Object> map = list.get(0);
            System.out.println("查询用户成功");
            res.code = 0;
            res.msg = "查询成功";
            res.data = map;
        } else {
            System.out.println("查询用户失败");
            res.code = -1;
            res.msg = "未查询到该用户";
            res.data = null;
        }
        return res;
    }

    // 查询用户
    @PostMapping("/search")
    public result searchF(@RequestBody Person per) {
        result res = search(per.openid);
        return res;
    }

    // 登录
    @PostMapping("/login")
    public result login(
            @RequestBody Person per) {
        String num = per.num;
        System.out.println(per);
        // 判断是否为已注册用户
        this.id = new String(id);
        result userinfo = search(per.openid);
        if (userinfo.data != null) {
            System.out.println("用户存在");
            userinfo.msg = "登陆成功";
            return userinfo;
        }

        // 插入新的用户信息
        System.out.println("用户不存在创建新的用户");
        int age = 0;
        int sex = 0;
        String sql = "select count(*) from userinfo ;";
        List<Map<String, Object>> realId = jdbcTemplate.queryForList(sql);
        String RId = realId.get(0).get("count(*)").toString();
        System.out.println(RId);
        String name = "新用户" + RId;
        sql = "insert into userinfo(id,name,age,sex,phone,openid)values ('"
                + RId + "','"
                + name + "',"
                + age + ","
                + sex + ",'"
                + num + "','"
                + per.openid + "');";
        System.out.println(sql);
        jdbcTemplate.update(sql);
        System.out.println("添加新用户信息");
        // 创建该用户的relation表（关系，号码）
        sql = "create table relation" + RId + "(relation varchar(32) primary key,phone varchar(32));";
        jdbcTemplate.update(sql);
        System.out.println("创建用户亲属表");
        // 创建该用户的driver表（批次，时间，违规类型，图片地址）
        sql = "create table driver" + RId
                + "(num varchar(32),time datetime,type int,img varchar(225),longitude decimal(20,10),latitude decimal(20,10));";
        jdbcTemplate.update(sql);
        System.out.println("创建用户驾驶图表");
        // 创建该用户的health表（批次，时间，心率，血压，酒精，体温。。。）
        sql = "create table health" + RId
                + "(num varchar(32),time datetime,heart int,press double,alcohol double,heat double,longitude decimal(20,10),latitude decimal(20,10));";
        jdbcTemplate.update(sql);
        System.out.println("创建用户健康表");

        userinfo = search(per.openid);
        userinfo.msg = "用户创建成功";
        userinfo.code = 1;
        return userinfo;
    }

    // 更新用户信息
    // @GetMapping("/update/{id}/{name}/{age}/{sex}/{num}")
    // public boolean updateUser(
    // @PathVariable("id") String id,
    // @PathVariable("name") String name,
    // @PathVariable("age") String age,
    // @PathVariable("sex") String sex,
    // @PathVariable("num") String num) {
    // System.out.println("更新用户信息");
    // String sql = "update userinfo set name ='" + name + "',age=" + age + ",sex="
    // + sex + ",phone='" + num
    // + "' where id='" + id + "';";
    // jdbcTemplate.update(sql);
    // return true;
    // }
    @PostMapping(path = "/update")
    public boolean updateuserinfo(@RequestBody UserInfo userinfo) {
        System.out.println("更新用户信息");
        String sql = "update userinfo set name ='" + userinfo.name + "',age='" + userinfo.age + "',sex=" + userinfo.sex
                + ",phone='" + userinfo.phone + "' where id='" + userinfo.id + "';";
        jdbcTemplate.update(sql);
        return true;
    }

    @GetMapping(path = "/getId")
    public result getId() {
        result res = new result();
        res.data = this.id;
        return res;
    }
}
