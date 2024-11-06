package com.function;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class SendMessageFunction {

    @FunctionName("SendMessageFunction")
    public HttpResponseMessage sendMessage(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<String> request,
        final ExecutionContext context) {

        context.getLogger().info("Processing request to send message.");

        String messageContent = request.getBody();
        String connectionString = System.getenv("ServiceBusConnectionString");
        String queueName = System.getenv("QueueName");

        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();

        senderClient.sendMessage(new ServiceBusMessage(messageContent));
        senderClient.close();

        return request.createResponseBuilder(HttpStatus.OK).body("Message sent to Service Bus").build();
    }
}
