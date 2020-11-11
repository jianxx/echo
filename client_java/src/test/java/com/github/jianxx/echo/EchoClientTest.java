package com.github.jianxx.echo;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import echo.TransceiverGrpc;
import echo.TransceiverOuterClass.EchoRequest;
import echo.TransceiverOuterClass.EchoResponse;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;

/**
 * Unit test for echo client.
 */
public class EchoClientTest {
    /**
     * This rule manages automatic graceful shutdown for the registered servers and
     * channels at the end of test.
     */
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private EchoClient client;

    private TransceiverGrpc.TransceiverImplBase serviceImpl = mock(TransceiverGrpc.TransceiverImplBase.class,
            delegatesTo(new TransceiverGrpc.TransceiverImplBase() {
                // By default the client will receive Status.UNIMPLEMENTED for all RPCs.
                // You might need to implement necessary behaviors for your test here, like
                // this:
                //
                @Override
                public void echo(EchoRequest request, StreamObserver<EchoResponse> respObserver) {
                    respObserver.onNext(EchoResponse.getDefaultInstance());
                    respObserver.onCompleted();
                }
            }));

    @Before
    public void setUp() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();

        // Create a server, add service, start, and register for automatic graceful
        // shutdown.
        grpcCleanup.register(
                InProcessServerBuilder.forName(serverName).directExecutor().addService(serviceImpl).build().start());

        // Create a client channel and register for automatic graceful shutdown.
        ManagedChannel channel = grpcCleanup
                .register(InProcessChannelBuilder.forName(serverName).directExecutor().build());

        // Create a EchoClient using the in-process channel;
        client = new EchoClient(channel);
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void echo() {
        ArgumentCaptor<EchoRequest> requestCaptor = ArgumentCaptor.forClass(EchoRequest.class);

        client.echo("Hello");

        verify(serviceImpl).echo(requestCaptor.capture(), ArgumentMatchers.<StreamObserver<EchoResponse>>any());
        assertEquals("Hello", requestCaptor.getValue().getFromClient().getMessage());
    }
}
