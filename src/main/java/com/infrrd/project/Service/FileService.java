package com.infrrd.project.Service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileService {
    public void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
    public String deleteFile(String fileName){
        File file = new File("sourcePath"+fileName);
        if (file.delete()){
            return "deleted successfully";
        }
        else{
            return "delete failed";
        }
    }
}
