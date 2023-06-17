package org.example;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        List<String> urlList = ParseImages("https://lenta.ru/");
        urlList.forEach(url -> url = url.trim());

        Long time = System.currentTimeMillis();
        for (String url: urlList) {
            if(downloadImg(url))
                System.out.println("file created");
            else
                System.out.println("file doesnt created !!!");
        }
        Long timeAfter = System.currentTimeMillis();
        System.out.println(timeAfter - time);
    }

    public static boolean downloadImg(String url){
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream()); FileOutputStream fout = new FileOutputStream("images/" + url.substring(url.lastIndexOf("/")))) {
            byte[] data = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, 1024);
                fout.flush();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<String> ParseImages(String url){
        List<String> links = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements el = doc.select("img");
            links = el.eachAttr("abs:src");
        } catch (Exception e){
            e.printStackTrace();
        }
        return links;
    }

    /*public static boolean downloadImg(String url) {
        try {
            FileUtils.copyURLToFile(new URL(url), new File("images/" + url.substring(url.lastIndexOf("/"))));
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }*/

    /*public static boolean downloadImg(String url){
        try(InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in,Paths.get("images/" + url.substring(url.lastIndexOf("/"))));
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }*/
}
