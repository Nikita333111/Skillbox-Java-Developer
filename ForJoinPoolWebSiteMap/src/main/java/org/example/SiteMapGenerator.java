package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveTask;

public class SiteMapGenerator extends RecursiveTask<String> {

    private String url;
    private static String startUrl;
    private static CopyOnWriteArraySet<String> allLinks = new CopyOnWriteArraySet<>();

    public SiteMapGenerator(String url){
        this.url = url;
    }

    public SiteMapGenerator(String url, String startUrl) {
        this.url = url.trim();
        SiteMapGenerator.startUrl = startUrl.trim();
    }

    @Override
    protected String compute() {
        StringBuffer sb = new StringBuffer(url + "\n");
        Set<SiteMapGenerator> taskSet = new HashSet<>();

        getChildren(taskSet);

        for (SiteMapGenerator task : taskSet) {
            sb.append(task.join());
        }

        return sb.toString();
    }

    public void getChildren(Set<SiteMapGenerator> taskSet){
        Document doc;
        List<String> elements;
        try {
            Thread.sleep(200);
            doc = Jsoup.connect(url).get();
            elements = doc.select("a").eachAttr("abs:href");

            for (String el : elements) {
                if (!el.isEmpty() && el.startsWith(startUrl) && !allLinks.contains(el) && !el.contains("#")){
                    SiteMapGenerator task = new SiteMapGenerator(el);
                    task.fork();
                    taskSet.add(task);
                    allLinks.add(el);
                }
            }
        }catch (Exception ex){
            System.out.println("kakaiato oshibka chtenia !!!" + ex.getMessage());
        }
    }
}
