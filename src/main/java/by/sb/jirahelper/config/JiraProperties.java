package by.sb.jirahelper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jira")
public class JiraProperties {

    private String username;
    private String password;
    private String url;
    private String project;
    private String ciUserName;
}
