package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("������� �������� ������ ����� -> (������) https://yandex.ru");
        String root = scanner.nextLine();

        System.out.println("������������ ���������..");
        String siteMap = new ForkJoinPool().invoke(new SiteMapGenerator(root,root));
        System.out.println("������������ ���������");

        writeFile(siteMap);
    }

    public static void writeFile(String map){
        System.out.println("���� ������������");
        File file = new File("writing/map.txt");
        try(PrintWriter writer = new PrintWriter(file)) {
            writer.write(map);
        } catch (FileNotFoundException ex){
            System.out.println("File not found " + ex.getMessage());
        }
        System.out.println("����� ����� ������� !");
    }
}