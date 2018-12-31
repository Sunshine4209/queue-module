package com.iglobal.services.queue;

public interface MessageInterface
{

    final String url =  "http://localstack:4576/queue/zonos";


    /**
     * @param id Id represents a string that can be used to delete or modify attribute(s) for a particular message
     */
    void setId(String id);
    String getId();

    /**
     * @return payload the payload of the message, can be json or xml string.
     */
    String getPayload();
}
