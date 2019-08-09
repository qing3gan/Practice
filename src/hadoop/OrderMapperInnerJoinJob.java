package hadoop;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Agony on 2019/7/29
 * <p>
 * t_order join t_product(small)
 */
public class OrderMapperInnerJoinJob {

    static class OrderJoinMapper extends Mapper<LongWritable, Text, Text, OrderProductBean> {

        Text keyOut = new Text();
        OrderProductBean valueOut = new OrderProductBean();
        Map<String, OrderProductBean> productBeanMap = new HashMap<>();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Path[] localCacheFiles = context.getLocalCacheFiles();
            System.out.println(localCacheFiles[0].toString());
            FileReader fin = new FileReader(localCacheFiles[0].toString());
            BufferedReader bin = new BufferedReader(fin);
            String _line;
            while (null != (_line = bin.readLine())) {
                String[] line = _line.split("\t");
                OrderProductBean productBean = new OrderProductBean();
                productBean.setPid(line[0]);
                productBean.setName(line[1]);
                productBean.setCategoryId(line[2]);
                productBean.setPrice(new BigDecimal(line[3]));
                productBean.setOid("");
                productBean.setDate("");
                productBean.setAmount(0);
                productBeanMap.put(productBean.getPid(), productBean);
            }
            IOUtils.closeQuietly(bin);
            IOUtils.closeQuietly(fin);
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            System.out.println("map start");

            String[] line = value.toString().split("\t");

            valueOut.setOid(line[0]);
            valueOut.setDate(line[1]);
            valueOut.setPid(line[2]);
            valueOut.setAmount(Long.valueOf(line[3]));
            valueOut.setName("");
            valueOut.setCategoryId("");
            valueOut.setPrice(BigDecimal.ZERO);

            System.out.println("map object " + valueOut);

            OrderProductBean productBean = productBeanMap.get(valueOut.getPid());
            valueOut.setName(productBean.getName());
            valueOut.setCategoryId(productBean.getCategoryId());
            valueOut.setPrice(productBean.getPrice());

            keyOut.set(valueOut.getPid());
            context.write(keyOut, valueOut);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJar("/root/OrderMapperJoin.jar");
        job.setJarByClass(OrderMapperInnerJoinJob.class);
        job.setMapperClass(OrderJoinMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(OrderProductBean.class);
        FileInputFormat.setInputPaths(job, "hdfs://hadoop01:9000/order_mapper_join/input/");
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop01:9000/order_mapper_join/output"));

        job.setNumReduceTasks(0);
        job.addCacheFile(new URI("hdfs://hadoop01:9000/file/t_product.dat"));

        boolean exit = job.waitForCompletion(true);
        System.exit(exit ? 0 : 1);
    }
}