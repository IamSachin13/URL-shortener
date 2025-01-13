package com.sachin.urlshortener.common.messagecontants;

public class MessageConstants {

    public static final String IP_BLOCKED_MESSAGE = "Your IP has been blocked due to too many requests.";

    //ERROR MESSAGE CONSTANTS
    public static final String ERROR_SERIALIZING_URL_RECORD = "Error serializing UrlRecord";
    public static final String ERROR_DESERIALIZING_URL_RECORD = "Error deserializing UrlRecord";

    //INFO MESSAGE CONSTANTS
    public static final String SHORT_URL_ALIAS_ALREADY_IN_USE = "Short URL alias is already in use: ";
    public static final String SHORT_URL_NOT_FOUND = "Short URL not found: ";
}
