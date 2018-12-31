package com.iglobal.services.queue;

import com.amazonaws.services.sqs.model.MessageAttributeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * this class represent a Aws Sqs message and it is specific to Aws in its implementation.
 */
public class SqsMessage implements MessageInterface
{
    private String id;
    private String payload;
    private Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();

    SqsMessage(String payload)
    {
        this.payload = payload;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String getPayload()
    {
        return this.payload;
    }

    public void setMessageAttributes(Map<String, MessageAttributeValue> mapAttribute)
    {
        this.messageAttributes = mapAttribute;
    }

    public void addStringMessageAttributes(String name, String dataType, String value)
    {
        this.messageAttributes.put(name, new MessageAttributeValue().withDataType(dataType).withStringValue(value));
    }

    public void addNumberMessageAttributes(String name, String dataType, Number value)
    {
        this.messageAttributes.put(name, new MessageAttributeValue().withDataType(dataType).withStringValue(value.toString()));
    }

    public Map<String, MessageAttributeValue> getMessageAttributes()
    {
        return this.messageAttributes;
    }

}
