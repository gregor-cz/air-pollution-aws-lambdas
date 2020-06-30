package org.polsl.co.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.polsl.co.model.SensorData;
import org.polsl.co.model.SensorLocationData;

public class StoreSensorLocationHandler implements RequestHandler<SensorData, SensorLocationData> {

    private static final String DYNAMO_DB_COLLECTION_NAME = "sensors";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final DynamoDB dynamoDB = createDynamoDB();

    @Override
    public SensorLocationData handleRequest(SensorData event, Context context) {
        LambdaLogger logger = context.getLogger();

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        SensorLocationData sensorLocationData = SensorLocationData.from(event);
        storeSensorLocation(sensorLocationData);

        return sensorLocationData;
    }

    private void storeSensorLocation(SensorLocationData sensorLocationData) {
        PutItemSpec itemToAdd = new PutItemSpec().withItem(createsItem(sensorLocationData));
        dynamoDB.getTable(DYNAMO_DB_COLLECTION_NAME).putItem(itemToAdd);
    }

    private DynamoDB createDynamoDB() {
        return new DynamoDB(AmazonDynamoDBClientBuilder.standard().build());
    }

    private Item createsItem(SensorLocationData sensorLocationData) {
        return new Item()
                .withPrimaryKey("id", sensorLocationData.getId())
                .withNumber("longitude", sensorLocationData.getLocation().getLongitude())
                .withNumber("latitude", sensorLocationData.getLocation().getLatitude());
    }
}