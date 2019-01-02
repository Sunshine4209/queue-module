/*
 * Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  https://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

package com.iglobal.services.queue;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * This sample demonstrates how to make basic requests to Amazon SQS using the
 * AWS SDK for Java.
 * <p>
 * Prerequisites: You must have a valid Amazon Web Services developer account,
 * and be signed up to use Amazon SQS. For more information about Amazon SQS,
 * see https://aws.amazon.com/sqs
 * or you can use localstack with docker.
 * <p>
 * Make sure that your credentials are located in ~/.aws/credentials
 */
public class SQSSimpleJavaClientExample
{
    public static void main(String[] args)
    {

        /*
         * Create a new instance of the a queue client with endpoints configurations.
         * move this to its own file and use dagger to instantiate and set right config values.
         */
        final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
            .withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(System.getenv("aws-endpoint-url"), System.getenv("aws-region"))
            ).build();

        String myQueueUrl = "http://localhost:4576/queue/zonos";

        SqsQueue sqsQueue = new SqsQueue(sqs, myQueueUrl);

        try {

            // Creating a message.
            System.out.println("Receiving messages from MyQueue.\n");
            Map<String, String> test = new HashMap<String, String>();
            test.put("ordderId", "2-122");
            test.put("someKeyValue", "this is a message 221");
            SqsMessage message = new SqsMessage(test.toString());
            message.addStringMessageAttributes("createdAt", "String", new Date().toString());

            // Send a message.
            System.out.println("Sending a message to MyQueue.\n");
            sqsQueue.enqueue(message);

            // Receive messages.
            System.out.println("Receiving messages from MyQueue.\n");
            SqsMessage fetchMessage = (SqsMessage)sqsQueue.fetch();

            if (fetchMessage != null) {
                printMessage(fetchMessage);

                System.out.println("Deleting message " +fetchMessage.getId() + " from MyQueue.\n");
                sqsQueue.delete(fetchMessage.getId());
            }


            List<String> attributeNames = new ArrayList<String>();
            attributeNames.add("All");

            GetQueueAttributesRequest queueAttributesRequest = new GetQueueAttributesRequest(myQueueUrl);
            queueAttributesRequest.setAttributeNames(attributeNames);
            Map<String, String> attributes = sqs.getQueueAttributes(queueAttributesRequest).getAttributes();
            String size = attributes.get("ApproximateNumberOfMessages");

            System.out.println("  size:     " + size);

            SqsMessage dequeuedMessage = (SqsMessage)sqsQueue.dequeue();

            if (dequeuedMessage != null) {
                printMessage(dequeuedMessage);
            }

        } catch (final AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());

        } catch (final AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means " +
                    "the client encountered a serious internal problem while " +
                    "trying to communicate with Amazon SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static void printMessage(SqsMessage message)
    {
        System.out.println("Message");
        System.out.println("  MessageId:     "  + message.getId());
        System.out.println("  Body:          "  + message.getPayload());
        System.out.println("  Message Attribute:          "  + message.getMessageAttributes().toString());
    }
}