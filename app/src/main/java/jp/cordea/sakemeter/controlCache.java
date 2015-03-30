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
            return writeCache(true, cacheDir, fileName);
        }
        Log.i("readCache", "hashMap: " + hashMap.toString());

        return hashMap;
    }

    public static HashMap<String, String> writeCache(boolean flag, File cacheDir, String fileName) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String acceptable = "0";

        int index = -1;
        if (fileName.contains("limit")) index = 0;

        if (flag) for (String str : general.sakeArray) hashMap.put(str, Integer.toString(index));
        else {
            SakeLimitActivity sla = new SakeLimitActivity();
            hashMap = sla.readRowItems();
            acceptable = sla.calcTolerance(hashMap).toString();
        }

        try {
            File file = new File(cacheDir.getAbsolutePath(), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            try {
                if (!file.exists()) file.createNewFile();
                for (Map.Entry<String, String> hm : hashMap.entrySet()) {
                    String content = hm.getKey() + "," + hm.getValue().toString() + ":";
                    byte[] contentInBytes = content.getBytes();
                    fos.write(contentInBytes);
                    fos.flush();
                    Log.i("writeCache", content);
                }
                fos.write(acceptable.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {}
        } catch (FileNotFoundException e) {}
        return hashMap;
    }
}
