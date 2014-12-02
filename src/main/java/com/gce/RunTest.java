package com.gce;

import com.gce.bean.TestBean;
import com.google.api.services.compute.Compute;

/**
 * Created by satya on 28/11/14.
 */
public class RunTest {
    private TestBean testBean;


    public void runTest(String fromDir, String jarName, String destName){
        Compute compute=ComputeServiceAccountSample.getCompute();
        ComputeServiceAccountSample.createInstance(compute, testBean);
        ComputeServiceAccountSample.runTask(testBean.testName, fromDir, jarName, destName);
        ComputeServiceAccountSample.deleteTestSetup(compute, testBean);
    }

    public void start(String name){

        RunTest runTest=new RunTest();
        runTest.testBean=new TestBean();
        runTest.testBean.testName=name;
        runTest.runTest("/home/satya/", "cloud.jar", "/home/sattimadala_gmail_com/");
        //runTest.runTest(args[1], args[2], args[3]);

    }
}
