package util;

import java.io.Serializable;

/**
 * Created by Agony on 2018/7/18.
 */
public class Obj implements Serializable {

    private String attr;

    public Obj() {
    }

    public Obj(String attr) {
        this.attr = attr;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    @Override
    public String toString() {
        return "Obj{" +
                "attr='" + attr + '\'' +
                '}';
    }
}
