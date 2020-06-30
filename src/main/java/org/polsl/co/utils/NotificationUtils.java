package org.polsl.co.utils;

import org.polsl.co.model.AirQualityNotification;
import org.polsl.co.model.AirQualityData;

import java.util.stream.Collectors;

public class NotificationUtils {

    public static String toEmailContent(AirQualityData airQualityData) {
        return String.format("%s current value: %s ug/m3, warning level: %d %%",
                airQualityData.getName(),
                airQualityData.getCurrentValue(),
                airQualityData.getPercentChange());
    }

    public static  String toEmailContent(AirQualityNotification airQualityNotification) {
        String notificationString = airQualityNotification.getParameters()
                .stream().map(NotificationUtils::toEmailContent).collect(Collectors.joining("\n"));

        return "The following parameters of air pollution have exceeded threshold values: \n\n" + notificationString;
    }
}
