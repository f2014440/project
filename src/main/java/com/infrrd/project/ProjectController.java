package com.infrrd.project;

import com.infrrd.project.Service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Logger;


@RestController
public class ProjectController {
    @Autowired
    FileService fileService;
    private  static  final Logger log = Logger.getLogger("");

    @RequestMapping(value = "infrrd/getFile/{fileName}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE},  method = RequestMethod.POST)
    public void getFile(@RequestParam(value="fileName", required=false) String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {

        byte[] reportBytes = null;
        File result = new File("/home/arpit/Documents/PCAP/dummyPath/" + fileName);
        log.info("got the file with filename:"+fileName);

        if (result.exists()) {
            InputStream inputStream = new FileInputStream("/User/Documents/dummyPath/" + fileName);
            String type = result.toURL().openConnection().guessContentTypeFromName(fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Type", type);

            reportBytes = new byte[1000];
            OutputStream os = response.getOutputStream();
            int read = 0;
            while ((read = inputStream.read(reportBytes)) != -1) {
                os.write(reportBytes, 0, read);
            }
            os.flush();
            os.close();
        }
    }
    @ResponseBody
    @RequestMapping(value = "infrrd/copyFile/{fileName}",  method = RequestMethod.POST)
    public ResponseEntity copyFile(@RequestParam(value="fileName", required=false) String fileName){
        File source = new File("/home/arpit/Documents/PCAP/dummyPath/" + fileName);
        File dest = new File("/home/arpit/Documents/PCAP/dummyDest/" + fileName);
        try {
            fileService.copyFileUsingStream(source, dest);
        }catch (IOException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "infrrd/deleteFile/{fileName}",  method = RequestMethod.GET)
    public String  deleteFile(@RequestParam(value="fileName", required=false) String fileName){
        return fileService.deleteFile(fileName);
    }
    @RequestMapping(path = "/health", method = RequestMethod.GET)
    public String getHealth() {
        return "Healthy!!!";
    }

}

