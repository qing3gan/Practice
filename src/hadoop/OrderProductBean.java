package hadoop;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by Agony on 2019/7/29
 */
public class OrderProductBean implements Writable {

    public enum Origin {
        PRODUCT("t_product.dat"), ORDER("t_order.dat");

        private final String fileName;

        Origin(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    //order
    private String oid;
    private String date;
    private long amount;
    //product
    private String pid;
    private String name;
    private String categoryId;
    private BigDecimal price;
    //origin
    private String fileName;

    public OrderProductBean() {
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return oid + '\t' +
                date + '\t' +
                amount + '\t' +
                pid + '\t' +
                name + '\t' +
                categoryId + '\t' +
                price + '\t' +
                fileName;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        System.out.println("Serialize " + fileName);
        dataOutput.writeUTF(fileName);
        dataOutput.writeUTF(pid);
        dataOutput.writeUTF(name);
        dataOutput.writeUTF(categoryId);
        dataOutput.writeUTF(price.toString());
        dataOutput.writeUTF(oid);
        dataOutput.writeUTF(date);
        dataOutput.writeLong(amount);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        fileName = dataInput.readUTF();
        System.out.println("Deserialize " + fileName);
        pid = dataInput.readUTF();
        name = dataInput.readUTF();
        categoryId = dataInput.readUTF();
        price = new BigDecimal(dataInput.readUTF());
        oid = dataInput.readUTF();
        date = dataInput.readUTF();
        amount = dataInput.readLong();
    }
}
