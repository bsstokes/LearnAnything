package com.bsstokes.learnanything.dev_tools;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class CopyFile {

    public static void backupDatabaseFile(String databasePath) throws IOException {
        File currentDB = new File(databasePath);
        File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), currentDB.getName());
        copy(currentDB, backupDB);
    }

    public static void copy(File fromFile, File toFile) throws IOException {
        if (fromFile.exists()) {
            FileChannel src = new FileInputStream(fromFile).getChannel();
            FileChannel dst = new FileOutputStream(toFile).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        }
    }
}
