package jp.cordea.sakemeter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yoshihiro Tanaka on 16/01/07.
 */
@Getter
@Setter
@AllArgsConstructor
public class SakeInfo {
    private int alcohol;
    private int volume;
}
