package com.example.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {
    @Id    //id表示这个字段为主键
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "totle_request")
    private Integer totleRequest = 0;
    @Column(name = "used_number")
    private Integer usedNumber = 0;
    @Column(name = "apply_credibility_1")
    private Integer applyCredibility1 = 0;
    @Column(name = "apply_credibility_2")
    private Integer applyCredibility2 = 0;
    @Column(name = "apply_credibility_3")
    private Integer applyCredibility3 = 0;

    @Column(name = "node_status_credibility_1")
    private Integer nodeStatusCredibility1 = 0;
    @Column(name = "node_status_credibility_2")
    private Integer nodeStatusCredibility2 = 0;
    @Column(name = "node_status_credibility_3")
    private Integer nodeStatusCredibility3 = 0;

    @Column(name = "totle_credibility_1")
    private Integer totleCredibility1 = 0;
    @Column(name = "totle_credibility_2")
    private Integer totleCredibility2 = 0;
    @Column(name = "totle_credibility_3")
    private Integer totleCredibility3 = 0;

    @Column(name = "last_is_malicious")
    private Integer lastIsMalicious;
    @Column(name = "thsi_is_malicious")
    private Integer thsiIsMalicious;

    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTotleRequest() {
        return totleRequest;
    }

    public void setTotleRequest(Integer totleRequest) {
        this.totleRequest = totleRequest;
    }

    public Integer getUsedNumber() {
        return usedNumber;
    }

    public void setUsedNumber(Integer usedNumber) {
        this.usedNumber = usedNumber;
    }

    public Integer getApplyCredibility1() {
        return applyCredibility1;
    }

    public void setApplyCredibility1(Integer applyCredibility1) {
        this.applyCredibility1 = applyCredibility1;
    }

    public Integer getApplyCredibility2() {
        return applyCredibility2;
    }

    public void setApplyCredibility2(Integer applyCredibility2) {
        this.applyCredibility2 = applyCredibility2;
    }

    public Integer getApplyCredibility3() {
        return applyCredibility3;
    }

    public void setApplyCredibility3(Integer applyCredibility3) {
        this.applyCredibility3 = applyCredibility3;
    }

    public Integer getNodeStatusCredibility1() {
        return nodeStatusCredibility1;
    }

    public void setNodeStatusCredibility1(Integer nodeStatusCredibility1) {
        this.nodeStatusCredibility1 = nodeStatusCredibility1;
    }

    public Integer getNodeStatusCredibility2() {
        return nodeStatusCredibility2;
    }

    public void setNodeStatusCredibility2(Integer nodeStatusCredibility2) {
        this.nodeStatusCredibility2 = nodeStatusCredibility2;
    }

    public Integer getNodeStatusCredibility3() {
        return nodeStatusCredibility3;
    }

    public void setNodeStatusCredibility3(Integer nodeStatusCredibility3) {
        this.nodeStatusCredibility3 = nodeStatusCredibility3;
    }

    public Integer getTotleCredibility1() {
        return totleCredibility1;
    }

    public void setTotleCredibility1(Integer totleCredibility1) {
        this.totleCredibility1 = totleCredibility1;
    }

    public Integer getTotleCredibility2() {
        return totleCredibility2;
    }

    public void setTotleCredibility2(Integer totleCredibility2) {
        this.totleCredibility2 = totleCredibility2;
    }

    public Integer getTotleCredibility3() {
        return totleCredibility3;
    }

    public void setTotleCredibility3(Integer totleCredibility3) {
        this.totleCredibility3 = totleCredibility3;
    }

    public Integer getLastIsMalicious() {
        return lastIsMalicious;
    }

    public void setLastIsMalicious(Integer lastIsMalicious) {
        this.lastIsMalicious = lastIsMalicious;
    }

    public Integer getThsiIsMalicious() {
        return thsiIsMalicious;
    }

    public void setThsiIsMalicious(Integer thsiIsMalicious) {
        this.thsiIsMalicious = thsiIsMalicious;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
