package by.sb.jirahelper;

import by.sb.jirahelper.config.DataPropeties;
import by.sb.jirahelper.config.JiraProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        JiraProperties.class,
        DataPropeties.class
})
public class JiraHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiraHelperApplication.class, args);
    }

}
