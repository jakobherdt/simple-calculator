package org.retest.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

public class Server extends AbstractVerticle {

//	private UserRepo userList;
	private TemplateEngine engine;
	private User user;

	public Server() {
		engine = HandlebarsTemplateEngine.create().setExtension("html");
//		UserRepo userList = new UserRepo().init();
	}
	
//	    @Override
//	    public void start(Future<Void> startFuture) throws Exception {
//	  
//	    	Router router = Router.router(vertx);
//	    	router.route("/login").handler(this::login);
//			router.route("/logout").handler(this::logout);
////			router.route("/showLogin").handler(this::showLogin);
//			router.route("/calculator").handler(this::calculator);
//	        HttpServer httpServer = vertx
//	                .createHttpServer(new HttpServerOptions().setPort(8080))
//	                .requestHandler(router::accept);
//	    
//	        httpServer.listen(status -> {
//	            if (status.succeeded()) {
//	                startFuture.complete();
//	                return;
//	            }
//	            startFuture.fail(status.cause());
//	        });
//	    }
	
	public void start() {
		Router router = Router.router(vertx);
		router.route("/login").handler(this::login);
		router.route("/logout").handler(this::logout);
		router.route("/showLogin").handler(this::showLogin);
		router.route("/calculator").handler(this::calculator);
		router.route("/loginFailed").handler(this::loginFailed);
		router.route().handler(this::show);
		
		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router::accept).listen(8080);
	}
	
	public void show(RoutingContext context) {
		if (user == null) {
			context.reroute("/showLogin");
		} 
	}

	public void login(RoutingContext context) {
//		if(checkLogin(context)) {
//			context.reroute("/calculator");
//		} else {
//			context.reroute("/loginFailed");
//		}
		String name = context.request().getParam("user");
		String password = context.request().getParam("password");
		if (!(("Max".equals(name))||("retest".equals(password)))) {
			context.reroute("/loginFailed");
		} else {
			context.reroute("/calculator");
		}
	}
	
	public void calculator(RoutingContext context) {
		context.reroute("/calculator.html");
	}

	public void logout(RoutingContext context) {
		user = null;
		context.reroute("/login");
	}

	public void showLogin(RoutingContext context) {
		engine.render(context, "src/main/resources/login.html", res -> {
			if (res.succeeded()) {
				context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
		context.reroute("/login");
	}
	
	public void loginFailed(RoutingContext context) {
//		if(checkLogin(context)) {
//			context.reroute("/calculator");
//		} else {
//			context.reroute("/loginFailed");
//		}
		String name = context.request().getParam("user");
		String password = context.request().getParam("password");
		if (!(("Max".equals(name))||("retest".equals(password)))) {
			context.reroute("/loginFailed");
		} else {
			context.reroute("/calculator");
		}
	}
	
	public boolean checkLogin(RoutingContext context) {
		boolean applied = true;
		String name = context.request().getParam("user");
		String password = context.request().getParam("password");
		if (!(("Max".equals(name))||("retest".equals(password)))) {
			applied = false;
		}else {
			user = new User(name, password);
			applied = true;
		}
		return applied;
	}
	
	 public static void main(String[] args) {
	        Vertx vertx = Vertx.vertx();
	        vertx.deployVerticle(Server.class.getName());
	    }

}
