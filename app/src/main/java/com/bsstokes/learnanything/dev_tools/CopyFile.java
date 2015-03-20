package com.bsstokes.learnanything.dev_tools;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import rx.Observable;
import rx.Subscriber;

public class CopyFile {

    public static Observable<String> copyDatabaseToSDCard(final String databasePath) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String backedUpFilePath = CopyFile.backupDatabaseFile(databasePath);

                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(backedUpFilePath);
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static String backupDatabaseFile(String databasePath) throws IOException {
        File currentDB = new File(databasePath);
        File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), currentDB.getName());
        copy(currentDB, backupDB);

        return backupDB.getAbsolutePath();
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
