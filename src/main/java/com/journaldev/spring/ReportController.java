package com.journaldev.spring;

import com.journaldev.spring.service.ReportService;
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
public class ReportController {
    private ReportService reportService;

    @Autowired(required=true)
    @Qualifier(value="reportService")
    public void setReportService(ReportService ts){
        this.reportService = ts;
    }

    @RequestMapping(value = "/testcase/{id}/reports", method = RequestMethod.GET)
    public String listReports(Model model,@PathVariable("id") int id) {

        model.addAttribute("testCaseId", id);
        model.addAttribute("listReports", this.reportService.listReportByTestCase_Id(id));
        return "report";
    }


    @RequestMapping("/testcase/{testCaseId}/report/remove/{id}")
    public String removeReport(@PathVariable("id") int id, @PathVariable("testCaseId") int testCaseId){

        this.reportService.removeReport(id);
        return "redirect:/testcase/"+testCaseId+"/reports";
    }
}
