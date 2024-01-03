package org.apereo.cas.adaptors.generic;

import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.config.CasCookieAutoConfiguration;
import org.apereo.cas.config.CasCoreAuthenticationAutoConfiguration;
import org.apereo.cas.config.CasCoreAutoConfiguration;
import org.apereo.cas.config.CasCoreLogoutAutoConfiguration;
import org.apereo.cas.config.CasCoreMultifactorAuthenticationConfiguration;
import org.apereo.cas.config.CasCoreNotificationsAutoConfiguration;
import org.apereo.cas.config.CasCoreServicesAutoConfiguration;
import org.apereo.cas.config.CasCoreTicketsAutoConfiguration;
import org.apereo.cas.config.CasCoreUtilAutoConfiguration;
import org.apereo.cas.config.CasCoreWebAutoConfiguration;
import org.apereo.cas.config.CasMultifactorAuthenticationWebflowAutoConfiguration;
import org.apereo.cas.config.CasPersonDirectoryConfiguration;
import org.apereo.cas.config.CasPersonDirectoryStubConfiguration;
import org.apereo.cas.config.CasWebflowAutoConfiguration;
import org.apereo.cas.config.FileAuthenticationEventExecutionPlanConfiguration;
import org.apereo.cas.config.GroovyAuthenticationEventExecutionPlanConfiguration;
import org.apereo.cas.config.JsonResourceAuthenticationEventExecutionPlanConfiguration;
import org.apereo.cas.config.RejectUsersAuthenticationEventExecutionPlanConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link ConfigurationTests}.
 *
 * @author Misagh Moayyed
 * @since 6.2.0
 */
@SpringBootTest(classes = {
    RefreshAutoConfiguration.class,
    WebMvcAutoConfiguration.class,
    CasCoreLogoutAutoConfiguration.class,
    CasWebflowAutoConfiguration.class,
    CasCoreNotificationsAutoConfiguration.class,
    CasCoreServicesAutoConfiguration.class,
    CasCoreWebAutoConfiguration.class,
    CasCoreMultifactorAuthenticationConfiguration.class,
    CasMultifactorAuthenticationWebflowAutoConfiguration.class,
    CasCoreAutoConfiguration.class,
    CasCoreAuthenticationAutoConfiguration.class,
    CasCoreTicketsAutoConfiguration.class,
    CasCookieAutoConfiguration.class,
    CasPersonDirectoryConfiguration.class,
    CasPersonDirectoryStubConfiguration.class,
    CasCoreUtilAutoConfiguration.class,
    FileAuthenticationEventExecutionPlanConfiguration.class,
    GroovyAuthenticationEventExecutionPlanConfiguration.class,
    JsonResourceAuthenticationEventExecutionPlanConfiguration.class,
    RejectUsersAuthenticationEventExecutionPlanConfiguration.class
}, properties = {
    "cas.authn.file.filename=classpath:authentication.txt",
    "cas.authn.groovy.location=classpath:GroovyAuthnHandler.groovy",
    "cas.authn.json.location=classpath:sample-users.json",
    "cas.authn.reject.users=one,two,three"
})
@Tag("CasConfiguration")
class ConfigurationTests {
    @Autowired
    @Qualifier("fileAuthenticationHandler")
    private AuthenticationHandler fileAuthenticationHandler;

    @Autowired
    @Qualifier("groovyResourceAuthenticationHandler")
    private AuthenticationHandler groovyResourceAuthenticationHandler;

    @Autowired
    @Qualifier("jsonResourceAuthenticationHandler")
    private AuthenticationHandler jsonResourceAuthenticationHandler;

    @Autowired
    @Qualifier("rejectUsersAuthenticationHandler")
    private AuthenticationHandler rejectUsersAuthenticationHandler;

    @Test
    void verifyOperation() throws Throwable {
        assertNotNull(fileAuthenticationHandler);
        assertNotNull(groovyResourceAuthenticationHandler);
        assertNotNull(jsonResourceAuthenticationHandler);
        assertNotNull(rejectUsersAuthenticationHandler);
    }
}
