package org.polsl.co.configuration;

public class Configuration {

    public static final String EMAIL_POLLUTION_TOPIC = "arn:aws:sns:us-east-2:464446151961:email-notification-topic";

    public static final String SMS_NOTIFICATION_TOPIC = "arn:aws:sns:us-west-2:464446151961:testsms";

    public static final String AIR_QUALITY_THRESHOLDS_TABLE = "thresholds";

    private Configuration() {
    }
}
