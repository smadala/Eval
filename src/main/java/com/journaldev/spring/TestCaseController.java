package com.journaldev.spring;

import com.journaldev.spring.model.TestCase;
import com.journaldev.spring.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by satya on 29/11/14.
 */
@Controller
public class TestCaseController {
    private TestCaseService testCaseService;

    @Autowired(required=true)
    @Qualifier(value="testCaseService")
    public void setTestCaseService(TestCaseService ts){
        this.testCaseService = ts;
    }

    @RequestMapping(value = "/testcases", method = RequestMethod.GET)
    public String listTestCases(Model model) {
        model.addAttribute("testCase", new TestCase());
        model.addAttribute("listTestCases", this.testCaseService.listTestCases());
        return "testCase";
    }

    //For add and update testCase both
    @RequestMapping(value= "/testcase/add", method = RequestMethod.POST)
    public String addTestCase(@ModelAttribute("testCase") TestCase testCase){

        if(testCase.getId() == 0){
            //new testCase, add it
            this.testCaseService.addTestCase(testCase);


        }else{
            //existing testCase, call update
            this.testCaseService.updateTestCase(testCase);
        }

        return "redirect:/testcases";

    }

    @RequestMapping("/testcase/remove/{id}")
    public String removeTestCase(@PathVariable("id") int id){

        this.testCaseService.removeTestCase(id);
        return "redirect:/testcases";
    }

    @RequestMapping("/testcase/edit/{id}")
    public String editTestCase(@PathVariable("id") int id, Model model){
        model.addAttribute("testCase", this.testCaseService.getTestCaseById(id));
        model.addAttribute("listTestCases", this.testCaseService.listTestCases());
        return "testCase";
    }
    @RequestMapping("/run/{id}")
    public String runTestCase( @PathVariable("id") int id){

        testCaseService.runTest(id);
        return "redirect:/testcases";
    }
}
