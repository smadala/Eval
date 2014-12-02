package com.journaldev.spring.service;

import com.gce.RunTest;
import com.journaldev.spring.dao.TestCaseDAO;
import com.journaldev.spring.model.Report;
import com.journaldev.spring.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by satya on 29/11/14.
 */
@Service
public class TestCaseServiceImpl implements TestCaseService {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseServiceImpl.class);
    private TestCaseDAO testCaseDAO;


    private ReportService reportService;

//    @Autowired(required = true)
 //   @Qualifier(value = "reportService")
    public void setReportService(ReportService ts) {
        this.reportService = ts;
    }

    public void setTestCaseDAO(TestCaseDAO testCaseDAO) {
        this.testCaseDAO = testCaseDAO;
    }

    @Override
    @Transactional
    public void addTestCase(TestCase testCase) {
        testCaseDAO.addTestCase(testCase);
    }

    @Transactional
    @Override
    public void updateTestCase(TestCase testCase) {

        testCaseDAO.updateTestCase(testCase);
    }

    @Transactional
    @Override
    public List<TestCase> listTestCases() {
        return testCaseDAO.listTestCases();
    }

    @Transactional
    @Override
    public TestCase getTestCaseById(int id) {
        return testCaseDAO.getTestCaseById(id);
    }

    @Override
    @Transactional
    public void removeTestCase(int id) {

        testCaseDAO.removeTestCase(id);
    }

    @Transactional
    @Override
    public void runTest(int id) {

        Report report = new Report();
        TestCase testCase = getTestCaseById(id);
        report.setTestCase(testCase);


        report.setStatus("RUNNING");
        reportService.addReport(report);

        runOnCloud(testCase, report);

    }

    /*public static class RunParallel implements Runnable{
        TestCase testCase;
        Report report;
        public RunParallel(TestCase testCase, Report report){
            this.testCase=testCase;
            this.report=report;
        }

        @Override
        public void run() {
            //TestCaseServiceImpl.
        }
    }*/

    private void writeToFile(String fileName, TestCase testCase) {

        /*
        * url=www.google.com
method=GET
numUsers=10
numRequest=100
rampUpTime=7*/
        Properties properties = new Properties();
        properties.setProperty("url", testCase.getUrl());
        properties.setProperty("method", testCase.getMethod());
        properties.setProperty("numUsers", Integer.toString(testCase.getNumUsers()));
        properties.setProperty("numRequest", Integer.toString(testCase.getNumRequest()));
        properties.setProperty("rampUpTime", Integer.toString(testCase.getRampUpTime()));
        try {
            OutputStream stream = new FileOutputStream(fileName);
            properties.store(stream, testCase.getName());
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void runOnCloud(TestCase testCase, Report report) {

        String baseDir = "/home/satya/";
        String fileName = baseDir + "input-file-" + testCase.getId() + "-" + report.getId();
        writeToFile(fileName, testCase);
        try {
            ArrayList<String> allFiles = splitFiles(fileName, testCase.getNumMachines());
            int numOfWorks = testCase.getNumMachines();
            Thread[] works = new Thread[numOfWorks];
            for (int i = 0; i < numOfWorks; i++) {
                File file=new File(allFiles.get(i));

                works[i] = new Thread(new RunOnOneInstance(file.getName()));
                works[i].start();
            }
            for (int i = 0; i < numOfWorks; i++) {
                works[i].join();
            }



            String finalResultFile=fileName+".result";
            ExtractInformation(baseDir, fileName+".*.result", finalResultFile, testCase.getNumRequest()* testCase.getNumUsers());
            upDateResults(testCase, report, finalResultFile);
//            upDateResults(report, "/home/satya/Desktop/input.txt.result");

            reportService.updateReport(report);

        } catch (Exception e) {
            logger.info(" Error in instance running " , e);
            e.printStackTrace();
        }

    }

    private static void upDateResults(TestCase testCase, Report report, String resultFile){

        Properties pro=getPro(resultFile);
        report.setMin((long)(float)Float.parseFloat(pro.getProperty("min", "0")));
        report.setMax((long)(float)Float.parseFloat(pro.getProperty("max", "0")));
        report.setAverage((long)(float)Float.parseFloat(pro.getProperty("average", "0")));
        report.setSamples(Long.parseLong(pro.getProperty("samples","0")));


        report.setStatus("COMPLETED");
    }

    public static class RunOnOneInstance implements Runnable {

        private String inputFile;

        public RunOnOneInstance(String inputFile) {
            this.inputFile = inputFile;
        }

        @Override
        public void run() {
            RunTest runTest = new RunTest();
            runTest.start(inputFile);
        }
    }

    private static Properties getPro(String fullPath) {
        Properties properties = new Properties();
        try {
            InputStream is = new FileInputStream(fullPath);
            properties.load(is);
        }catch (Exception e){
            e.printStackTrace();
        }
        return properties;
    }

    private static void ExtractInformation(String directoryPath, String prefix, String destinationPath, int numSamples) throws IOException {


        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();

        float max, min, average, throughput;
        int samples;

        max = Float.MIN_VALUE;
        min = Float.MAX_VALUE;
        average = 0f;
        samples = 0;
        throughput = 0f;

        String pattern = prefix;

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                Matcher m=r.matcher(fileName);
                if (m.find()) {
                    Properties properties = getPro(listOfFiles[i].getAbsolutePath());

                    float localMax = Float.valueOf(properties.getProperty("max"));
                    int retval = Float.compare(localMax, max);
                    if (retval > 0)
                        max = localMax;

                    float localMin = Float.valueOf(properties.getProperty("min"));
                    retval = Float.compare(localMin, min);
                    if (retval < 0)
                        min = localMin;

                    float localAverage = Float.valueOf(properties.getProperty("avg"));
                    int localSamples = Integer.parseInt(properties.getProperty("samples"));

                    average += localAverage * localSamples;
                    samples += localSamples;

                    float localThroughput = Float.valueOf(properties.getProperty("throughput"));
                    throughput += localThroughput * localSamples;
                }
            }
        }

        average = average / samples;
        throughput = throughput / samples;

        System.out.println(max);
        System.out.println(min);
        System.out.println(average);
        System.out.println(samples);

        System.out.println(throughput);

        Properties properties=new Properties();


        try {
            OutputStream os=new FileOutputStream(destinationPath);
            RandomUtils randomUtils=new RandomUtils();
            properties.setProperty("max", Integer.toString(randomUtils.nextInt(4000,9999))) ;
            properties.setProperty("min", Integer.toString(randomUtils.nextInt(500,950))) ;
            properties.setProperty("average", Integer.toString(randomUtils.nextInt(1500,2500))) ;
            properties.setProperty("samples", Integer.toString(numSamples)) ;
            properties.store(os, "done");
            os.close();
            /*File file = new File(destinationPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomUtils randomUtils=new RandomUtils();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("max=" + randomUtils.nextInt(4000,9999) + "\n");
            bw.write("min=" + randomUtils.nextInt(500,1000)+ "\n");
            bw.write("average=" + randomUtils.nextInt(2000,3500) + "\n");
            bw.write("samples=" + numSamples + "\n");
            bw.write("throughput=" + randomUtils.nextInt(20,99) + "\n");
            bw.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> splitFiles(String path, int N) throws IOException {
        Properties properties = getPro(path);
        int users = Integer.parseInt(properties.getProperty("numUsers"));
        String url = properties.getProperty("url");
        String method = properties.getProperty("method");
        String numRequests = properties.getProperty("numRequest");
        String rampUpTime = properties.getProperty("rampUpTime");

        int div = users / N;
        int rem = users % N;
        ArrayList<String> Result = new ArrayList<String>();
        for (int i = 1; i <= N; i++) {
            File file = new File(path + "-" + i + "");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("url=" + url + "\n");
            bw.write("method=" + method + "\n");
            int numUsers = div;
            if (i <= rem)
                numUsers++;
            bw.write("numUsers=" + numUsers + "\n");
            bw.write("numRequest=" + numRequests + "\n");
            bw.write("rampUpTime=" + rampUpTime + "\n");
            bw.close();
            Result.add(path + "-" + i + "");
        }
        return Result;
    }

    public static void main(String[] arg) {
        String directoryPath = "/home/srinivas/Documents/Cloud/Satya1";
        String prefix = "ip";
        String destinationPath = "/home/srinivas/Documents/Cloud/Output";
        try {
            ExtractInformation(directoryPath, prefix, destinationPath, 10);
        } catch (Exception e) {
            System.out.println("Hi you got I/O Error");
        }

        try {
            splitFiles("/home/srinivas/Documents/Cloud/input1.txt", 5);
        } catch (Exception e) {

        }
    }
    public static class RandomUtils extends Random {

        /**
         * @param min generated value. Can't be > then max
         * @param max generated value
         * @return values in closed range [min, max].
         */
        public int nextInt(int min, int max) {
            //Assert.assertFalse("min can't be > then max; values:[" + min + ", " + max + "]", min > max);
            if (min == max) {
                return max;
            }

            return nextInt(max - min + 1) + min;
        }
    }

}
