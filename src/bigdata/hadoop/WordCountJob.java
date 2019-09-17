package bigdata.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordCountJob {

    /**
     * invoke for each line
     */
    public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        public WordCountMapper() {
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            for (String word : line.split(" ")) {
                context.write(new Text(word), new IntWritable(1));
            }
        }
    }

    /**
     * invoke for key value group
     */
    public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        public WordCountReducer() {
        }

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable value : values) {
                count += value.get();
            }
            context.write(key, new IntWritable(count));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJar("/root/wordcount.jar");
        job.setJarByClass(WordCountJob.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.setInputPaths(job, "hdfs://hadoop01:9000/wordcount/input/somewords");
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop01:9000/wordcount/output"));
        boolean exit = job.waitForCompletion(true);
        System.exit(exit ? 0 : 1);
    }
}
