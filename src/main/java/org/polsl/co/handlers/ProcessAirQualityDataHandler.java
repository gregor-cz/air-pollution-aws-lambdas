package org.polsl.co.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.polsl.co.model.AirQualityNotification;
import org.polsl.co.model.SensorData;
import org.polsl.co.notifier.SnsNotifier;
import org.polsl.co.service.AirQualityService;
import org.polsl.co.configuration.Configuration;
import software.amazon.awssdk.regions.Region;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.polsl.co.utils.NotificationUtils.toEmailContent;

public class ProcessAirQualityDataHandler implements RequestHandler<SensorData, String> {

    private static final String STATION_ID_KEY = "station_id";
    private static final String EMAIL_SUBJECT = "Air quality notification";


    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final AirQualityService airQualityService = new AirQualityService();
    private final SnsNotifier snsNotifier = new SnsNotifier();

    private LambdaLogger logger;

    @Override
    public String handleRequest(SensorData event, Context context) {
        logger = context.getLogger();

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        Optional<AirQualityNotification> notification = airQualityService.process(event);

        String lambdaResult = notification.map(this::sendNotification)
                .orElse("None of the air quality parameters have exceeded threshold values, notification will not be sent.");

        logger.log("Lambda result: " + lambdaResult);
        return lambdaResult;
    }

    private String sendNotification(AirQualityNotification data) {
        Map<String, String> attributes = createNotificationParams(data.getStationId());
        String result1 = snsNotifier.sendNotification(Region.US_EAST_2, Configuration.EMAIL_POLLUTION_TOPIC, EMAIL_SUBJECT, toEmailContent(data), attributes);
        String result2 = snsNotifier.sendNotification(Region.US_WEST_2, Configuration.SMS_NOTIFICATION_TOPIC, EMAIL_SUBJECT, toEmailContent(data), attributes);
        return  result1 + result2;
    }

    private Map<String, String> createNotificationParams(String stattionId) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(STATION_ID_KEY, stattionId);
        return attributes;
    }
}