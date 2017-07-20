package org.apereo.cas.configuration.model.core.ticket;

/**
 * This is {@link ServiceTicketProperties}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
public class ServiceTicketProperties {

    /**
     * Controls number of times a service ticket can be used within CAS server. Usage in CAS context means service ticket validation
     * transaction.
     */
    private int numberOfUses = 1;

    /**
     * Time in seconds that service tickets should be considered live in CAS server.
     */
    private long timeToKillInSeconds = 10;

    /**
     * Maximum length of generated service tickets.
     */
    private int maxLength = 20;

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(final int maxLength) {
        this.maxLength = maxLength;
    }
    
    public int getNumberOfUses() {
        return numberOfUses;
    }

    public void setNumberOfUses(final int numberOfUses) {
        this.numberOfUses = numberOfUses;
    }

    public long getTimeToKillInSeconds() {
        return timeToKillInSeconds;
    }

    public void setTimeToKillInSeconds(final long timeToKillInSeconds) {
        this.timeToKillInSeconds = timeToKillInSeconds;
    }
}
