package com.enableets.edu.pakage.etm.core;

import java.io.File;

public class FileUtil {

    public static void clearFiles(String workspaceRootPath) {
        File file = new File(workspaceRootPath);
        deleteFile(file);
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
        }
        file.delete();
    }


}
