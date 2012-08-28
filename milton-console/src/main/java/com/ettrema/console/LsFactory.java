package com.ettrema.console;

import com.bradmcevoy.http.Auth;
import com.ettrema.console.ResultFormatter;
import java.util.List;

public class LsFactory extends AbstractConsoleCommandFactory {

    private final ResultFormatter resultFormatter;

    public LsFactory() {
        this.resultFormatter = new DefaultResultFormatter();
    }

    public LsFactory( ResultFormatter resultFormatter ) {
        this.resultFormatter = resultFormatter;
    }



    @Override
    public String[] getCommandNames() {
        return new String[]{"ls"};
    }

    @Override
    public ConsoleCommand create( List<String> args, String host, String currentDir, Auth auth ) {
        return new Ls( args, host, currentDir, consoleResourceFactory, resultFormatter );
    }

    @Override
    public String getDescription() {
        return "List. List contents of the current or a specified directory";
    }
}
