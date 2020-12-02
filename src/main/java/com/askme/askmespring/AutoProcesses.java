package com.askme.askmespring;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AutoProcesses extends Thread{

    public void deleteTmpImage() {

        File tmpDir = new File("tmp");
        File[] fileList = tmpDir.listFiles();
        List<File> pastDir = new ArrayList<File>();

        // Parameters for datetime and timestamp for the loading file
        SimpleDateFormat fileDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sectionDate = new SimpleDateFormat("HH");
        long timestamp = System.currentTimeMillis();
        Date datetime = new Date(timestamp);
        int currentHour = Integer.parseInt(sectionDate.format(datetime));
        String currentDate = fileDate.format(datetime);

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    pastDir.add(file);
                }
            }
        }
    }
}
