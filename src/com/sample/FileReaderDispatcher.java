package com.sample;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileReaderDispatcher {
    //method to take the name of a folder and get all the files in the folder
    public File[] getAllFilesInDirectory(String directoryPath){
        File folder = new File(directoryPath);
        File[] fileList = folder.listFiles();
        if(fileList == null){
            System.out.println("The directory path given is not valid , please give a correct directory path !!!!");
            return null;
        }
        return fileList;
    }
    public void dispatchFileToWorker(String directoryPath , int threadLimit){
        File[] fileList = getAllFilesInDirectory(directoryPath);
        //in case there are no files to be processed return from the array
        if(fileList == null)return ;
//        else initialize the the thread pool-> for this use case i have taken a limit sized thread pool and start assigning them
//        runanble instances
        ExecutorService pool = Executors.newFixedThreadPool(threadLimit);
        /*creating a map to store the results of multiple threads
        this object will be shared accross multiple threads to keep revenue data consistent
        */
        Map<String, BigInteger> revenueTally = new HashMap<>();
        for(int i = 0 ; i< fileList.length; i++){
            FileReaderWorker newFileReader = new FileReaderWorker(fileList[i],revenueTally);
            pool.execute(newFileReader);
            }
        pool.shutdown();
        try{
            boolean finished = pool.awaitTermination(24, TimeUnit.HOURS);
            if(!finished)throw new InterruptedException("The function took too long to read the files !!!");
            for(String advertiser : revenueTally.keySet())System.out.println(advertiser  +
                    " -> Revenue : " + revenueTally.get(advertiser));
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}
