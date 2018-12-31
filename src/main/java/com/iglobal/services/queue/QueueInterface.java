package com.iglobal.services.queue;

public interface QueueInterface
{
    /** puts a message in the queue. */
    void enqueue(MessageInterface message);

    /** Removes a message from the queue and returns it. */
    MessageInterface dequeue();

    /** Read a message from the queue but does not remove it. */
    MessageInterface fetch();

    /** Removes a message from the queue. */
    void delete(String id);
}
