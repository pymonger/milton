package com.ettrema.http.caldav.demo;

import com.ettrema.http.SchedulingInboxResource;

/**
 *
 * @author brad
 */
public class TScheduleInboxResource extends TFolderResource implements SchedulingInboxResource {

    public TScheduleInboxResource(TFolderResource parent, String name) {
        super(parent, name);
    }


}
