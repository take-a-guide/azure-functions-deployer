package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class ReceiveMessageFunction {

    @FunctionName("ReceiveMessageFunction")
    public void run(
        @ServiceBusQueueTrigger(name = "message", queueName = "%QueueName%", connection = "ServiceBusConnectionString") String message,
        final ExecutionContext context) {

        context.getLogger().info("Received message: " + message);
    }
}
