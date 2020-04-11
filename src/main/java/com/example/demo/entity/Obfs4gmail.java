package com.example.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "obfs4gmail")
public class Obfs4gmail {
    @Id    //id表示这个字段为主键
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "host")
    private String host;
    @Column(name = "port")
    private String port;
    @Column(name = "fingerprint")
    private String fingerprint;
    @Column(name = "cert")
    private String cert;
    @Column(name = "iat_mode")
    private Integer iatMode;
    @Column(name = "username")
    private String username;
    @Column(name = "time")
    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public Integer getIatMode() {
        return iatMode;
    }

    public void setIatMode(Integer iatMode) {
        this.iatMode = iatMode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
