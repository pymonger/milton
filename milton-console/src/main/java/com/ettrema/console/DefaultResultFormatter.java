package com.ettrema.console;

import com.bradmcevoy.http.Resource;
import java.util.List;

public class DefaultResultFormatter implements ResultFormatter {

    public String format( String href, Resource r1 ) {
        StringBuilder sb = new StringBuilder();
        sb.append( "<a href=\'" ).append( href ).append( "\'>" ).append( r1.getName() ).append( "</a>" ).append( "<br/>" );
        return sb.toString();
    }

    public String begin( List<? extends Resource> list ) {
        return "";
    }

    public String end() {
        return "";
    }
}
