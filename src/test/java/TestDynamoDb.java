import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.model.AirPollutionThreshold;

import java.util.Iterator;

public class TestDynamoDb {

    public static void main(String[] args) {

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table reply = dynamoDB.getTable("air_pollution_thresholds");

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("id = :v_id")
                .withValueMap(new ValueMap()
                        .withNumber(":v_id", 1));

        ItemCollection<QueryOutcome> items;
        items = reply.query(spec);

        Iterator<Item> iterator = items.iterator();
        Item item = null;
//        while (iterator.hasNext()) {
//            item = iterator.next();
//            System.out.println(item.toJSON());
//            System.out.println(item.getNumber("pm25"));
//        }

        AirPollutionThreshold airPollutionThreshold = gson.fromJson(iterator.next().toJSON(), AirPollutionThreshold.class);
    }
}
