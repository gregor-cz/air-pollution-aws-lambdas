import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.polsl.co.model.AirPollutionThresholds;

import java.util.Iterator;

public class CopyDynamoDBTable {

    public static void main(String[] args) {

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("air_pollution_data2");

        Table raw_data = dynamoDB.getTable("raw_data");


        ItemCollection<ScanOutcome> scan = table.scan(new ScanSpec());



        Iterator<Item> iter = scan.iterator();
        while (iter.hasNext()) {
            Item item2 = iter.next();
            //System.out.println(item2.toString());
            raw_data.putItem(item2);
        }
    }
}
