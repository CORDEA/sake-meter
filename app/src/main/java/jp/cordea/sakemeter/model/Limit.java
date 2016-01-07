package jp.cordea.sakemeter.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yoshihiro Tanaka on 16/01/07.
 */
public class Limit extends RealmObject {

    @PrimaryKey
    private String sake;
    private int limit;

    public String getSake() {
        return sake;
    }

    public void setSake(String sake) {
        this.sake = sake;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
