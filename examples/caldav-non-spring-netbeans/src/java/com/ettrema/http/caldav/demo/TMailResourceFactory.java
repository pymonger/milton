package com.ettrema.http.caldav.demo;

import com.ettrema.mail.MailResourceFactory;
import com.ettrema.mail.Mailbox;
import com.ettrema.mail.MailboxAddress;

/**
 * This adds email support to the CalDAV demo application
 *
 * NOTE THAT MILTON CALDAV DOES NOT DEPEND ON THE GEROA PROJECT!
 *
 * This is just here because it can be convenient to test some caldav clients
 * with an integrated email and caldav server
 *
 *
 * @author brad
 */
public class TMailResourceFactory implements MailResourceFactory{

    private final TResourceFactory resourceFactory;

    public TMailResourceFactory(TResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public Mailbox getMailbox(MailboxAddress add) {
        return resourceFactory.findUser(add.user);
    }

}
