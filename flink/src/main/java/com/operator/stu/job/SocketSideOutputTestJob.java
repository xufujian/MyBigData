package com.operator.stu.job;

import com.mybigdata.url.ResourceUrlUtils;
import com.operator.stu.utils.FlinkUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 今天风很大
 * @date:2021/7/29 22:24
 * @Description: 测试 侧流输出
 */
public class SocketSideOutputTestJob {
    private static Logger logger = LoggerFactory.getLogger(SocketSideOutputTestJob.class);

    public static void main(String[] args) throws Exception {
        ParameterTool parameters = ParameterTool
                .fromPropertiesFile(ResourceUrlUtils.getCurrentModuleConfigUrl(KafkaToPrintJob.class, "config.properties"));

        DataStreamSource<String> dataStreamSource = FlinkUtils.createSocketStream(parameters, "192.168.80.5", 8888);

        //Tuple2<String, String>
        //Tuple2<String, String>
        OutputTag<Tuple2<String, String>> masterTag = new OutputTag<Tuple2<String, String>>("master-tag"){};
        OutputTag<Tuple2<String, String>> twoTag = new OutputTag<Tuple2<String, String>>("two-tag"){};

        //主流
        SingleOutputStreamOperator<Tuple2<String, String>> masterDataStream =
                dataStreamSource.process(new ProcessFunction<String, Tuple2<String, String>>() {
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                    }
                    @Override
                    public void processElement(String input, Context ctx, Collector<Tuple2<String, String>> out) throws Exception {
                        String[] fields = input.split(",");
                        String type = fields[0];
                        String name = fields[1];
                        Tuple2<String, String> tp = Tuple2.of(type, name);
                        //2.对数据打标签
                        //将数据打上标签
                        if (type.equals("master")) {
                            //输出数据，将数据和标签关联
                            ctx.output(masterTag, tp);  //ctx.output  输出侧流的
                        } else if (type.equals("two")) {
                            ctx.output(twoTag, tp);
                        }
//                        //输出主流的数据
//                        out.collect(tp);
                    }
                });

        //输出的测流只能通过getSideOutput
        DataStream<Tuple2<String, String>> masterStream = masterDataStream.getSideOutput(masterTag);
        DataStream<Tuple2<String, String>> twoDataStream = masterDataStream.getSideOutput(twoTag);

        //分别处理各种类型的数据。
        twoDataStream.print();

        masterStream.print();

        FlinkUtils.getEnv().execute(SocketSideOutputTestJob.class.getName());
    }
}
