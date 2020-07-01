package org.polsl.co.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.polsl.co.model.RegisterUserData;
import org.polsl.co.subscriber.SnsSubscriber;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.model.SnsResponse;

import java.util.List;

import static org.polsl.co.configuration.Configuration.SMS_NOTIFICATION_TOPIC;

public class CreateSMSSubscriptionHandler implements RequestHandler<SNSEvent, String> {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private SnsSubscriber snsSubscriber = new SnsSubscriber();

    @Override
    public String handleRequest(SNSEvent event, Context context) {
        LambdaLogger logger = context.getLogger();

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        RegisterUserData registerUserData = convertToRegisterData(event);

        List<SnsResponse> smsSubscription = snsSubscriber.createSmsSubscription(Region.US_WEST_2, SMS_NOTIFICATION_TOPIC,
                registerUserData.getPhoneNumber(), registerUserData.getStationId());

        return gson.toJson(smsSubscription);
    }


    private RegisterUserData convertToRegisterData(SNSEvent event) {
        String raw = event.getRecords().get(0).getSNS().getMessage();
        return gson.fromJson(raw, RegisterUserData.class);
    }
}