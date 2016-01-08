package jp.cordea.sakemeter;

import java.util.HashMap;
import java.util.List;

import jp.cordea.sakemeter.model.SakeInfo;
import rx.Observable;

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

public class SakeInfoUtils {

    public static HashMap<Sake, SakeInfo> getSakeInfo() {
        float[] alcohol = {5.5f, 15.0f, 10.0f, 15.0f, 20.0f, 40.0f, 30.0f};
        int[] volume = {350, 180, 125, 150, 50, 45, 30};

        HashMap<Sake, SakeInfo> sakeInfo = new HashMap<>();
        int i = 0;
        for (Sake sake : Sake.values()) {
            sakeInfo.put(sake, new SakeInfo(alcohol[i], volume[i]));
            i++;
        }
        return sakeInfo;
    }

    public static List<String> getStringSakeList() {
        return Observable
                .from(Sake.values())
                .map(Enum::name)
                .toList()
                .toBlocking()
                .first();
    }
}
