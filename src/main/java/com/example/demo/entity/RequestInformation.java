package com.example.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "request_information")
public class RequestInformation {
    @Id    //id表示这个字段为主键
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "bridge_node_id")
    private Integer bridgeNodeId;
    @Column(name = "is_used")
    private Integer isUsed;
    @Column(name = "is_deal")
    private Integer isDeal;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "apply_credibility")
    private Integer applyCredibility;
    @Column(name = "node_status_credibility")
    private Integer nodeStatusCredibility;
    @Column(name = "totle_credibility")
    private Integer totleCredibility;

    public Integer getApplyCredibility() {
        return applyCredibility;
    }

    public void setApplyCredibility(Integer applyCredibility) {
        this.applyCredibility = applyCredibility;
    }

    public Integer getNodeStatusCredibility() {
        return nodeStatusCredibility;
    }

    public void setNodeStatusCredibility(Integer nodeStatusCredibility) {
        this.nodeStatusCredibility = nodeStatusCredibility;
    }

    public Integer getTotleCredibility() {
        return totleCredibility;
    }

    public void setTotleCredibility(Integer totleCredibility) {
        this.totleCredibility = totleCredibility;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBridgeNodeId() {
        return bridgeNodeId;
    }

    public void setBridgeNodeId(Integer bridgeNodeId) {
        this.bridgeNodeId = bridgeNodeId;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(Integer isDeal) {
        this.isDeal = isDeal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
