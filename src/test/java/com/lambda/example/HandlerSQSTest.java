package com.lambda.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HandlerSQSTest {
    private HandlerSQS handlerSQS;

    @Mock
    SQSEvent sqsEvent;

    @Mock
    Context context;

    @Mock
    LambdaLogger lambdaLogger;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        handlerSQS = new HandlerSQS();
        when(context.getLogger()).thenReturn(lambdaLogger);
        doReturn(getRecords()).when(sqsEvent).getRecords();
    }

    @Test
    void testMessageHandler() {
        String result = handlerSQS.handleRequest(sqsEvent, context);
        assertEquals("200 OK", result);
    }

    private List<SQSEvent.SQSMessage> getRecords() throws IOException, URISyntaxException {
        URI uri = requireNonNull(getClass().getClassLoader().getResource("Sample.json")).toURI();
        Reader reader = Files.newBufferedReader(Paths.get(uri));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SQSMessage sqsMessage = gson.fromJson(reader, SQSMessage.class);
        List<SQSEvent.SQSMessage> sqsMessages = new ArrayList<>();
        sqsMessages.add(sqsMessage);

        return sqsMessages;
    }
}