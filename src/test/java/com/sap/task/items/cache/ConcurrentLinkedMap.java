package com.sap.task.items.cache;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import com.sap.task.items.domain.ItemDetail;


/**
 * This is alternate standalone implementation using {@link ConcurrentHashMap} and {@link LinkedList}
 * This class can be put to test using {@link CacheTest} by changing implementation instance to this class.
 *
 * @author mk
 */
public final class ConcurrentLinkedMap implements Cache
{
	private final Integer INITIAL_CAPACITY_SIZE = 500;
	private final ConcurrentHashMap<ZonedDateTime, ItemDetail> ITEMS = new ConcurrentHashMap<>(INITIAL_CAPACITY_SIZE);
	private final LinkedList<ZonedDateTime> ORDERED_KEY = new LinkedList<>();


	private Map<ZonedDateTime, ItemDetail> items()
	{
		return ITEMS;
	}


	private LinkedList<ZonedDateTime> orderedKeys()
	{
		return ORDERED_KEY;
	}


	@Override
	public void put(final ItemDetail itemDetail)
	{
		handleCapacity();
		final ZonedDateTime zdtKey = itemDetail.getItem().getTimestamp();
		if (!items().containsKey(zdtKey))
		{
			items().put(zdtKey, itemDetail);
			orderedKeys().add(zdtKey);
		}
	}


	@Override
	public Map<ZonedDateTime, ItemDetail> subMap(final ZonedDateTime zdtKey, final boolean inclusive)
	{
		Map<ZonedDateTime, ItemDetail> result = new HashMap<>();
		for (ZonedDateTime zdt : orderedKeys())
		{
			final ItemDetail eachItem = items().get(zdt);
			if (zdt.isAfter(zdtKey))
			{
				result.put(zdt, eachItem);
			}
			else if (zdt.isEqual(zdtKey) && inclusive)
			{
				result.put(zdt, eachItem);
			}
			else if (zdt.isBefore(zdtKey))
			{
				break;
			}
		}
		return result;
	}


	@Override
	public void evict(Integer nthElement)
	{
		IntStream.range(0, nthElement).forEach(index -> {
			final ZonedDateTime zdt = orderedKeys().pollLast();
			items().remove(zdt);
		});
	}


	public Map<ZonedDateTime, ItemDetail> lastNthSecondsPostedItem(final long seconds)
	{
		final ZonedDateTime zonedDateTime = lastNthSecondsKey(seconds);
		return subMap(zonedDateTime, false);
	}


	public Map<ZonedDateTime, ItemDetail> lastNthPostedItem(final Integer nthElement)
	{
		final ZonedDateTime zonedDateTime = nthElementKey(nthElement);
		return subMap(zonedDateTime, true);
	}


	@Override
	public Integer size()
	{
		return items().size();
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
		return orderedKeys().get(nthElement - 1);
	}


	private void handleCapacity(){
		for( int index = items().size(); index > INITIAL_CAPACITY_SIZE; index--  ){
			final ZonedDateTime zdt = orderedKeys().pollLast();
			items().remove(zdt);
		}
	}
}
