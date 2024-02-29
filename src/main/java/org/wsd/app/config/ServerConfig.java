package org.wsd.app.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ServerConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    private final int httpPort;
    private final int redirectToHttpsPort;

    @Autowired
    public ServerConfig(@Value("${http.port}") int httpPort,
                        @Value("${server.port}") int redirectToHttpsPort) {
        this.httpPort = httpPort;
        this.redirectToHttpsPort = redirectToHttpsPort;
    }

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addAdditionalTomcatConnectors(redirectConnector());
    }

    private Connector redirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(redirectToHttpsPort);
        return connector;
    }
}
