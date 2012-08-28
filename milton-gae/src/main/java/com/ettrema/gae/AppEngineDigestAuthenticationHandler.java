package com.ettrema.gae;

import com.bradmcevoy.http.http11.auth.DigestAuthenticationHandler;

/**
 * Convenience subclass of DigestAuthenticationHandler which just creates
 * a AppEngineMemcacheNonceProvider as the default nonce provider.
 *
 * Note that if you want to configure the AppEngineMemcacheNonceProvider you
 * should just create a DigestAuthenticationHandler and pass it the configured
 * instance of AppEngineMemcacheNonceProvider.
 *
 * @author Scott Hernandez
 */
public class AppEngineDigestAuthenticationHandler extends DigestAuthenticationHandler {
	public AppEngineDigestAuthenticationHandler(){
		super(new AppEngineMemcacheNonceProvider(3600));
	}
}
