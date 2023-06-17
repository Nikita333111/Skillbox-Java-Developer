package org.example;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class Main {

    public static JSONObject StationsAndLines = new JSONObject();

    public static HashMap <String,JSONArray> map = new HashMap<>();

    public static void main(String[] args) {
        try {
            //парсим страницу, получаем div таблицу всех линий и станций
            Document doc = Jsoup.connect("https://www.moscowmap.ru/metro.html#lines").maxBodySize(0).get();
            Elements el = doc.select("div#metrodata");
            Elements lines = el.select("span.js-metro-line");
            // записываем названия и номера линий в JSON
            writeJsonMetroLines(lines);


            Elements stations = el.select("div.js-metro-stations");
            // номера линий и их станции в JSON
            writeJsonMetroStations(stations);

        } catch (Exception ex){
            ex.printStackTrace();
        }
        // читаем JSON и выводим названия линий и колличество станции на них
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());
            JSONArray lineArr = (JSONArray) jsonData.get("lines");
            JSONObject station = (JSONObject) jsonData.get("stations");
            lineArr.forEach(line -> {
                JSONObject lineJsonObject = (JSONObject) line;
                int lineStationsNumber = getStationsNumOnTheLine(lineJsonObject.get("number"),station);
                System.out.println("Line " + lineJsonObject.get("name") + " consist of " + lineStationsNumber + " stations");
            });

        } catch (ParseException ex){
            ex.printStackTrace();
        }
    }

    public static int getStationsNumOnTheLine(Object lineNum, JSONObject stations){
        JSONArray arr =  (JSONArray) stations.get(lineNum);
        return arr.size();
    }

    public static String getJsonFile(){
        StringBuilder builder = new StringBuilder();
        try {
            Stream<String> lines = Files.lines(Paths.get("data/metro.json"), Charset.forName("Cp1251"));
            lines.forEach(line -> builder.append(line));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

    public static void writeJsonMetroStations(Elements stations){
        for (Element stationsLine: stations) {
            JSONArray arrStations = new JSONArray();

            Elements lineStationsName = stationsLine.select("span.name");
            lineStationsName.forEach(element -> arrStations.add(element.text()));
            map.put(stationsLine.attr("data-line"),arrStations);
        }
        StationsAndLines.put("stations",map);
        try(FileWriter writer = new FileWriter("data/metro.json")) {
            writer.write(StationsAndLines.toJSONString());
            writer.flush();
            writer.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    public static void writeJsonMetroLines(Elements el){
        JSONArray arrLines = new JSONArray();
        for (Element l: el) {
            JSONObject line = new JSONObject();
            line.put("number",l.attr("data-line"));
            line.put("name",l.text());
            arrLines.add(line);
        }
        StationsAndLines.put("lines",arrLines);
        try(FileWriter writer = new FileWriter("data/metro.json")) {
            writer.write(StationsAndLines.toJSONString());
            writer.flush();
            writer.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}