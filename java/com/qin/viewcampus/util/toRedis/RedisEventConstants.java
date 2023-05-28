package com.qin.viewcampus.util.toRedis;

public class RedisEventConstants {
    public static final String Event_key = "Event:";
    public static final String Event_Activated_IdSet_key = "Events:activated:idSet";
    public static final String Event_event = "event";
    public static final String Event_eventInformation = "eventInformation";
    public static final Long Event_Activated_TTL = 10L;
    public static final Long Events_Activated_IdSet_TTL = 3L;
}
