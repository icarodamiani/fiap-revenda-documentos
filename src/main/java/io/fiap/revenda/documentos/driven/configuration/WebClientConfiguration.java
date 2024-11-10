package io.fiap.revenda.documentos.driven.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfiguration {

    @Bean("DetranWebClient")
    public WebClient mercadoPagoWebClient(@Value("${detran.client.connectionTimeout:50000}") Integer connectionTimeout,
                                   @Value("${detran.client.responseTimeout:50000}") Integer responseTimeout,
                                   @Value("${detran.client.readTimeout:50000}") Integer readTimeout,
                                   @Value("${detran.client.writeTimeout:50000}") Integer writeTimeout,
                                   @Value("${detran.client.maxConnections:5}") Integer maxConnections,
                                   @Value("${detran.client.url:http://localhost:8081}") String uri) {
        return getWebClient(connectionTimeout, responseTimeout, readTimeout, writeTimeout, maxConnections, uri, "payment");
    }

    private WebClient getWebClient(Integer connectionTimeout, Integer responseTimeout,
                                   Integer readTimeout, Integer writeTimeout, Integer maxConnections,
                                   String uri, String name) {

        HttpClient httpClient = HttpClient.create(ConnectionProvider.create(name, maxConnections))
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
            .responseTimeout(Duration.ofMillis(responseTimeout))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
            .baseUrl(uri)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

}
