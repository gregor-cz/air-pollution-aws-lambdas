import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.HashMap;
import java.util.Map;

public class TestSendSms {


    public static void main(String[] args) {


        SnsClient snsClient = SnsClient.builder().build();
//        String message = "My SMS message";
//        String phoneNumber = "+1XXX5550100";
//        Map<String, MessageAttributeValue> smsAttributes =
//                new HashMap<String, MessageAttributeValue>();
//        //<set SMS attributes>
        sendSMSMessage(snsClient);
    }

    public static void sendSMSMessage(SnsClient snsClient) {
        PublishResponse publish = snsClient.publish(PublishRequest.builder().
                topicArn("arn:aws:sns:us-west-2:464446151961:testsms")
                .subject("Testsms")
                .message("test sms content")
                .build());
        System.out.println(publish.messageId()); // Prints the message ID.
    }

}
