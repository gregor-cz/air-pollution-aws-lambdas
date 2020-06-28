package example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.model.RegisterUserData;
import example.model.SensorLocationData;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SetSubscriptionAttributesRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

public class HandlerCreateSubscription implements RequestHandler<RegisterUserData, SubscribeResponse> {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static String AIR_POLLUTION_NOTIFICATION_TOPIC = "arn:aws:sns:us-east-2:464446151961:email-notification-topic";

    @Override
    public SubscribeResponse handleRequest(RegisterUserData event, Context context) {
        LambdaLogger logger = context.getLogger();

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());


        SnsClient snsClient = SnsClient.builder().build();



        SubscribeRequest emailSubscriptionRequest = SubscribeRequest.builder().topicArn(AIR_POLLUTION_NOTIFICATION_TOPIC).protocol("email").endpoint(event.getEmail()).returnSubscriptionArn(true).build();

        SubscribeResponse subscribe = snsClient.subscribe(emailSubscriptionRequest);

        String filterPolicyString = "{\"station_id\":[\"" + event.getStationId() + "\"]}";

        SetSubscriptionAttributesRequest filterPolicy = SetSubscriptionAttributesRequest.builder().subscriptionArn(subscribe.subscriptionArn())
                .attributeName("FilterPolicy").attributeValue(filterPolicyString).build();

        snsClient.setSubscriptionAttributes(filterPolicy);

        return subscribe;
    }

    private DynamoDB createDynamoDB() {
        AmazonDynamoDB awsDynamoDb = AmazonDynamoDBClientBuilder.standard().build();
        return new DynamoDB(awsDynamoDb);
    }

    private Item getItem(SensorLocationData sensorLocationData) {
        return new Item()
                .withPrimaryKey("id", sensorLocationData.getId())
                .withNumber("longitude", sensorLocationData.getLocation().getLongitude())
                .withNumber("latitude", sensorLocationData.getLocation().getLatitude());
    }
}