import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.model.SensorData;

// Handler value: HandlerAirPollutionData
public class HandlerAirPollutionData implements RequestHandler<SensorData, SensorData> {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public SensorData handleRequest(SensorData event, Context context) {
        LambdaLogger logger = context.getLogger();
        // process event
        //logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        AmazonDynamoDB client1 = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client1);


        dynamoDB.getTable("test")
                .putItem(new PutItemSpec()
                        .withItem(
                                new Item()
                                        .withPrimaryKey("id", "34242").withJSON("payload", gson.toJson(event))));

        return event;
    }
}