package org.wdd.app.android.seedoctor.utils;

import java.io.File;

/**
 * Created by richard on 1/20/17.
 */

public class FileUtils {

    public static long getFolderSize(File folder) {
        if (!folder.exists()) return 0;
        if (folder.isFile()) return folder.length();
        int size = 0;
        File[] childFiles = folder.listFiles();
        for (File file : childFiles) {
            if (file.isFile()) {
                size += file.length();
            } else {
                size += getFolderSize(file);
            }
        }
        return size;
    }
}
