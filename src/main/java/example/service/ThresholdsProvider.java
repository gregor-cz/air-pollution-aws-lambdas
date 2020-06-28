package example.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.model.AirPollutionThreshold;

import java.util.Iterator;

public class ThresholdsProvider {

    private static final String AIR_POLLUTION_THRESHOLDS_TABLE = "air_pollution_thresholds";

    private DynamoDB dynamoDB;

    public ThresholdsProvider() {
        this.dynamoDB = createDynamoDBClient();
    }

    private DynamoDB createDynamoDBClient() {
        AmazonDynamoDB awsDynamoDb = AmazonDynamoDBClientBuilder.standard().build();
        return new DynamoDB(awsDynamoDb);
    }

    public AirPollutionThreshold getThreshold() {

        Gson gson = new GsonBuilder().create();

        Table reply = dynamoDB.getTable(AIR_POLLUTION_THRESHOLDS_TABLE);

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("id = :v_id")
                .withValueMap(new ValueMap()
                        .withNumber(":v_id", 1));

        ItemCollection<QueryOutcome> items;
        items = reply.query(spec);

        Iterator<Item> iterator = items.iterator();

        return gson.fromJson(iterator.next().toJSON(), AirPollutionThreshold.class);
    }

}
