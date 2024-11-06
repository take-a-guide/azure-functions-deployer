package com.function;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class RegisterMessageFunction {

    private static final CosmosClient cosmosClient = new CosmosClientBuilder()
        .endpoint(System.getenv("CosmosDBEndpoint"))
        .key(System.getenv("CosmosDBKey"))
        .consistencyLevel(ConsistencyLevel.EVENTUAL)
        .buildClient();

    private static final CosmosContainer container = cosmosClient
        .getDatabase("YourDatabaseName")
        .getContainer("YourContainerName");

    @FunctionName("RegisterMessageFunction")
    public void run(
        @ServiceBusQueueTrigger(name = "message", queueName = "%QueueName%", connection = "ServiceBusConnectionString") String message,
        final ExecutionContext context) {

        context.getLogger().info("Received message: " + message);


        CosmosItemResponse<Object> response = container.createItem(new MessageItem(message));
        context.getLogger().info("Inserted item with request charge: " + response.getRequestCharge());
    }

    public static class MessageItem {
        private String id;
        private String message;

        public MessageItem(String message) {
            this.id = java.util.UUID.randomUUID().toString();
            this.message = message;
        }

        public String getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }
    }
}