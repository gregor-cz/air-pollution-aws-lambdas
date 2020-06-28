package example.notifier;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Map;
import java.util.stream.Collectors;

public class EmailNotifier {



    public String sendNotification(String snsTopic, String emailSubject, String emailContent, Map<String, String> attributes) {

        SnsClient snsClient = SnsClient.builder().build();
        PublishRequest publishRequest = createPublishRequest(snsTopic, emailSubject, emailContent, attributes);
        PublishResponse publish = snsClient.publish(publishRequest);

        return publish.toString();
    }

    public PublishRequest createPublishRequest(String snsTopic, String emailSubject, String emailContent, Map<String, String> attributes) {
        Map<String, MessageAttributeValue> messageAttributes = createMessageAttributes(attributes);

        return PublishRequest.builder()
                .topicArn(snsTopic)
                .subject(emailSubject)
                .message(emailContent)
                .messageAttributes(messageAttributes)
                .build();
    }

    private Map<String, MessageAttributeValue> createMessageAttributes(Map<String, String> attributes) {

        Map<String, MessageAttributeValue> notificationAttributes = attributes.entrySet()
                .stream()
                .collect(Collectors.toMap(attr -> attr.getKey(),
                        attr -> MessageAttributeValue.builder().dataType("String").stringValue(attr.getValue()).build()));

        return notificationAttributes;
    }
}
