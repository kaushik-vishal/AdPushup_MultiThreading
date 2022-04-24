package com.sample;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the max number of thread you want this application to use !!");
        Integer threadLimit = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the directory path for which the files need to be read !!!");
        String  directory = sc.nextLine();

        FileReaderDispatcher reader = new FileReaderDispatcher();
//        "D:\\AdpushUpDemo"
        reader.dispatchFileToWorker(directory,threadLimit);

    }
}
