package com.ettrema.http.caldav.demo;

import com.bradmcevoy.http.Resource;
import com.ettrema.mail.MessageFolder;
import com.ettrema.mail.MessageResource;
import com.ettrema.mail.StandardMessageFactoryImpl;
import com.ettrema.mail.StandardMessageImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author brad
 */
public class TMailFolder extends TFolderResource implements MessageFolder {

    private int nextMsgId = 1000;

    public TMailFolder(TFolderResource parent, String name) {
        super(parent, name);
    }

    public Collection<MessageResource> getMessages() {
        List<MessageResource> list = new ArrayList<MessageResource>();
        for( Resource r : this.children ) {
            if( r instanceof MessageResource) {
                list.add((MessageResource) r);
            }
        }
        return list;
    }

    public int numMessages() {
        return getMessages().size();
    }

    public int totalSize() {
        int size = 0;
        for( Resource r : this.children ) {
            if( r instanceof MessageResource) {
                MessageResource mr = (MessageResource) r;
                size += mr.getSize();
            }
        }
        return size;

    }

    public void storeMail(MimeMessage mm) {
        StandardMessageFactoryImpl factoryImpl = new StandardMessageFactoryImpl();
        StandardMessageImpl sm = new StandardMessageImpl();
        factoryImpl.toStandardMessage(mm, sm);
        TMessageResource mr = new TMessageResource(this,"msg-" + nextMsgId++, sm);
        System.out.println("stored message in: " + mr.getName());
    }

}
