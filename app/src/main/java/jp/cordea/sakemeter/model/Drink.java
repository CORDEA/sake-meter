package jp.cordea.sakemeter.model;

import org.joda.time.LocalDate;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yoshihiro Tanaka on 16/01/07.
 */
public class Drink extends RealmObject {

    private Date date;
    private String sake;
    private int vot;
    private int limit;

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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
