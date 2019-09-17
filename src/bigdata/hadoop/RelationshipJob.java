package bigdata.hadoop;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agony on 2019/8/14
 * <p>
 * common friend between user
 */
public class RelationshipJob {

    /**
     * friend as key
     */
    static class RelationshipMapper1 extends Mapper<LongWritable, Text, Text, Text> {

        Text keyOut = new Text();
        Text valueOut = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            /*
            A:B,C,D,F,E,O
            B:A,C,E,K
            ...
            */
            String[] relationship = value.toString().split(":");
            String user = relationship[0];
            String[] friends = relationship[1].split(",");
            //<friend1,user>,<friend2,user>...
            valueOut.set(user);
            for (String friend : friends) {
                keyOut.set(friend);
                context.write(keyOut, valueOut);
            }
        }
    }

    /**
     * common friend
     */
    static class RelationshipReduce1 extends Reducer<Text, Text, Text, Text> {

        Text keyOut = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //<friend,user1>,<friend,user2>...
            List<String> users = new ArrayList<>();
            for (Text value : values) {
                users.add(value.toString());
            }
            //user1-user2   friend...
            for (int i = 0; i < users.size(); i++) {
                for (int j = i + 1; j < users.size(); j++) {
                    keyOut.set(users.get(i) + "-" + users.get(j));
                    context.write(keyOut, key);
                }
            }
        }
    }

    /**
     * user as key
     */
    static class RelationshipMapper2 extends Mapper<LongWritable, Text, Text, Text> {

        Text keyOut = new Text();
        Text valueOut = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //user1-user2   friend
            String[] relationship = value.toString().split("\t");
            keyOut.set(relationship[0]);
            valueOut.set(relationship[1]);
            context.write(keyOut, valueOut);
        }
    }

    /**
     * common friend between user
     */
    static class RelationshipReducer2 extends Reducer<Text, Text, Text, Text> {

        Text valueOut = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //<user1-user2,friend>
            List<String> friends = new ArrayList<>();
            for (Text value : values) {
                friends.add(value.toString());
            }
            //user1-user2   friend1,friend2
            valueOut.set(StringUtils.join(friends, ","));
            context.write(key, valueOut);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job1 = Job.getInstance(conf);

        job1.setJar("/root/Relationship.jar");
        job1.setJarByClass(RelationshipJob.class);
        job1.setMapperClass(RelationshipMapper1.class);
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(Text.class);
        FileInputFormat.setInputPaths(job1, "hdfs://hadoop01:9000/relationship/input/");

        job1.setReducerClass(RelationshipReduce1.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);
        FileOutputFormat.setOutputPath(job1, new Path("hdfs://hadoop01:9000/relationship/temp"));

        boolean exit = job1.waitForCompletion(true);

        if (exit) {
            Job job2 = Job.getInstance(conf);

            job2.setJar("/root/Relationship.jar");
            job2.setJarByClass(RelationshipJob.class);
            job2.setMapperClass(RelationshipMapper2.class);
            job2.setMapOutputKeyClass(Text.class);
            job2.setMapOutputValueClass(Text.class);
            FileInputFormat.setInputPaths(job2, "hdfs://hadoop01:9000/relationship/temp/part-r-00000");

            job2.setReducerClass(RelationshipReducer2.class);
            job2.setOutputKeyClass(Text.class);
            job2.setOutputValueClass(Text.class);
            FileOutputFormat.setOutputPath(job2, new Path("hdfs://hadoop01:9000/relationship/output"));

            exit = job2.waitForCompletion(true);
        }

        System.exit(exit ? 0 : 1);
    }

}
