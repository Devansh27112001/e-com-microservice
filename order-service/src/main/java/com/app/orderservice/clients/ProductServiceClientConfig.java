package com.app.orderservice.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ProductServiceClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder getRestClientBuilder() {
        return RestClient.builder();
    }

    @Primary
    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public ProductServiceClient restClientInterface(@LoadBalanced RestClient.Builder  clientBuilder) {
        RestClient restClient = clientBuilder
                .baseUrl("http://product-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,((request, response) -> Optional.empty()))
                .build();
        RestClientAdapter clientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(clientAdapter)
                .build();
        return factory.createClient(ProductServiceClient.class);
    }
}
