package com.sap.task.items.cache;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.sap.task.items.domain.ItemDetail;


/**
 * This is light-weight, thread safe Item Cache {@link CacheImpl} implemented using {@link ConcurrentSkipListMap}
 * ConcurrentSkipListMap has complexity of O(log(n)), supports for concurrency and implements {@link ConcurrentNavigableMap}
 * that gives more navigational ability required for our use-case.
 *
 * When object is added to Item Cache, the key {@link ZonedDateTime} will inserted in descending order.
 * By doing this it gives us ability to read or navigate faster using {@link java.util.NavigableMap}.
 * [zdt(n), zdt(n-1), zdt(n-1)... zd(n)]
 *
 * @author mk
 */
public interface Cache
{
	/**
	 * Put item in cache.
	 *
	 * @param itemDetail instance of {@link ItemDetail}, not null.
	 */
	void put(ItemDetail itemDetail);


	/**
	 * Evict last N item from cache.
	 *
	 * @param nthElement key, last N element to be removed, not null.
	 */
	void evict(Integer nthElement);


	/**
	 * Return all element from having key value greater then given {@link ZonedDateTime}.
	 *
	 * @param zdt ZonedDateTime used as key, not null.
	 * @param inclusive whether to include this item in resultant map.
	 * @return headMap, element having key greater then ZonedDateTime.
	 */
	Map<ZonedDateTime, ItemDetail> subMap(ZonedDateTime zdt, boolean inclusive);


	/**
	 * Iterates through map key to return key for last nth seconds.
	 *
	 * @param seconds ZoneDateTime key for that seconds, not null.
	 * @return ZonedDateTime, key for that last nth second.
	 */
	ZonedDateTime lastNthSecondsKey(long seconds);


	/**
	 * Iterates through map keys to return key for given index or nthElement.
	 *
	 * @param nthElement ZoneDateTime key for given index or nthElement, not null.
	 * @return ZonedDateTime, key for given index or nthElement.
	 */
	ZonedDateTime nthElementKey(Integer nthElement);


	/**
	 * Size of Item Cache.
	 *
	 * @return size of item cache.
	 */
	Integer size();
}
