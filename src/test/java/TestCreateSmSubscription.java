import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SetSubscriptionAttributesRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

public class TestCreateSmSubscription {

    public static final String
            FILTER_POLICY = "FilterPolicy";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final static String AIR_POLLUTION_NOTIFICATION_TOPIC = "arn:aws:sns:us-west-2:464446151961:testsms";

    public static void main(String[] args) {




        SnsClient snsClient = SnsClient.builder().region(Region.US_WEST_2).build();
        SubscribeResponse subscribe = snsClient.subscribe(createSubscriptionRequest("+48503836347"));
        snsClient.setSubscriptionAttributes(createStationFiltering("station1234", subscribe.subscriptionArn()));
    }

    private static SetSubscriptionAttributesRequest createStationFiltering(String stationId, String subscription) {
        String stationFilterPolicyString = "{\"station_id\":[\"" + stationId + "\"]}";

        return SetSubscriptionAttributesRequest.builder().subscriptionArn(subscription)
                .attributeName(FILTER_POLICY).attributeValue(stationFilterPolicyString).build();
    }

    private static SubscribeRequest createSubscriptionRequest(String phoneNumber) {
        return SubscribeRequest.builder()
                .topicArn(AIR_POLLUTION_NOTIFICATION_TOPIC)
                .protocol("sms")
                .endpoint(phoneNumber)
                .returnSubscriptionArn(true)
                .build();
    }
}
