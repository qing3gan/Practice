package bigdata.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by Agony on 2019/7/19
 */
public class FlowSortJob {

    static class FlowSortMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            FlowBean flowBean = FlowBean.parse(value.toString());
            context.write(flowBean, new Text(flowBean.getPhone()));
        }
    }

    static class FlowSortReduce extends Reducer<FlowBean, Text, Text, FlowBean> {
        @Override
        protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            context.write(values.iterator().next(), key);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJar("/root/FlowCount.jar");
        job.setJarByClass(FlowSortJob.class);
        job.setMapperClass(FlowSortMapper.class);
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);
        conf.setBoolean(Job.MAP_OUTPUT_COMPRESS, true);
        conf.setClass(Job.MAP_OUTPUT_COMPRESS_CODEC, DefaultCodec.class, CompressionCodec.class);
        FileInputFormat.setInputPaths(job, "hdfs://hadoop01:9000/flowcount/input/flow.log");

        job.setReducerClass(FlowSortReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);
        FileOutputFormat.setCompressOutput(job, true);
        FileOutputFormat.setOutputCompressorClass(job, (Class<? extends CompressionCodec>) Class.forName("org.apache.hadoop.io.compress.DefaultCodec"));
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop01:9000/flowcount/output"));

        boolean exit = job.waitForCompletion(true);
        System.exit(exit ? 0 : 1);
    }
}
