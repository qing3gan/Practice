package bigdata.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by Agony on 2019/7/23
 */
public class FlowPartitionJob {

    static class FlowPartitionMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            FlowBean flowBean = FlowBean.parse(value.toString());
            context.write(new Text(flowBean.getPhone()), flowBean);
        }
    }

    static class FlowPartition extends Partitioner<Text, FlowBean> {
        @Override
        public int getPartition(Text text, FlowBean flowBean, int i) {
            String phonePrefix = text.toString().substring(0, 3);
            switch (phonePrefix) {
                case "135":
                    return 0;
                case "136":
                    return 1;
                case "137":
                    return 2;
                case "138":
                    return 3;
                case "139":
                    return 4;
            }
            return 5;
        }
    }

    static class FlowPartitionReduce extends Reducer<Text, FlowBean, Text, FlowBean> {
        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
            FlowBean flowBean = new FlowBean();
            for (FlowBean bean : values) {
                flowBean.setUpFlow(flowBean.getUpFlow() + bean.getUpFlow());
                flowBean.setDownFlow(flowBean.getDownFlow() + bean.getDownFlow());
                flowBean.setSumFlow(flowBean.getSumFlow() + bean.getSumFlow());
            }
            context.write(key, flowBean);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJar("/root/FlowCount.jar");
        job.setJarByClass(FlowPartitionJob.class);
        job.setMapperClass(FlowPartitionMapper.class);
        job.setPartitionerClass(FlowPartition.class);
        job.setReducerClass(FlowPartitionReduce.class);
        job.setNumReduceTasks(6);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);
        FileInputFormat.setInputPaths(job, "hdfs://hadoop01:9000/flowcount/input/flow.log");
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop01:9000/flowcount/output"));
        boolean exit = job.waitForCompletion(true);
        System.exit(exit ? 0 : 1);
    }
}
