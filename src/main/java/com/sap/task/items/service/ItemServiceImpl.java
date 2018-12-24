package com.sap.task.items.service;


import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sap.task.items.cache.Cache;
import com.sap.task.items.domain.ItemDetail;


/**
 * This provide interface to work with underlying cache.
 * Class encapsulate the business logic for handling cache response.
 *
 * @author mk
 */
@Service
public final class ItemServiceImpl implements ItemService
{
	private final Cache cache;

	private final Integer thresholdNthElement;

	private final long lastNthDurationInSeconds;


	@Autowired
	public ItemServiceImpl(final Cache cache,
						   @Value("${item.response.minimum.elements:100}") final Integer thresholdNthElement,
						   @Value("${item.response.last.nth.seconds:1}") final long lastNthDurationInSeconds)
	{
		this.cache = cache;
		this.thresholdNthElement = thresholdNthElement;
		this.lastNthDurationInSeconds = lastNthDurationInSeconds;
	}


	@Override
	public void addItem(final ItemDetail itemDetail)
	{
		cache.put(itemDetail);
	}


	@Override
	public Collection<ItemDetail> getItems()
	{
		final Map<ZonedDateTime, ItemDetail> durationMap = lastNthSecondPostedItem(lastNthDurationInSeconds);
		final int sizeOfDurationMap = durationMap.size();
		final int sizeOfItemMap = cache.size();
		final Boolean criteria = checkResponseCriteria(sizeOfDurationMap, sizeOfItemMap, thresholdNthElement);
		final Map<ZonedDateTime, ItemDetail> resultMap = criteria ? lastNthPostedItem(thresholdNthElement) : durationMap;
		return resultMap.values();
	}


	/**
	 * Retrieve item posted in last nth second.
	 *
	 * @param seconds unit of time, not null.
	 * @return submap from item cache.
	 */
	private Map<ZonedDateTime, ItemDetail> lastNthSecondPostedItem(final long seconds)
	{
		final ZonedDateTime zonedDateTime = cache.lastNthSecondsKey(seconds);
		return cache.subMap(zonedDateTime, false);
	}


	/**
	 * Retrieve list of last nth posted item.
	 *
	 * @param nthElement configured threshold, for nth element posted.
	 * @return submap from itemcache.
	 */
	private Map<ZonedDateTime, ItemDetail> lastNthPostedItem(final Integer nthElement)
	{
		final ZonedDateTime zonedDateTime = cache.nthElementKey(nthElement);
		return cache.subMap(zonedDateTime, true);
	}


	/**
	 * Evaluate the criteria for the list of items posted in the last nth seconds or the list of last nth posted items.
	 *
	 * @param sizeOfDurationMap count of item posted in last nth second
	 * @param sizeOfItemMap count of item in ItemCache
	 * @param thresholdNthElement configured threshold, count of last nth posted item.
	 * @return decision to return item by last nth seconds or last nth posted item.
	 */
	private Boolean checkResponseCriteria(final Integer sizeOfDurationMap, final Integer sizeOfItemMap, final Integer thresholdNthElement)
	{
		Boolean consider = Boolean.FALSE;
		if (sizeOfDurationMap < thresholdNthElement && sizeOfItemMap > sizeOfDurationMap)
		{
			consider = Boolean.TRUE;
		}
		return consider;
	}

}
