package com.journaldev.spring.dao;

import com.journaldev.spring.model.TestCase;
import com.journaldev.spring.model.TestCase;
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
public class TestCaseDAOImpl implements TestCaseDAO {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }

    @Override
    public void addTestCase(TestCase testCase) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(testCase);
        logger.info("TestCase saved successfully, TestCase Details="+ testCase);
    }

    @Override
    public void updateTestCase(TestCase testCase) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(testCase);
        logger.info("TestCase updated successfully, TestCase Details="+testCase);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TestCase> listTestCases() {
        Session session = this.sessionFactory.getCurrentSession();
        List<TestCase> testCasesList = session.createQuery("from TestCase").list();
        for(TestCase testCase : testCasesList){
            logger.info("TestCase List::"+testCase);
        }
        return testCasesList;
    }

    @Override
    public TestCase getTestCaseById(int id) {
        Session session = this.sessionFactory.getCurrentSession();

//        TestCase testCase = (TestCase) session.load(TestCase.class, new Integer(id));
        Query query= session.createQuery(" From TestCase t where t.id=:id");
        query.setParameter("id", id);
        List<TestCase> testCaseList=query.list();
        logger.info("TestCase loaded successfully, TestCase details="+testCaseList.get(0));
        return testCaseList.get(0);
    }

    @Override
    public void removeTestCase(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        TestCase testCase = (TestCase) session.load(TestCase.class, new Integer(id));
        if(null != testCase){
            session.delete(testCase);
        }
        logger.info("TestCase deleted successfully, TestCase details="+testCase);
    }




}
