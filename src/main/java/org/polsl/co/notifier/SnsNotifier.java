package org.polsl.co.notifier;


import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Map;
import java.util.stream.Collectors;

public class SnsNotifier {

    public String sendNotification(Region region, String snsTopic, String messageSubject, String messageContent, Map<String, String> attributes) {

        SnsClient snsClient = SnsClient.builder().region(region).build();
        PublishRequest publishRequest = createPublishRequest(snsTopic, messageSubject, messageContent, attributes);
        PublishResponse publish = snsClient.publish(publishRequest);

        return publish.toString();
    }

    private PublishRequest createPublishRequest(String snsTopic, String messageSubject, String messageContent, Map<String, String> attributes) {
        Map<String, MessageAttributeValue> messageAttributes = createMessageAttributes(attributes);

        return PublishRequest.builder()
                .topicArn(snsTopic)
                .subject(messageSubject)
                .message(messageContent)
                .messageAttributes(messageAttributes)
                .build();
    }

    private Map<String, MessageAttributeValue> createMessageAttributes(Map<String, String> attributes) {

        Map<String, MessageAttributeValue> notificationAttributes = attributes.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        attr -> MessageAttributeValue.builder().dataType("String").stringValue(attr.getValue()).build()));

        return notificationAttributes;
    }
}
