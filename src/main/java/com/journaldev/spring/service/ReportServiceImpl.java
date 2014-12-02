package com.journaldev.spring.service;

import com.journaldev.spring.dao.ReportDAO;
import com.journaldev.spring.model.Report;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by satya on 29/11/14.
 */
@Service
public class ReportServiceImpl implements ReportService{

    private ReportDAO reportDAO;

    public void setReportDAO(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    @Override
    @Transactional
    public void addReport(Report report) {

        reportDAO.addReport(report);
    }

    @Override
    @Transactional
    public void updateReport(Report report) {

        reportDAO.updateReport(report);
    }

    @Override
    @Transactional
    public void removeReport(int id) {

        reportDAO.removeReport(id);
    }

    @Override
    @Transactional
    public List<Report> listReportByTestCase_Id(int id) {
        return reportDAO.listReportByTestCase_Id(id);
    }

    @Override
    @Transactional
    public Report getReportById(int id) {
        return reportDAO.getReportById(id);
    }
}
