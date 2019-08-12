package by.sb.jirahelper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "data")
public class DataPropeties {

    private List<String> environments;
    private List<String> namespaces;
}
