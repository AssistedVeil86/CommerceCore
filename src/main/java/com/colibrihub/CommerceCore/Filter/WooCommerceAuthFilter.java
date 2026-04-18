package com.colibrihub.CommerceCore.Filter;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class WooCommerceAuthFilter implements ExchangeFilterFunction {

    private final OAuth10aService oAuthService;

    private final OAuth1AccessToken emptyToken = new OAuth1AccessToken("", "");

    public WooCommerceAuthFilter(String consumerKey, String consumerSecret) {
        this.oAuthService = new ServiceBuilder(consumerKey)
                .apiSecret(consumerSecret)
                .build(new WooCommerceApi());
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        var uri = request.url();
        var method = request.method().name();

        var oauthRequest = new OAuthRequest(Verb.valueOf(method), uri.toString());

        oAuthService.signRequest(emptyToken, oauthRequest);
        String authorizationHeader = oauthRequest.getHeaders().get("Authorization");

        ClientRequest mutatedRequest = ClientRequest.from(request)
                .header("Authorization", authorizationHeader)
                .build();

        return next.exchange(mutatedRequest);
    }

    private static class WooCommerceApi extends DefaultApi10a {
        @Override
        public String getRequestTokenEndpoint() { return null; }
        @Override
        public String getAccessTokenEndpoint() { return null; }

        @Override
        protected String getAuthorizationBaseUrl() { return ""; }

        @Override
        public String getAuthorizationUrl(com.github.scribejava.core.model.OAuth1RequestToken token) { return ""; }
    }
}
