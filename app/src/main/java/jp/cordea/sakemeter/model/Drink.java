package jp.cordea.sakemeter.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yoshihiro Tanaka on 16/01/07.
 */
public class Drink extends RealmObject {

    @PrimaryKey
    private String sake;
    private int vot;

    public String getSake() {
        return sake;
    }

    public void setSake(String sake) {
        this.sake = sake;
    }

    public int getVot() {
        return vot;
    }

    public void setVot(int vot) {
        this.vot = vot;
    }
}
