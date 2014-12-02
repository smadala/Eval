package com.journaldev.spring.dao;

import com.journaldev.spring.model.Report;
import com.journaldev.spring.model.TestCase;

import java.util.List;

/**
 * Created by satya on 29/11/14.
 */
public interface ReportDAO {
    public void addReport( Report report);
    public void updateReport(Report report);
    public void removeReport(int id);
    public List<Report> listReportByTestCase_Id(int id);
    public Report getReportById(int id);

}
