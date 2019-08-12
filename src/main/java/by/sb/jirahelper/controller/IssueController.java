package by.sb.jirahelper.controller;

import by.sb.jirahelper.config.DataPropeties;
import by.sb.jirahelper.model.IssueDto;
import by.sb.jirahelper.model.SuggestionDto;
import by.sb.jirahelper.service.JiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IssueController {

    @Autowired
    private JiraService jiraService;

    @Autowired
    private DataPropeties dataPropeties;

    @GetMapping("index")
    public String index(Model model) {
        model.addAttribute("issue", new IssueDto());
        model.addAttribute("createIssueUrl", null);
        return "index";
    }

    @PostMapping("/issues/create")
    public String createIssue(@ModelAttribute("issue") IssueDto issue, BindingResult result, Model model) {
        final String issueUrl = jiraService.createIssue(issue);
        model.addAttribute("createIssueUrl", issueUrl);
        return "index";
    }

    @GetMapping(value = "/suggestion", produces = "application/json")
    @ResponseBody
    public SuggestionDto autocompleteSuggestions(@RequestParam("searchstr") String searchstr) {
        return jiraService.getSuggestion(searchstr);
    }

    @ModelAttribute("environments")
    public List<String> environments() {
        return dataPropeties.getEnvironments();
    }

    @ModelAttribute("namespaces")
    public List<String> namespaces() {
        return dataPropeties.getNamespaces();
    }
}
