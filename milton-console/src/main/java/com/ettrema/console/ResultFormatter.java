package com.ettrema.console;

import com.bradmcevoy.http.Resource;
import java.util.List;

public interface ResultFormatter {

    String begin( List<? extends Resource> list );

    String format( String href, Resource r );

    String end();
}
