package org.apereo.cas.webflow;

import org.apereo.cas.authentication.CoreAuthenticationTestUtils;
import org.apereo.cas.authentication.DefaultAuthenticationResultBuilder;
import org.apereo.cas.authentication.PrincipalElectionStrategy;
import org.apereo.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.apereo.cas.config.CasCookieAutoConfiguration;
import org.apereo.cas.config.CasCoreAuditAutoConfiguration;
import org.apereo.cas.config.CasCoreAuthenticationAutoConfiguration;
import org.apereo.cas.config.CasCoreAutoConfiguration;
import org.apereo.cas.config.CasCoreLogoutAutoConfiguration;
import org.apereo.cas.config.CasCoreMultifactorAuthenticationConfiguration;
import org.apereo.cas.config.CasCoreNotificationsAutoConfiguration;
import org.apereo.cas.config.CasCoreServicesAutoConfiguration;
import org.apereo.cas.config.CasCoreTicketsAutoConfiguration;
import org.apereo.cas.config.CasCoreUtilAutoConfiguration;
import org.apereo.cas.config.CasCoreValidationAutoConfiguration;
import org.apereo.cas.config.CasCoreWebAutoConfiguration;
import org.apereo.cas.config.CasFiltersConfiguration;
import org.apereo.cas.config.CasLoggingAutoConfiguration;
import org.apereo.cas.config.CasMultifactorAuthenticationWebflowAutoConfiguration;
import org.apereo.cas.config.CasPersonDirectoryConfiguration;
import org.apereo.cas.config.CasPersonDirectoryStubConfiguration;
import org.apereo.cas.config.CasPropertiesConfiguration;
import org.apereo.cas.config.CasSupportActionsConfiguration;
import org.apereo.cas.config.CasThemesConfiguration;
import org.apereo.cas.config.CasThymeleafConfiguration;
import org.apereo.cas.config.CasWebAppConfiguration;
import org.apereo.cas.config.CasWebflowAutoConfiguration;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.util.MockRequestContext;
import org.apereo.cas.util.function.FunctionUtils;
import org.apereo.cas.web.flow.actions.BaseCasWebflowAction;
import org.apereo.cas.web.support.WebUtils;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.executor.FlowExecutor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListSet;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link BaseCasWebflowSessionContextConfigurationTests}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@SpringBootTest(classes = {
    AopAutoConfiguration.class,
    RefreshAutoConfiguration.class,
    WebMvcAutoConfiguration.class,
    CasWebflowAutoConfiguration.class,
    CasThemesConfiguration.class,
    CasThymeleafConfiguration.class,
    CasFiltersConfiguration.class,
    CasPropertiesConfiguration.class,
    CasWebAppConfiguration.class,
    BaseCasWebflowSessionContextConfigurationTests.TestWebflowContextConfiguration.class,
    CasMultifactorAuthenticationWebflowAutoConfiguration.class,
    CasCoreServicesAutoConfiguration.class,
    CasCoreAuthenticationAutoConfiguration.class,
    CasCoreTicketsAutoConfiguration.class,
    CasLoggingAutoConfiguration.class,
    CasSupportActionsConfiguration.class,
    CasCoreUtilAutoConfiguration.class,
    CasCoreLogoutAutoConfiguration.class,
    CasCookieAutoConfiguration.class,
    CasCoreWebAutoConfiguration.class,
    CasCoreValidationAutoConfiguration.class,
    CasCoreAutoConfiguration.class,
    CasCoreAuditAutoConfiguration.class,
    CasCoreNotificationsAutoConfiguration.class,
    CasPersonDirectoryConfiguration.class,
    CasPersonDirectoryStubConfiguration.class,
    CasCoreMultifactorAuthenticationConfiguration.class
}, properties = "spring.main.allow-bean-definition-overriding=true")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@EnableAspectJAutoProxy(proxyTargetClass = false)
public abstract class BaseCasWebflowSessionContextConfigurationTests {

    @Test
    void verifyExecutorsAreBeans() {
        assertNotNull(getFlowExecutor());
    }

    @Test
    void verifyFlowExecutorByClient() throws Throwable {
        val context = MockRequestContext.create().withLocale(Locale.ENGLISH);
        val map = new LocalAttributeMap<>();
        getFlowExecutor().launchExecution("login", map, context.getExternalContext());
    }

    public abstract FlowExecutor getFlowExecutor();

    /**
     * The type Test webflow context configuration.
     */
    @TestConfiguration(value = "testWebflowContextConfiguration", proxyBeanMethods = false)
    static class TestWebflowContextConfiguration {
        private static final String TEST = "test";

        @Autowired
        @Qualifier(PrincipalElectionStrategy.BEAN_NAME)
        private ObjectProvider<PrincipalElectionStrategy> principalElectionStrategy;

        @Bean
        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
        public Action testWebflowSerialization() {
            //CHECKSTYLE:OFF
            return new BaseCasWebflowAction() {
                @Override
                protected Event doExecuteInternal(final RequestContext requestContext) {
                    val flowScope = requestContext.getFlowScope();
                    flowScope.put("test", TEST);
                    flowScope.put("test0", Collections.singleton(TEST));
                    flowScope.put("test1", List.of(TEST));
                    flowScope.put("test2", Collections.singletonMap(TEST, TEST));
                    flowScope.put("test3", Arrays.asList(TEST, TEST));
                    flowScope.put("test4", new ConcurrentSkipListSet());
                    flowScope.put("test5", List.of("test1"));
                    flowScope.put("test6", Collections.unmodifiableSet(Collections.singleton(1)));
                    flowScope.put("test7", Collections.unmodifiableMap(new HashMap<>()));
                    flowScope.put("test8", Collections.emptyMap());
                    flowScope.put("test9", new TreeMap<>());
                    flowScope.put("test10", Collections.emptySet());
                    flowScope.put("test11", Collections.emptyList());

                    val service = new SimpleWebApplicationServiceImpl();
                    service.setId(CoreAuthenticationTestUtils.CONST_TEST_URL);
                    service.setOriginalUrl(CoreAuthenticationTestUtils.CONST_TEST_URL);
                    service.setArtifactId(null);

                    return FunctionUtils.doUnchecked(() -> {
                        val authentication = CoreAuthenticationTestUtils.getAuthentication();
                        val authenticationResultBuilder = new DefaultAuthenticationResultBuilder();
                        val principal = CoreAuthenticationTestUtils.getPrincipal();
                        authenticationResultBuilder.collect(authentication);
                        authenticationResultBuilder.collect(CoreAuthenticationTestUtils.getCredentialsWithSameUsernameAndPassword());
                        val authenticationResult = authenticationResultBuilder.build(principalElectionStrategy.getObject(), service);

                        WebUtils.putAuthenticationResultBuilder(authenticationResultBuilder, requestContext);
                        WebUtils.putAuthenticationResult(authenticationResult, requestContext);
                        WebUtils.putPrincipal(requestContext, principal);

                        return success();
                    });
                }
            };
            //CHECKSTYLE:ON
        }
    }
}
