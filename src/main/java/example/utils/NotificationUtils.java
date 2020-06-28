package example.utils;

import example.model.AirPollutionNotification;
import example.model.AirPollutionParameter;

import java.util.stream.Collectors;

public class NotificationUtils {

    public static String toEmailContent(AirPollutionParameter airPollutionParameter) {
        return String.format("%s current value: %s ug/m3, warning level: %d %%",
                airPollutionParameter.getName(),
                airPollutionParameter.getCurrentValue(),
                airPollutionParameter.getPercentChange());
    }

    public static  String toEmailContent(AirPollutionNotification airPollutionNotification) {
        String notificationString = airPollutionNotification.getParameters()
                .stream().map(NotificationUtils::toEmailContent).collect(Collectors.joining("\n"));

        return "The following parameters of air pollution have exceeded threshold values: \n\n" + notificationString;
    }
}
