package com.journaldev.spring.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by satya on 29/11/14.
 */
@Entity
@Table(name="TESTCASE")
public class TestCase {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String name;
    private String url;
    private String method;

    private int numUsers;
    private int numRequest;
    private int rampUpTime;

    private int numMachines;

    @OneToMany(mappedBy = "testCase")

    private Set<Report> reportSet;

    public Set<Report> getReportSet() {
        return reportSet;
    }

    public void setReportSet(Set<Report> reportSet) {
        this.reportSet = reportSet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }

    public int getNumRequest() {
        return numRequest;
    }

    public void setNumRequest(int numRequest) {
        this.numRequest = numRequest;
    }

    public int getRampUpTime() {
        return rampUpTime;
    }

    public void setRampUpTime(int rampUpTime) {
        this.rampUpTime = rampUpTime;
    }

    public int getNumMachines() {
        return numMachines;
    }

    public void setNumMachines(int numMachines) {
        this.numMachines = numMachines;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", numUsers=" + numUsers +
                ", numRequest=" + numRequest +
                ", rampUpTime=" + rampUpTime +
                '}';
    }
}
