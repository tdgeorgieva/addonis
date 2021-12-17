package com.addonis.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("")
public class GitHubAPIClient {

    //download link

    public String getOwner(String link) {
        String[] arr = link.split("/");
        return arr[3];
    }

    public String getRepositoryName(String link) {
        String[] arr = link.split("/");
        return arr[4];
    }

    public int getOpenIssuesCount(String owner, String name) {
        try {
            String url = String.format("https://api.github.com/repos/%s/%s/issues", owner, name);
            String charset = StandardCharsets.UTF_8.name();
            String state = "open";

            String query = String.format("state=%s",
                    URLEncoder.encode(state, charset));

            URL connection = new URL(url + "?" + query);
            connection.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.openStream()));

            StringBuilder content = new StringBuilder();
            String inputLine;
            String[] str;
            String[] count;
            while ((inputLine = in.readLine()) != null) {
                str = inputLine.split(",");
                if (str.length == 1) {
                    content.append(0);
                    break;
                }
                count = str[8].split(":");
                content.append(count[1]);
            }

            in.close();

            return Integer.parseInt(content.toString());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    public int getPullRequestsCount(String owner, String name) {
        try {
            String url = String.format("https://api.github.com/repos/%s/%s/", owner, name);
            String charset = StandardCharsets.UTF_8.name();
            String pulls = "pulls";

            String query = URLEncoder.encode(pulls, charset);

            URL connection = new URL(url + query);
            connection.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.openStream()));

            StringBuilder content = new StringBuilder();
            String inputLine;
            String[] str;
            String[] count;
            while ((inputLine = in.readLine()) != null) {
                str = inputLine.split(",");
                if (str.length == 1) {
                    content.append(0);
                    break;
                }
                count = str[7].split(":");
                content.append(count[1]);
            }

            in.close();
            return Integer.parseInt(content.toString());

        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    public String getLastCommitInfo(String owner, String name) {
        try {
            String url = String.format("https://api.github.com/repos/%s/%s/", owner, name);
            String charset = StandardCharsets.UTF_8.name();
            String commits = "commits";

            String query = URLEncoder.encode(commits, charset);

            URL connection = new URL(url + query);
            connection.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.openStream()));

            StringBuilder content = new StringBuilder();
            String inputLine;
            String[] str;
            String[] message;
            String[] date;
            while ((inputLine = in.readLine()) != null) {
                str = inputLine.split(",");
                if (str.length == 1) {
                    content.append("There are no commits.");
                    break;
                }
                message = str[8].split(":");
                date = str[66].split(":");

                content.append(String.format("Last commit message: %s: ", message[1]));
                content.append(String.format("Date: %s", date[1].substring(1, date[1].length() - 3)));
            }

            in.close();
            return content.toString();

        } catch (IOException e) {
            return "This repository has 0 commits.";
        }
    }
}
