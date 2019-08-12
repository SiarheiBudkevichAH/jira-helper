package by.sb.jirahelper.model;

import lombok.Data;

@Data
public class IssueDto {
    private String projectId;
    private Long issueId;
    private String fixVersions;

    private String namespace;
    private String environment;

    private String updateDb;
    private String updateSecrets;
    private String updateConfigMap;
    private String externalAccess;
}
