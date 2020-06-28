package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.model.RegisterUserData;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SetSubscriptionAttributesRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

public class HandlerCreateSubscription2 implements RequestHandler<SNSEvent, String> {

    public static final String
            FILTER_POLICY = "FilterPolicy";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final static String AIR_POLLUTION_NOTIFICATION_TOPIC = "arn:aws:sns:us-east-2:464446151961:email-notification-topic";

    @Override
    public String handleRequest(SNSEvent event, Context context) {
        LambdaLogger logger = context.getLogger();

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        RegisterUserData registerUserData = convertToRegisterData(event);

        SnsClient snsClient = SnsClient.builder().build();
        SubscribeResponse subscribe = snsClient.subscribe(createSubscriptionRequest(registerUserData));
        snsClient.setSubscriptionAttributes(createStationFiltering(registerUserData, subscribe.subscriptionArn()));

        return subscribe.toString();
    }

    private SetSubscriptionAttributesRequest createStationFiltering(RegisterUserData registerUserData, String subscription) {
        String stationFilterPolicyString = "{\"station_id\":[\"" + registerUserData.getStationId() + "\"]}";

        return SetSubscriptionAttributesRequest.builder().subscriptionArn(subscription)
                .attributeName(FILTER_POLICY).attributeValue(stationFilterPolicyString).build();
    }

    private RegisterUserData convertToRegisterData(SNSEvent event) {
        String raw = event.getRecords().get(0).getSNS().getMessage();

        return gson.fromJson(raw, RegisterUserData.class);
    }

    private SubscribeRequest createSubscriptionRequest(RegisterUserData registerUserData) {
        return SubscribeRequest.builder()
                    .topicArn(AIR_POLLUTION_NOTIFICATION_TOPIC)
                    .protocol("email")
                    .endpoint(registerUserData.getEmail())
                    .returnSubscriptionArn(true)
                    .build();
    }
}