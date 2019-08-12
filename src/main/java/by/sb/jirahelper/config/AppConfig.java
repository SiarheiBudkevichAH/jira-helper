package by.sb.jirahelper.config;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class AppConfig {

    @Autowired
    private JiraProperties jiraProperties;

    @Bean
    public JiraRestClient restClient() {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(
                        URI.create(jiraProperties.getUrl()),
                        jiraProperties.getUsername(),
                        jiraProperties.getPassword()
                );
    }
}
