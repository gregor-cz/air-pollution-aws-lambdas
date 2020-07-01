package org.polsl.co.subscriber;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.polsl.co.model.StationFilterPolicy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.*;

public class SnsSubscriber {

    private final String FILTER_POLICY = "FilterPolicy";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String AIR_POLLUTION_NOTIFICATION_TOPIC = "arn:aws:sns:us-west-2:464446151961:testsms";

    public List<SnsResponse> createSubscription(Region region, String topicArn, String protocolType, String endpoint, String stationId) {

        SnsClient snsClient = SnsClient.builder().region(region).build();
        SubscribeResponse subscribeResponse = snsClient.subscribe(createSubscriptionRequest(topicArn, protocolType, endpoint));
        SetSubscriptionAttributesResponse subscriptionAttributesResponse = snsClient.setSubscriptionAttributes(createStationFiltering(stationId, subscribeResponse.subscriptionArn()));
        return Arrays.asList(subscribeResponse, subscriptionAttributesResponse);
    }

    public List<SnsResponse> createEmailSubscription(Region region, String topicArn, String endpoint, String stationId) {
        return createSubscription(region, topicArn, "email", endpoint, stationId);
    }

    public List<SnsResponse> createSmsSubscription(Region region, String topicArn, String endpoint, String stationId) {
        return createSubscription(region, topicArn, "sms", endpoint, stationId);
    }

    private SetSubscriptionAttributesRequest createStationFiltering(String stationId, String subscriptionArn) {
        String stationFilterPolicyString = getStationFilterPolicyString(stationId);

        return SetSubscriptionAttributesRequest.builder().subscriptionArn(subscriptionArn)
                .attributeName(FILTER_POLICY).attributeValue(stationFilterPolicyString).build();
    }

    private String getStationFilterPolicyString(String stationId) {
        StationFilterPolicy filterPolicy = new StationFilterPolicy();
        filterPolicy.add(stationId);
        return gson.toJson(filterPolicy);
    }


    private SubscribeRequest createSubscriptionRequest(String topicArn, String protocolType, String endpoint) {
        return SubscribeRequest.builder()
                .topicArn(topicArn)
                .protocol(protocolType)
                .endpoint(endpoint)
                .returnSubscriptionArn(true)
                .build();
    }
}
