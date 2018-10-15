package io.vertx.grpc;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.grpc.RequestServiceGrpc.RequestServiceVertxStub;
import java.util.UUID;

public class Client extends AbstractVerticle {

  @Override
  public void start() {

    ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, "localhost", 8080)
      .usePlaintext(true)
      .build();

    RequestServiceVertxStub stub = RequestServiceGrpc.newVertxStub(channel);

    Request request = Request.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setMessage(UUID.randomUUID().toString())
        .build();

    stub.makeRequest(request, result -> {
      if (result.succeeded()) {
        System.out.println(result.result());
      } else {
        System.out.println("Fail to get response " + result.cause());
      }
    });

  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Client());
  }

}