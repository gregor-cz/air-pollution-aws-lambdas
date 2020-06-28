package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import example.model.AirPollutionNotification;
import example.model.SensorData;
import example.notifier.EmailNotifier;
import example.service.AirPollutionService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static example.configuration.AwsConfiguration.AIR_POLLUTION_TOPIC;
import static example.utils.NotificationUtils.toEmailContent;

public class ProcessAirPollutionData implements RequestHandler<SensorData, String> {

    private static final String STATION_ID_KEY = "station_id";
    private static final String EMAIL_SUBJECT = "Air pollution notification";


    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final AirPollutionService airPollutionService = new AirPollutionService();
    private final EmailNotifier emailNotifier = new EmailNotifier();

    private LambdaLogger logger;

    @Override
    public String handleRequest(SensorData event, Context context) {
        logger = context.getLogger();

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        Optional<AirPollutionNotification> notification = airPollutionService.process(event);

        String lambdaResult = notification.map(this::sendNotification)
                .orElse("Air pollution parameters have exceeded threshold values.");

        logger.log("Lambda result: " + lambdaResult);
        return lambdaResult;
    }

    private String sendNotification(AirPollutionNotification data) {
        Map<String, String> attributes = createNotificationParams(data.getStationId());
        return emailNotifier.sendNotification(AIR_POLLUTION_TOPIC, EMAIL_SUBJECT, toEmailContent(data), attributes);
    }

    private Map<String, String> createNotificationParams(String stattionId) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(STATION_ID_KEY, stattionId);
        return attributes;
    }
}