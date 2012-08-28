package com.ettrema.proxy;

import java.net.Socket;

/** proxylogserver is a logging version of proxyserver.
Stores log files in "log' subdirectory **/
public class ProxyLogServer extends ProxyServer {

    public ProxyLogServer() {
    }

    public ProxyLogServer(String targetHost, int targetPort, int listenPort) {
        super(targetHost, targetPort, listenPort);
    }

    @Override
    protected void gotconn(Socket sconn) throws Exception {
        ProxyLogConn pc = new ProxyLogConn(sconn, getTargetHost(), getTargetPort());
        pc.go();
    }

    public static void main(String args[]) {
        String targetHost = args[0];
        Integer targetPort = new Integer(args[1]);
        Integer listenPort = new Integer(args[2]);
        ProxyLogServer us = new ProxyLogServer(targetHost, targetPort, listenPort);
        us.go();
    }
}

