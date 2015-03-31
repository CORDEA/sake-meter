package jp.cordea.sakemeter;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Copyright [2015] [Yoshihiro Tanaka]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Yoshihiro Tanaka<contact@cordea.jp>
 * Date  : 2015/03/29
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