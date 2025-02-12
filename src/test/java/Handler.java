import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

// Handler value: Handler
public class Handler implements RequestHandler<Map<String, String>, String> {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        LambdaLogger logger = context.getLogger();
        String response = new String("200 OK");
        // log execution details
        logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
        logger.log("CONTEXT: " + gson.toJson(context));
        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

//    AmazonDynamoDBClient client = new AmazonDynamoDBClient();
//    client.setRegion(Region.getRegion(Regions.US_EAST_2));
//    DynamoDB dynamoDB = new DynamoDB(client);
//
//    dynamoDB.getTable("test")
//            .putItem(new PutItemSpec()
//                    .withItem(
//                            new Item()
//                                    .withPrimaryKey("id", "34242").with("hahah", "erer")));

        return response;
    }
}