package jp.cordea.sakemeter;

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

public class general {
    public static String[]  sakeArray = {"Beer", "Sake", "Wine", "Cocktail", "Liqueur", "Spirits", "Other"};

    public static String    limitFile = "sakeMeter.limit.csv";
    public static String    meterFile = "sakeMeter.meter.csv";

    public static HashMap<String, Integer> sakeMap() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        /**
         * Beer     : 5.5   %
         * Sake     : 15    %
         * Wine     : 10    %
         * Cocktail : 15    %
         * Liqueur  : 20    %
         * Spirits  : 40    %
         * Other    : 30    %
         */
        int[] alcohol = {55, 150, 100, 150, 200, 400, 300};
        for (int i = 0; i < sakeArray.length; i++) hashMap.put(sakeArray[i], alcohol[i]);
        return hashMap;
    }

    public static HashMap<String, Integer> sakeVolMap() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        /**
         * Beer     : 350   ml
         * Sake     : 180   ml
         * Wine     : 125   ml (Full bottle: 750 ml)
         * Cocktail : 150   ml
         * Liqueur  : 50    ml
         * Spirits  : 45    ml
         * Other    : 30    ml
         */
        int[] alcohol = {350, 180, 125, 150, 50, 45, 30};
        for (int i = 0; i < sakeArray.length; i++) hashMap.put(sakeArray[i], alcohol[i]);
        return hashMap;
    }
}
