package vertx;

import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.micrometer.PrometheusScrapingHandler;

import java.util.List;

public class SslServer {
    public static void main(String[] args) {
        Vertx vertx = io.vertx.reactivex.core.Vertx.vertx(new VertxOptions().setMetricsOptions(
                new MicrometerMetricsOptions()
                        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
                        .setEnabled(true)));
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setPemKeyCertOptions(new PemKeyCertOptions().setCertPath("cert/cert.pem").setKeyPath("cert/key.pem"));
        httpServerOptions.setSsl(true);
        httpServerOptions.setUseAlpn(true);
        httpServerOptions.setAlpnVersions(List.of(HttpVersion.HTTP_2));

        Router router = Router.router(vertx);
        router.route("/metrics").handler(PrometheusScrapingHandler.create());
        router.route("/test").handler(request -> {
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/plain");
            response.end("Hello World!");
        });
        HttpServer server = vertx.createHttpServer(httpServerOptions).requestHandler(router);
        server.rxListen(8080).subscribe(s-> System.out.println("Server started"), System.err::println);
    }

}
