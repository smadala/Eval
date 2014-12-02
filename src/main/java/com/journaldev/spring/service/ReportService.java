package com.journaldev.spring.service;

import com.journaldev.spring.model.Report;

import java.util.List;

/**
 * Created by satya on 29/11/14.
 */
public interface ReportService {
    public void addReport( Report report);
    public void updateReport(Report report);
    public void removeReport(int id);
    public List<Report> listReportByTestCase_Id(int id);
    public Report getReportById(int id);
}
