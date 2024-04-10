package com.archipio.notificationservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AMQPUtils {

    public static final String NOTIFICATION_BY_EMAIL_QUEUE_NAME = "notification_by_email_queue";
    public static final String NOTIFICATION_BY_EMAIL_ROUTING_KEY = "email";
    public static final String NOTIFICATION_EXCHANGE_NAME = "notification_exchange";
}