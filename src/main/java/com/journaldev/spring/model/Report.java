package com.journaldev.spring.model;

import javax.net.ssl.SSLEngineResult;
import javax.persistence.*;

/**
 * Created by satya on 29/11/14.
 */
@Entity
@Table(name="REPORT")
public class Report {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String status;
    private long min;
    private long max;
    private long average;
    private long samples;

    @ManyToOne
    @JoinColumn(name = "testcase_id")
    private TestCase testCase;

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getAverage() {
        return average;
    }

    public void setAverage(long average) {
        this.average = average;
    }

    public long getSamples() {
        return samples;
    }

    public void setSamples(long samples) {
        this.samples = samples;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", min=" + min +
                ", max=" + max +
                ", average=" + average +
                ", samples=" + samples +
                '}';
    }
}
