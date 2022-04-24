package com.sample;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class FileReaderWorker implements Runnable{
    private final File file;
    Map<String,BigInteger> revenueTally;

    public FileReaderWorker(File file, Map<String,BigInteger> revenueTally){
        this.file = file;
        this.revenueTally = revenueTally;
    }
    private void readFileData(){
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            Map<String,BigInteger> inputFileTallyData = new HashMap<>();
            String st;
            while ((st = br.readLine()) != null){
                String[] inputFileLine = st.split(",");
                String advertiser = inputFileLine[0];
                BigInteger advertiserRevenue = new BigInteger(inputFileLine[1]);
                if(inputFileTallyData.containsKey(inputFileLine[0]))advertiserRevenue = advertiserRevenue.add(inputFileTallyData.get(advertiser));
                inputFileTallyData.put(advertiser , advertiserRevenue);
            }
            synchronized (this.revenueTally){
                for(String advertiser :inputFileTallyData.keySet()){
                    BigInteger totalAdvertiserRevenue = inputFileTallyData.get(advertiser);
                    if(this.revenueTally.containsKey(advertiser))totalAdvertiserRevenue = totalAdvertiserRevenue.add(this.revenueTally.get(advertiser));
                    this.revenueTally.put(advertiser,totalAdvertiserRevenue);
                }

            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        readFileData();
    }
}
