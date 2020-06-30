package org.polsl.co.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.polsl.co.model.StationFilterPolicy;
import org.polsl.co.model.RegisterUserData;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SetSubscriptionAttributesRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

import static org.polsl.co.configuration.Configuration.AIR_POLLUTION_TOPIC;

public class CreateEmailSubscriptionHandler implements RequestHandler<SNSEvent, String> {

    private static final String FILTER_POLICY = "FilterPolicy";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private SnsClient snsClient = SnsClient.builder().build();

    @Override
    public String handleRequest(SNSEvent event, Context context) {
        LambdaLogger logger = context.getLogger();

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        RegisterUserData registerUserData = convertToRegisterData(event);

        SubscribeResponse subscribe = snsClient.subscribe(createSubscriptionRequest(registerUserData.getEmail()));
        snsClient.setSubscriptionAttributes(createStationFiltering(registerUserData.getStationId(), subscribe.subscriptionArn()));

        return subscribe.toString();
    }

    private SetSubscriptionAttributesRequest createStationFiltering(String stationId, String subscription) {

        String stationFilterPolicyString = getStationFilterPolicyString(stationId);

        return SetSubscriptionAttributesRequest.builder()
                .subscriptionArn(subscription)
                .attributeName(FILTER_POLICY)
                .attributeValue(stationFilterPolicyString)
                .build();
    }

    private String getStationFilterPolicyString(String stationId) {
        StationFilterPolicy filterPolicy = new StationFilterPolicy();
        filterPolicy.add(stationId);
        return gson.toJson(filterPolicy);
    }

    private RegisterUserData convertToRegisterData(SNSEvent event) {
        String raw = event.getRecords().get(0).getSNS().getMessage();
        return gson.fromJson(raw, RegisterUserData.class);
    }

    private SubscribeRequest createSubscriptionRequest(String email) {
        return SubscribeRequest.builder()
                .topicArn(AIR_POLLUTION_TOPIC)
                .protocol("email")
                .endpoint(email)
                .returnSubscriptionArn(true)
                .build();
    }
}