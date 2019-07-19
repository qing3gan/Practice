package test;

import org.apache.hadoop.io.Writable;

import java.io.*;

/**
 * Created by Agony on 2019/7/18
 */
public class TestSeri {
    static class ItemBeanSer implements Serializable {
        long _long;
        float _float;

        public ItemBeanSer(long _long, float _float) {
            this._long = _long;
            this._float = _float;
        }

        private void writeObject(java.io.ObjectOutputStream out)
                throws IOException {
            out.writeLong(this._long);
            out.writeFloat(this._float);
        }
    }

    static class ItemBean implements Writable {
        long _long;
        float _float;

        public ItemBean(long _long, float _float) {
            this._long = _long;
            this._float = _float;
        }

        @Override
        public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeLong(this._long);
            dataOutput.writeFloat(this._float);
        }

        @Override
        public void readFields(DataInput dataInput) throws IOException {

        }
    }

    public static void main(String[] args) throws Exception {
        //定义两个ByteArrayOutputStream，用来接收不同序列化机制的序列化结果
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        ByteArrayOutputStream ba2 = new ByteArrayOutputStream();

        //定义两个DataOutputStream，用于将普通对象进行jdk标准序列化
        DataOutputStream dout = new DataOutputStream(ba);
        DataOutputStream dout2 = new DataOutputStream(ba2);
        ObjectOutputStream obout = new ObjectOutputStream(dout2);
        //定义两个bean，作为序列化的源对象
        ItemBean itemBean = new ItemBean(1000L, 89.9f);
        ItemBeanSer itemBeanSer = new ItemBeanSer(1000L, 89.9f);

        //用于比较String类型和Text类型的序列化差别
//        Text atext = new Text("a");
//        atext.write(dout);
        itemBean.write(dout);

        byte[] byteArray = ba.toByteArray();

        //比较序列化结果
        System.out.println(byteArray.length);
        for (byte b : byteArray) {

            System.out.print(b);
            System.out.print(":");
        }

        System.out.println("\n-----------------------");

//        String astr = "a";
//        dout2.writeUTF(astr);
        obout.writeObject(itemBeanSer);

        byte[] byteArray2 = ba2.toByteArray();
        System.out.println(byteArray2.length);
        for (byte b : byteArray2) {
            System.out.print(b);
            System.out.print(":");
        }
    }

}
