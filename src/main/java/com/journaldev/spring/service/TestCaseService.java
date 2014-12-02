package com.journaldev.spring.service;

import com.journaldev.spring.model.TestCase;

import java.util.List;

/**
 * Created by satya on 29/11/14.
 */
public interface TestCaseService {
    public void addTestCase(TestCase testCase);
    public void updateTestCase(TestCase testCase);
    public List<TestCase> listTestCases();
    public TestCase getTestCaseById(int id);
    public void removeTestCase(int id);
    public void runTest(int id);
}
