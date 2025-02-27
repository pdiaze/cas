package org.apereo.cas.util.http;

import org.apereo.cas.web.HttpMessage;
import java.net.URL;

/**
 * Define the behaviour of a HTTP client.
 *
 * @author Jerome Leleu
 * @author Misagh Moayyed
 * @since 4.0.0
 */
public interface HttpClient {

    /**
     * Bean name for the client implementation that supports CAS trust store.
     */
    String BEAN_NAME_HTTPCLIENT_TRUST_STORE = "supportsTrustStoreSslSocketFactoryHttpClient";

    /**
     * Bean name for the client implementation that does not follow redirects.
     */
    String BEAN_NAME_HTTPCLIENT_NO_REDIRECT = "noRedirectHttpClient";

    /**
     * Bean name for the default client implementation.
     */
    String BEAN_NAME_HTTPCLIENT = "httpClient";

    /**
     * Sends a message to a particular endpoint.  Option of sending it without
     * waiting to ensure a response was returned.
     * This is useful when it doesn't matter about the response as you'll perform no action based on the response.
     *
     * @param message The message that should be sent to the http endpoint
     * @return boolean if the message was sent, or async was used.  false if the message failed.
     * @since 4.1.0
     */
    boolean sendMessageToEndPoint(HttpMessage message);

    /**
     * Contact the URL endpoint as a GET and return the resulting http message.
     *
     * @param url the url
     * @return the http message
     */
    HttpMessage sendMessageToEndPoint(URL url);

    /**
     * Make a synchronous HTTP(S) call to ensure that the url is reachable.
     *
     * @param url the url to call
     * @return whether the url is valid
     */
    boolean isValidEndPoint(String url);

    /**
     * Make a synchronous HTTP(S) call to ensure that the url is reachable.
     *
     * @param url the url to call
     * @return whether the url is valid
     */
    boolean isValidEndPoint(URL url);

    /**
     * Gets wrapped http client.
     *
     * @return the wrapped http client
     */
    org.apache.hc.client5.http.classic.HttpClient wrappedHttpClient();

    /**
     * Gets http client factory.
     *
     * @return the http client factory
     */
    HttpClientFactory httpClientFactory();
}
