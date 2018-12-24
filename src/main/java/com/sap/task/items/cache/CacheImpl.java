package com.sap.task.items.cache;


import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.stereotype.Repository;

import com.sap.task.items.domain.ItemDetail;


/**
 * This is light-weight, thread safe Item Cache {@link CacheImpl} implemented using {@link ConcurrentSkipListMap}
 * Maps implements {@link ConcurrentNavigableMap}, gives navigational ability required for our use-case.
 *
 * When object is added to Item Cache, the key {@link ZonedDateTime} will inserted in descending order.
 * By doing this it gives us ability to read or navigate faster using {@link java.util.NavigableMap}.
 * [zdt(n), zdt(n-1), zdt(n-1)... zd(n)]
 *
 * @author mk
 */

@Repository
public final class CacheImpl implements Cache
{
	/**
	 * Item cache, ordering key in descending while insertion. [zdt5, zdt4, zdt3... zdt1]
	 * Note: I have used Spring Constructor Autowiring for registering all beans.
	 * This ensures that the spring bean dependency object graph will initialize this first and once, hence not static.
	 */
	private  final ConcurrentSkipListMap<ZonedDateTime, ItemDetail> ITEMS =
			new ConcurrentSkipListMap<>((zdt1, zdt2) -> Long.compare(zdt2.toInstant().toEpochMilli(), zdt1.toInstant().toEpochMilli()));


	/**
	 * Instance of item map.
	 *
	 * @return cache instance.
	 */
	private ConcurrentSkipListMap<ZonedDateTime, ItemDetail> items()
	{
		return ITEMS;
	}


	@Override
	public void put(final ItemDetail itemDetail)
	{
		items().put(itemDetail.getItem().getTimestamp(), itemDetail);
	}


	@Override
	public ConcurrentNavigableMap<ZonedDateTime, ItemDetail> subMap(final ZonedDateTime zdt, final boolean inclusive)
	{
		return items().headMap(zdt, inclusive);
	}


	@Override
	public ZonedDateTime lastNthSecondsKey(final long seconds)
	{
		return ZonedDateTime.now()
					   .minusSeconds(seconds);
	}


	@Override
	public ZonedDateTime nthElementKey(Integer nthElement)
	{
		ZonedDateTime zdt = null;
		for (final ZonedDateTime dateTime : items().navigableKeySet())
		{
			if (nthElement == 0)
			{
				break;
			}
			zdt = dateTime;
			nthElement = nthElement - 1;
		}
		return zdt;
	}


	@Override
	public Integer size()
	{
		return items().size();
	}


	@Override
	public void evict(Integer nthElement)
	{
		for (final ZonedDateTime zdt : items().descendingKeySet())
		{
			if (nthElement == 0)
			{
				break;
			}
			items().remove(zdt);
			nthElement = nthElement - 1;
		}
	}

}