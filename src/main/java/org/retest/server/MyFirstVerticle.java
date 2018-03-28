package org.retest.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class MyFirstVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> fut) {
	 Router router = Router.router(vertx);
	 router.route("/").handler(routingContext -> {
	   HttpServerResponse response = routingContext.response();
	   response.sendFile("../../src/main/resources/login.html");
//	       .putHeader("content-type", "text/html")
//	       .end("../../src/main/resources/login.html");
	 });

	 // Serve static resources from the /assets directory
	 router.route("/assets/*").handler(StaticHandler.create("assets"));

	 vertx
	     .createHttpServer()
	     .requestHandler(router::accept)
	     .listen(
	         // Retrieve the port from the configuration,
	         // default to 8080.
	         config().getInteger("http.port", 8080),
	         result -> {
	           if (result.succeeded()) {
	             fut.complete();
	           } else {
	             fut.fail(result.cause());
	           }
	         }
	     );
	}
	
	 public static void main(String[] args) {
	        Vertx vertx = Vertx.vertx();
	        vertx.deployVerticle(MyFirstVerticle.class.getName());
	    }
	
}
