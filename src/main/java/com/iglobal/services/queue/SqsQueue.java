package com.iglobal.services.queue;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;

import java.util.List;


public class SqsQueue implements QueueInterface
{
    private AmazonSQS sqs;
    private String queueUrl;

    SqsQueue(AmazonSQS sqs, String queueUrl)
    {
        this.sqs = sqs;
        this.queueUrl = queueUrl;
    }


    public void enqueue(MessageInterface message)
    {
        SqsMessage sqsMessage = (SqsMessage) message;

        SendMessageRequest request = new SendMessageRequest(queueUrl, sqsMessage.getPayload());
        request.setMessageAttributes(sqsMessage.getMessageAttributes());
        sqs.sendMessage(request);
    }

    public MessageInterface dequeue()
    {
        MessageInterface message = this.fetch();
        if (message == null) {
            return  null;
        }
        sqs.deleteMessage(new DeleteMessageRequest(queueUrl, message.getId()));
        return message;

    }

    public MessageInterface fetch()
    {
        final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withMaxNumberOfMessages(1)
                .withMessageAttributeNames("All");
        final List<Message> sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        if (sqsMessages.isEmpty()) {
            return null;
        }

        // should we return null or have a null object ?
        Message sqsMessage = sqsMessages.get(0);

        com.iglobal.services.queue.SqsMessage message = new com.iglobal.services.queue.SqsMessage(sqsMessage.getBody());
        message.setId(sqsMessage.getReceiptHandle());
        message.setMessageAttributes(sqsMessage.getMessageAttributes());

        return message;
    }

    public void delete(String id)
    {
        sqs.deleteMessage(new DeleteMessageRequest(queueUrl, id));
    }
}
