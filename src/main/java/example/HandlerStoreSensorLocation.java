package example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import example.model.SensorData;
import example.model.SensorLocationData;

public class HandlerStoreSensorLocation implements RequestHandler<SensorData, SensorLocationData> {

    private static final String DYNAMO_DB_COLLECTION_NAME = "sensors";

    //private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public SensorLocationData handleRequest(SensorData event, Context context) {
        LambdaLogger logger = context.getLogger();

        // process event
        //logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        DynamoDB dynamoDB = createDynamoDB();
        SensorLocationData sensorLocationData = new SensorLocationData(event.getId(), event.getLocation());

        PutItemSpec itemToAdd = new PutItemSpec().withItem(getItem(sensorLocationData));
        dynamoDB.getTable(DYNAMO_DB_COLLECTION_NAME).putItem(itemToAdd);

        return sensorLocationData;
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