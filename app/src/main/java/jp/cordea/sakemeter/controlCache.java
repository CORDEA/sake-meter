package jp.cordea.sakemeter;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CORDEA on 2015/03/29.
 */
public class controlCache {
    public static HashMap<String, String> readCache(File cacheDir) {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        try {
            //File cacheDir = getCacheDir();
            File file = new File(cacheDir.getAbsolutePath(), "sakeMeter.csv");
            FileInputStream fis = new FileInputStream(file);
            StringBuilder builder = new StringBuilder();

            int content;
            try {
                while ((content = fis.read()) != -1) {
                    builder.append((char) content);
                }

                String[] strArray = builder.toString().split(":");
                for (String str : strArray) {
                    Log.i("readCache", str);
                    if (str.length() != 0) {
                        String[] kvArray = str.split(",");
                        hashMap.put(kvArray[0], kvArray[1]);
                    }
                }
                Log.i("readCache", hashMap.toString());
            } catch (IOException e) {
                Log.i("readCache", e.toString());
            }
        } catch (FileNotFoundException e) {
            SakeLimitActivity sla = new SakeLimitActivity();
            return sla.writeCache(true);
        }

        Log.i("readCache", "hashMap: " + hashMap.toString());

        return hashMap;
    }
}
