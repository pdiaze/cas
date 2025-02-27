package org.apereo.cas.web.support;

import org.apereo.cas.throttle.ThrottledSubmissionHandlerEndpoint;
import org.apereo.cas.web.report.AbstractCasEndpointTests;
import lombok.val;
import org.apereo.inspektr.common.web.ClientInfo;
import org.apereo.inspektr.common.web.ClientInfoHolder;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link ThrottledSubmissionHandlerEndpointTests}.
 *
 * @author Misagh Moayyed
 * @since 6.3.0
 */
@TestPropertySource(properties = {
    "cas.authn.throttle.failure.range-seconds=5",
    "management.endpoint.throttles.access=UNRESTRICTED"
})
@Import(BaseThrottledSubmissionHandlerInterceptorAdapterTests.SharedTestConfiguration.class)
@Tag("ActuatorEndpoint")
class ThrottledSubmissionHandlerEndpointTests extends AbstractCasEndpointTests {
    @Autowired
    @Qualifier("throttledSubmissionHandlerEndpoint")
    private ThrottledSubmissionHandlerEndpoint throttledSubmissionHandlerEndpoint;

    @Autowired
    @Qualifier(ThrottledSubmissionHandlerInterceptor.BEAN_NAME)
    private ThrottledSubmissionHandlerInterceptor throttle;

    @Test
    void verifyOperation() {
        assertTrue(throttledSubmissionHandlerEndpoint.getRecords().isEmpty());

        val request = new MockHttpServletRequest();
        request.setRemoteAddr("1.2.3.4");
        request.setLocalAddr("4.5.6.7");
        request.setRemoteUser("cas");
        request.addHeader(HttpHeaders.USER_AGENT, "Firefox");
        ClientInfoHolder.setClientInfo(ClientInfo.from(request));

        throttle.recordSubmissionFailure(request);
        val records = throttledSubmissionHandlerEndpoint.getRecords();
        assertFalse(records.isEmpty());
        throttledSubmissionHandlerEndpoint.deleteByKeyOrRelease(false, records.getFirst().getKey());
        assertDoesNotThrow(() -> throttledSubmissionHandlerEndpoint.deleteByKeyOrRelease(false, null));
        assertDoesNotThrow(() -> throttledSubmissionHandlerEndpoint.deleteByKeyOrRelease(true, null));
    }
}
