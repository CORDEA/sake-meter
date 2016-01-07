package jp.cordea.sakemeter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yoshihiro Tanaka on 16/01/07.
 */
@Getter
@AllArgsConstructor
public class EditListItem {
    private String title;
    private String detail;

    @Setter
    private int limit;
}
