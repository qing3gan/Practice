package hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Agony on 2019/7/19
 */
public class FlowCountJob {

    static class FlowBean implements WritableComparable<FlowBean> {

        long upflow;
        long downflow;
        long sumflow;

        public FlowBean() {
        }

        public FlowBean(long upflow, long downflow) {
            this.upflow = upflow;
            this.downflow = downflow;
            this.sumflow = upflow + downflow;
        }

        @Override
        public int compareTo(FlowBean o) {
            return this.sumflow > o.sumflow ? -1 : 1;
        }

        @Override
        public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeLong(upflow);
            dataOutput.writeLong(downflow);
            dataOutput.writeLong(sumflow);
        }

        @Override
        public void readFields(DataInput dataInput) throws IOException {
            upflow = dataInput.readLong();
            downflow = dataInput.readLong();
            sumflow = dataInput.readLong();
        }

        @Override
        public String toString() {
            return upflow + "\t" + downflow + "\t" + sumflow;
        }
    }

    static class FlowCountMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split("\t");
            String phone = line[1];
            long upflow = Long.valueOf(line[7]);
            long downflow = Long.valueOf(line[8]);
            FlowBean flowBean = new FlowBean(upflow, downflow);
            context.write(flowBean, new Text(phone));
        }
    }

    static class FlowCountReduce extends Reducer<FlowBean, Text, Text, FlowBean> {
        @Override
        protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            context.write(values.iterator().next(), key);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJar("/root/FlowCount.jar");
        job.setJarByClass(FlowCountJob.class);
        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReduce.class);
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);
        FileInputFormat.setInputPaths(job, "hdfs://hadoop01:9000/flowcount/input/flow.log");
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop01:9000/flowcount/output"));
        boolean exit = job.waitForCompletion(true);
        System.exit(exit ? 0 : 1);
    }
}
