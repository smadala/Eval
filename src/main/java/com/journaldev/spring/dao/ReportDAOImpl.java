package com.journaldev.spring.dao;

import com.journaldev.spring.model.Report;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by satya on 29/11/14.
 */
@Repository
public class ReportDAOImpl implements ReportDAO {
    private static final Logger logger= LoggerFactory.getLogger(ReportDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf){
        sessionFactory=sf;
    }

    @Override
    public void addReport( Report report){
        Session session=this.sessionFactory.getCurrentSession();
        session.persist(report);
        logger.info("report saved successfully, report Details="+ report);
    }
    @Override
    public void updateReport(Report report){
        Session session=this.sessionFactory.getCurrentSession();
        session.update(report);
        logger.info("report updated successfully, report Details="+report);
    }

    @Override
    public void removeReport(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Report report = (Report) session.load(Report.class, new Integer(id));
        if(null != report){
            session.delete(report);
        }
        logger.info("report deleted successfully, report details="+report);
    }

    @Override
    public List<Report> listReportByTestCase_Id(int testCaseId) {
        Session session = this.sessionFactory.getCurrentSession();
        Query query=session.createQuery("SELECT r FROM Report r WHERE r.testCase.id = :testCaseId");
        query.setParameter( "testCaseId", testCaseId);

        List<Report> reportsList = query.list();
        for(Report report : reportsList){
            logger.info("Report List::"+report);
        }
        return reportsList;
    }

    @Override
    public Report getReportById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Report report = (Report) session.load(Report.class, new Integer(id));
        logger.info("Report loaded successfully, Report details="+report);
        return report;
    }
}
