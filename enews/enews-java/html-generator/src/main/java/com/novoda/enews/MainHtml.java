package com.novoda.enews;

import com.googlecode.jatl.Html;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainHtml {

    /**
     * @param args https://api.slack.com/docs/oauth-test-tokens
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalStateException("You need to pass a Slack token as the first arg. See https://api.slack.com/web");
        }
        String slackToken = args[0];
        Scraper scraper = new Scraper.Factory().newInstance(slackToken);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().minusDays(7);
        Stream<ChannelHistory.Message> messageStream = scraper.scrape(start, end);

        String html = new NewsletterGenerator().generate(messageStream);
        System.out.println(html);
    }
}