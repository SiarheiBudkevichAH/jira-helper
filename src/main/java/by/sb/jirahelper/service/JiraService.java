package by.sb.jirahelper.service;

import by.sb.jirahelper.config.JiraProperties;
import by.sb.jirahelper.model.IssueDto;
import by.sb.jirahelper.model.SuggestionDto;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@DependsOn("restClient")
public class JiraService {

    private static final String LABELS_TEMPLATE = "environment=%s,namespace=%s";
    private List<String> fixVersions;

    @Autowired
    private JiraRestClient restClient;
    @Autowired
    private JiraProperties jiraProperties;

    public String createIssue(IssueDto issue) {
        log.debug("createIssue() - start");

        IssueRestClient issueClient = restClient.getIssueClient();

        IssueInput newIssue = new IssueInputBuilder()
                .setProjectKey(jiraProperties.getProject())
                .setIssueTypeId(33L) // Build
                .setSummary("Build " + issue.getFixVersions())
                .setDescription("Build " + issue.getFixVersions())
                .setComponentsNames(Arrays.asList("auth", "user"))
                .setFixVersionsNames(Arrays.asList(issue.getFixVersions().trim().split(",")))
                .setAffectedVersionsNames(Arrays.asList("auth_develop", "user_develop"))
                .setFieldValue("labels", generateLabels(issue))
                .setFieldValue("environment", String.format(
                        "jira_host",
                        issue.getEnvironment(),
                        issue.getNamespace()
                        )
                )
                .setAssigneeName(jiraProperties.getCiUserName())
                .build();

        final String issueId = issueClient.createIssue(newIssue).claim().getKey();
        log.debug("createIssue() - end: issueId = {}", issueId);

        return jiraProperties.getUrl() + "/browse/" + issueId;
    }

    private List<String> generateLabels(IssueDto issue) {
        final List<String> labels = new ArrayList<>(Arrays.asList(String.format(LABELS_TEMPLATE, issue.getEnvironment(), issue.getNamespace()).split(",")));

        if (StringUtils.isNotBlank(issue.getUpdateDb())) {
            labels.add(issue.getUpdateDb());
        }

        if (StringUtils.isNotBlank(issue.getUpdateSecrets())) {
            labels.add(issue.getUpdateSecrets());
        }

        if (StringUtils.isNotBlank(issue.getUpdateConfigMap())) {
            labels.add(issue.getUpdateConfigMap());
        }

        if (StringUtils.isNotBlank(issue.getExternalAccess())) {
            labels.add(issue.getExternalAccess());
        }

        return labels;
    }

    public SuggestionDto getSuggestion(String searchstr) {
        log.debug("searchstr = {}", searchstr);
        List<String> suggestions = new ArrayList<>();

        fixVersions.forEach(version -> {
            if (version.toLowerCase().contains(searchstr.toLowerCase())) {
                suggestions.add(version);
            }
        });

        // truncate the list to the first n, max 20 elements
        int n = suggestions.size() > 20 ? 20 : suggestions.size();
        List<String> sulb = new ArrayList<>(suggestions.subList(0, n));

        SuggestionDto sw = new SuggestionDto();
        sw.setSuggestions(sulb);
        return sw;
    }

    @PostConstruct
    public void init() {
        Project project = restClient.getProjectClient().getProject(jiraProperties.getProject()).claim();
        fixVersions = StreamSupport.stream(project.getVersions().spliterator(), false)
                .map(Version::getName)
                .collect(Collectors.toList());
    }
}
