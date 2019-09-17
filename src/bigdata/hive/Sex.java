package bigdata.hive;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * Created by Agony on 2019/8/28
 */
public class Sex extends UDF {

    public Text evaluate(final Text s) {
        return s == null ? null : s.toString().equals("男") ? new Text("male") : new Text("female");
    }

    public static void main(String[] args) {

    }
}
