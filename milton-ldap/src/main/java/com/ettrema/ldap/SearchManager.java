package com.ettrema.ldap;

import com.ettrema.common.LogUtils;
import com.sun.jndi.ldap.BerDecoder;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brad
 */
public class SearchManager {

	private static final Logger log = LoggerFactory.getLogger(SearchManager.class);
	/**
	 * Search threads map
	 */
	private final HashMap<LdapConnection, Map<Integer, SearchRunnable>> mapOfSearchesByConnection = new HashMap<LdapConnection, Map<Integer, SearchRunnable>>();
	private final HashMap<UUID, LdapConnection> mapOfUuids = new HashMap<UUID, LdapConnection>();

	private Map<Integer, SearchRunnable> getThreadMap(LdapConnection connection) {
		synchronized (mapOfSearchesByConnection) {
			Map<Integer, SearchRunnable> map = mapOfSearchesByConnection.get(connection);
			if (map == null) {
				map = new HashMap<Integer, SearchRunnable>();
				mapOfSearchesByConnection.put(connection, map);
			}
			return map;
		}
	}

	public void cancelAllSearches(LdapConnection aThis) {
		Map<Integer, SearchRunnable> searchThreadMap = getThreadMap(aThis);
		synchronized (searchThreadMap) {
			for (SearchRunnable searchRunnable : searchThreadMap.values()) {
				searchRunnable.abandon();
			}
		}
	}

	public void beginAsyncSearch(LdapConnection aThis, int currentMessageId, SearchRunnable searchRunnable) {
		searchRunnable.setUuid(UUID.randomUUID());
		Map<Integer, SearchRunnable> searchThreadMap = getThreadMap(aThis);
		synchronized (searchThreadMap) {
			searchThreadMap.put(currentMessageId, searchRunnable);
			mapOfUuids.put(searchRunnable.getUuid(), aThis);
		}
		Thread searchThread = new Thread(searchRunnable);
		searchThread.setName(aThis.getName() + "-Search-" + currentMessageId);
		searchThread.start();
	}

	public void search(LdapConnection aThis, SearchRunnable searchRunnable) {
		searchRunnable.run();
	}

	public void abandonSearch(LdapConnection aThis, int currentMessageId, BerDecoder reqBer) {
		int abandonMessageId = 0;
		try {
			abandonMessageId = (Integer) Ldap.PARSE_INT_WITH_TAG_METHOD.invoke(reqBer, Ldap.LDAP_REQ_ABANDON);
			Map<Integer, SearchRunnable> searchThreadMap = getThreadMap(aThis);
			synchronized (searchThreadMap) {
				SearchRunnable searchRunnable = searchThreadMap.get(abandonMessageId);
				if (searchRunnable != null) {
					searchRunnable.abandon();
					searchThreadMap.remove(currentMessageId);
				}
			}
		} catch (IllegalAccessException e) {
			log.error("", e);
		} catch (InvocationTargetException e) {
			log.error("", e);
		}
		LogUtils.debug(log, "LOG_LDAP_REQ_ABANDON_SEARCH", currentMessageId, abandonMessageId);
	}

	public void searchComplete(UUID id, Integer currentMessageId) {
		LdapConnection con = mapOfUuids.get(id);
		if (con != null) {
			Map<Integer, SearchRunnable> searchThreadMap = getThreadMap(con);
			synchronized (searchThreadMap) {
				searchThreadMap.remove(currentMessageId);
			}
		}
	}
}
