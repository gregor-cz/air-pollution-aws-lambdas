package example.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.model.ThresholdParameter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThresholdsProvider {

    private static final String AIR_POLLUTION_THRESHOLDS_TABLE = "air_pollution_thresholds2";

    private DynamoDB dynamoDB;

    public ThresholdsProvider() {
        this.dynamoDB = createDynamoDBClient();
    }

    private DynamoDB createDynamoDBClient() {
        AmazonDynamoDB awsDynamoDb = AmazonDynamoDBClientBuilder.standard().build();
        return new DynamoDB(awsDynamoDb);
    }

    public Map<String, ThresholdParameter> getThresholds() {

        Gson gson = new GsonBuilder().create();

        Table table = dynamoDB.getTable(AIR_POLLUTION_THRESHOLDS_TABLE);

        ItemCollection<ScanOutcome> scan = table.scan(new ScanSpec());

        Map<String, ThresholdParameter> airPollutionThresholds = new HashMap<>();

        Iterator<Item> iterator = scan.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            ThresholdParameter thresholdParameter = gson.fromJson(item.toJSON(), ThresholdParameter.class);
            airPollutionThresholds.put(thresholdParameter.getId(), thresholdParameter);
        }

        return airPollutionThresholds;
    }
}
