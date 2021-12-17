package com.addonis.controllers;

import com.addonis.models.addon.Addon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    private static final GitHubAPIClient gitHubAPIClient = new GitHubAPIClient();

    public static void setGitHubInformation(Addon addon) {
        String owner = gitHubAPIClient.getOwner(addon.getLink());
        String name = gitHubAPIClient.getRepositoryName(addon.getLink());
        int issues = gitHubAPIClient.getOpenIssuesCount(owner, name);
        int pulls = gitHubAPIClient.getPullRequestsCount(owner, name);
        addon.setIssues(issues);
        addon.setPulls(pulls);
        String[] lastCommitInfo = gitHubAPIClient.getLastCommitInfo(owner, name).split(": ");
        if(lastCommitInfo.length == 1) {
            addon.setLastCommitTitle("No commits.");
            addon.setLastCommitDate(LocalDate.parse("1900-01-01"));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(lastCommitInfo[3], formatter);
            addon.setLastCommitDate(date);
            addon.setLastCommitTitle(lastCommitInfo[1].substring(1, lastCommitInfo[1].length() - 1));
        }
    }

}

