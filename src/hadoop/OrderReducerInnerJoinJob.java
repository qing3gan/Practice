package hadoop;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agony on 2019/7/29
 * <p>
 * t_order join t_product
 */
public class OrderReducerInnerJoinJob {

    static class OrderJoinMapper extends Mapper<LongWritable, Text, Text, OrderProductBean> {

        Text keyOut = new Text();
        OrderProductBean valueOut = new OrderProductBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            System.out.println("map start");

            String[] line = value.toString().split("\t");
            String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();

            if (OrderProductBean.Origin.ORDER.getFileName().equals(fileName)) {
                valueOut.setOid(line[0]);
                valueOut.setDate(line[1]);
                valueOut.setPid(line[2]);
                valueOut.setAmount(Long.valueOf(line[3]));
                valueOut.setName("");
                valueOut.setCategoryId("");
                valueOut.setPrice(BigDecimal.ZERO);
            } else if (OrderProductBean.Origin.PRODUCT.getFileName().equals(fileName)) {
                valueOut.setPid(line[0]);
                valueOut.setName(line[1]);
                valueOut.setCategoryId(line[2]);
                valueOut.setPrice(new BigDecimal(line[3]));
                valueOut.setOid("");
                valueOut.setDate("");
                valueOut.setAmount(0);
            }
            valueOut.setFileName(fileName);

            System.out.println("map object " + valueOut);
            keyOut.set(valueOut.getPid());
            context.write(keyOut, valueOut);
        }
    }

    static class OrderJoinReducer extends Reducer<Text, OrderProductBean, Text, OrderProductBean> {
        @Override
        protected void reduce(Text key, Iterable<OrderProductBean> values, Context context) throws IOException, InterruptedException {
            System.out.println("reduce start");
            OrderProductBean productBean = new OrderProductBean();
            List<OrderProductBean> orderBeans = new ArrayList<>();
            for (OrderProductBean orderProductBean : values) {
                System.out.println("reduce object " + orderProductBean);
                if (OrderProductBean.Origin.PRODUCT.getFileName().equals(orderProductBean.getFileName())) {
                    try {
                        BeanUtils.copyProperties(productBean, orderProductBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (OrderProductBean.Origin.ORDER.getFileName().equals(orderProductBean.getFileName())) {
                    OrderProductBean orderBean = new OrderProductBean();
                    try {
                        BeanUtils.copyProperties(orderBean, orderProductBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    orderBeans.add(orderBean);
                }
            }
            System.out.println(orderBeans);
            for (OrderProductBean orderBean : orderBeans) {
                orderBean.setName(productBean.getName());
                orderBean.setCategoryId(productBean.getCategoryId());
                orderBean.setPrice(productBean.getPrice());
                context.write(key, orderBean);
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJar("/root/OrderJoin.jar");
        job.setJarByClass(OrderReducerInnerJoinJob.class);
        job.setMapperClass(OrderJoinMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(OrderProductBean.class);
        FileInputFormat.setInputPaths(job, "hdfs://hadoop01:9000/orderjoin/input/");

        job.setReducerClass(OrderJoinReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(OrderProductBean.class);
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop01:9000/orderjoin/output"));

        boolean exit = job.waitForCompletion(true);
        System.exit(exit ? 0 : 1);
    }
}