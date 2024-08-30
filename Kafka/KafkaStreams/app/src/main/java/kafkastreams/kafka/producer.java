package kafkastreams.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.Serdes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class producer {
    public static void main(String[] args) {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "b-2-public.dp.27tn16.c3.kafka.ap-northeast-2.amazonaws.com:9198,b-1-public.dp.27tn16.c3.kafka.ap-northeast-2.amazonaws.com:9198");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(SaslConfigs.SASL_MECHANISM, "AWS_MSK_IAM");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, "software.amazon.msk.auth.iam.IAMLoginModule required;");
        props.put(SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS, "software.amazon.msk.auth.iam.IAMClientCallbackHandler");
        
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        
        String data_Generator_Path = "/home/kkh/workspace/DE31-final-team1/TEST_FUNC/data_generator.py";
        ProcessBuilder processBuilder = new ProcessBuilder("python3", data_Generator_Path);

        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            ProducerRecord<String, String> record = new ProducerRecord<String,String>("receive_test_topic", reader.readLine());
            producer.send(record);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }    
}
