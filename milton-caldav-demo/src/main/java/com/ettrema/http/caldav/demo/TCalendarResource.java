package com.ettrema.http.caldav.demo;

import com.bradmcevoy.http.Resource;
import com.bradmcevoy.io.StreamUtils;
import com.ettrema.http.CalendarResource;
import com.bradmcevoy.http.ReportableResource;
import com.ettrema.http.caldav.ICalFormatter;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A calendar resource is really just a folder which knows how to create ICal
 * resources.
 *
 * @author brad
 */
public class TCalendarResource extends TFolderResource implements CalendarResource, ReportableResource {

    private static final Logger log = LoggerFactory.getLogger(TCalendarResource.class);

    private String color = "#2952A3";
    
    public TCalendarResource(TFolderResource parent, String name) {
        super(parent, name);
    }

    @Override
    protected Object clone(TFolderResource newParent) {
        return new TCalendarResource(newParent, name);
    }

    @Override
    public Resource createNew(String newName, InputStream inputStream, Long length, String contentType) throws IOException {
        log.debug("createNew: " + contentType);
//        if (contentType.startsWith("text/calendar")) {
            TEvent e = new TEvent(this, newName);
            log.debug("created tevent: " + e.name);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            StreamUtils.readTo(inputStream, bout);
            bout.close();
            String data = bout.toString();
            e.setiCalData(data);
            return e;
//        } else {
//            throw new RuntimeException("eek");
//            //log.debug( "creating a normal resource");
//            //return super.createNew( newName, inputStream, length, contentType );
//        }
    }

	@Override
    public String getCalendarDescription() {
        return "A test calendar";
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(String s) {
        this.color = s;
    }

}
