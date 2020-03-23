package com.example.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bridge_node")
public class BridgeNode {
    @Id    //id表示这个字段为主键
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ip")
    private String ip;
    @Column(name = "is_distribute")
    private Integer isDistribute;
    @Column(name = "is_valid")
    private Integer isValid;
    @Column(name = "create_time")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getIsDistribute() {
        return isDistribute;
    }

    public void setIsDistribute(Integer isDistribute) {
        this.isDistribute = isDistribute;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
