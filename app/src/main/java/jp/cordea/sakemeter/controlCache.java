package jp.cordea.sakemeter;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by CORDEA on 2015/03/29.
 */
public class controlCache {
    public static HashMap<String, String> readCache(boolean flag, File cacheDir, String fileName) {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        try {
            //File cacheDir = getCacheDir();
            File file               = new File(cacheDir.getAbsolutePath(), fileName);
            FileInputStream fis     = new FileInputStream(file);
            StringBuilder builder   = new StringBuilder();

            int content;
            try {
                while ((content = fis.read()) != -1) builder.append((char) content);

                String[] strArray = builder.toString().split(":");
                if (flag) {
                    hashMap.put("acceptable", strArray[strArray.length - 1]);
                    return hashMap;
                }
                for (String str : strArray) {
                    if (str.length() != 0) {
                        String[] kvArray = str.split(",");
                        if      (kvArray.length == 2) hashMap.put(kvArray[0], kvArray[1]);
                    }
                }
            } catch (IOException e) {
                Log.i("readCache", e.toString());
            }
        } catch (FileNotFoundException e) {
            SakeLimitActivity sla = new SakeLimitActivity();
            return sla.writeCache(true, cacheDir, fileName);
        }
        Log.i("readCache", "hashMap: " + hashMap.toString());

        return hashMap;
    }
}