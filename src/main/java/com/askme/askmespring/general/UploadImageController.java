package com.askme.askmespring.general;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UploadImageController {
    private static final Logger log = Logger.getLogger(UploadImageController.class.getName());

    /**
     * default location for the tmp uploaded images
     */
    private static final String tmpImageLocation = "UploadImageTmp";

    /**
     * Saving the uploaded image as a tmp file in the server.
     * @param imageInfo - the detail information about the image from frontend client
     */
    @RequestMapping(value = "/upload_picture",method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> uploadImage(@RequestBody JSONObject imageInfo){

        // Parameters for datetime and timestamp for the loading file
        SimpleDateFormat fileDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sectionDate = new SimpleDateFormat("HH");
        long timestamp = System.currentTimeMillis();
        Date datetime = new Date(timestamp);
        int uploadHours = Integer.parseInt(sectionDate.format(datetime));
        String uploadDate = fileDate.format(datetime);

        // File path and filename extension
        String dir = uploadDate + "_";
        String fileExtend = "." + imageInfo.get("picture_type");

        // Set to different directory, according to the am/pm time sections
        if (uploadHours >= 12){
            dir += "pmf";
        } else {
            dir += "amf";
        }

        // Ensuring the parent directory for tmp upload image exists
        File location = new File(tmpImageLocation);
        if (!location.exists()){
            if (location.mkdir()) {
                System.out.println("Creating the directory for uploading images: " + location);
                log.info("Directory " + location + " has been created successfully");
            } else {
                System.out.println("Create directory " + location + " failed. Please check the current selection");
                log.severe("Create directory " + location + " failed. Please check the system setting manually");
                log.severe("Directory creation failed, please check the server account permission and " +
                        "the directory path have been set correctly");
                return new ResponseEntity<HttpStatus>(HttpStatus.SERVICE_UNAVAILABLE);
            }
        }

        // Ensuring directory of tmp file storing exists or create a new one
        File directory = new File(tmpImageLocation + "/" + dir);
        if (!directory.exists()){
            if (directory.mkdir()) {
                System.out.println("Creating new temporary directory for images: " + dir);
                log.info("Directory " + dir + " has been created successfully");
            } else {
                dir = "tmp";
                System.out.println("Create directory " + dir + " failed. Please check the current selection");
                log.severe("Create directory " + dir + " failed. Please check the system setting manually");
                log.severe("Directory creation failed, please check the server account permission and " +
                        "the directory path have been set correctly");
            }
        }

        // Writing file contents to the assigned location
        log.info("Loading uploading file");
        String tmpImage = tmpImageLocation +"/" + dir + "/" + imageInfo.get("user_id") + "_" + timestamp + fileExtend;
        try {
            OutputStream out = new FileOutputStream(tmpImage);
            log.info("Writing image file to the temporary directory");
            out.write(Base64.decodeBase64((String) imageInfo.get("picture_content")));
            out.close();
            log.info("File " + tmpImage + " has been written successfully");
            log.info("File " + tmpImage + " upload successful");
            System.out.println(" File upload successful");
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            System.out.println("Could not create file: " + tmpImage);
            log.severe("Create file " + tmpImage + " failed. Please check the system setting manually");
            log.severe("Create file failed, please check the permission and directory path have been set correctly");
            return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            System.out.println("Error writing file: " + tmpImage);
            log.severe("Fail to write file: " + tmpImage);
            log.severe("Write file failed, please check the permission and directory path have been set correctly");
            return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deleting tmp images and directories from server.
     * @param file - the file or directory required to be deleted
     */
    public void deleteTmpImage(File file){

        // Deleting all files and directories under the parent directory
        if(file.isDirectory()){
            log.info("Deleting children files and directories under parent directory: " + file.getName());
            File[] childrenFiles = file.listFiles();
            if (childrenFiles != null) {
                for (File childFile : childrenFiles) {
                    log.info("Deleting file or directory " + childFile.getName());
                    deleteTmpImage(childFile);
                }
            }
        }

        // Delete the directory or file
        if (!file.delete()){
            log.severe("Deleting file " + file.getName() + " failed.");
            log.severe("Deleting file failed, please check the permission and file path are correct");
        } else {
            log.info("File or directory " + file.getName() + " has been deleted");
        }
    }

}
