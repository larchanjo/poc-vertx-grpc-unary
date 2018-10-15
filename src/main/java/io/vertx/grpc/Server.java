package io.vertx.grpc;

import io.grpc.BindableService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.grpc.RequestServiceGrpc.RequestServiceVertxImplBase;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends AbstractVerticle {

  private static final AtomicInteger requestCounter = new AtomicInteger(1);

  @Override
  public void start() {

    BindableService bindableService = new RequestServiceVertxImplBase() {

      @Override
      public void makeRequest(Request request, Future<Response> response) {
        System.out.println("Receveid " + request.toString());
        response.complete(Response.newBuilder()
            .setId(String.valueOf(requestCounter.getAndIncrement()))
            .setMessage(UUID.randomUUID().toString())
            .build());
      }

    };

    VertxServer rpcServer = VertxServerBuilder
        .forPort(vertx, 8080)
        .addService(bindableService)
        .build();

    rpcServer.start(result -> {
      if (result.succeeded()) {
        System.out.println("Server started at 8080");
      } else {
        System.out.println("Fail to start server " + result.cause().getMessage());
      }
    });

  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Server());
  }

}