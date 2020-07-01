import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.polsl.co.model.StationFilterPolicy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SetSubscriptionAttributesRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

public class TestCreateSmSubscription {

    public static final String
            FILTER_POLICY = "FilterPolicy";
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final static String AIR_POLLUTION_NOTIFICATION_TOPIC = "arn:aws:sns:us-west-2:464446151961:testsms";

    public static void main(String[] args) {

        SnsClient snsClient = SnsClient.builder().region(Region.US_WEST_2).build();
        SubscribeResponse subscribe = snsClient.subscribe(createSubscriptionRequest(AIR_POLLUTION_NOTIFICATION_TOPIC, "sms", "+48503836347"));
        snsClient.setSubscriptionAttributes(createStationFiltering("station1234", subscribe.subscriptionArn()));
    }

    private static SetSubscriptionAttributesRequest createStationFiltering(String stationId, String subscription) {
        String stationFilterPolicyString = getStationFilterPolicyString(stationId);

        return SetSubscriptionAttributesRequest.builder().subscriptionArn(subscription)
                .attributeName(FILTER_POLICY).attributeValue(stationFilterPolicyString).build();
    }

    private static String getStationFilterPolicyString(String stationId) {
        StationFilterPolicy filterPolicy = new StationFilterPolicy();
        filterPolicy.add(stationId);
        return gson.toJson(filterPolicy);
    }


    private static SubscribeRequest createSubscriptionRequest(String topicArn, String protocolType, String endpoint) {
        return SubscribeRequest.builder()
                .topicArn(topicArn)
                .protocol(protocolType)
                .endpoint(endpoint)
                .returnSubscriptionArn(true)
                .build();
    }
}
