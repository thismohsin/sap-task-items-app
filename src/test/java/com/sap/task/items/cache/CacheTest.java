package com.sap.task.items.cache;


import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.Test;

import com.sap.task.items.domain.ItemDetail;
import com.sap.task.items.util.TestUtil;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;


public final class CacheTest
{
	private final Cache cache = new CacheImpl();

	private final Integer MAXIMUM_ITEM_SIZE = 5;
	private final Integer Nth_ELEMENT_THRESHOLD = 4;
	private final long ELEMENT_IN_LAST_Nth_SECONDS = 2;
	private final Integer NUMBER_OF_OLD_ELEMENTS_TO_REMOVE = 2;


	@Test
	public void cacheTest()
	{
		shouldPutItemInCache();
		shouldReturnLastNthSecondPostedItem();
		shouldReturnLastNthPostedItem();
		shouldEvictOldItemsFromCache();
	}


	public void shouldPutItemInCache()
	{
		IntStream.range(0, MAXIMUM_ITEM_SIZE).forEach(index -> {
			ItemDetail itemDetail = TestUtil.item(index, ZonedDateTime.now().minusSeconds(index));
			cache.put(itemDetail);
		});

		assertThat(cache.size(), equalTo(MAXIMUM_ITEM_SIZE));
	}


	public void shouldReturnLastNthSecondPostedItem()
	{
		final ZonedDateTime zdtPivot = cache.lastNthSecondsKey(ELEMENT_IN_LAST_Nth_SECONDS);
		final Map<ZonedDateTime, ItemDetail> durationMap = cache.subMap(zdtPivot, false);
		final long expectedMapSize = durationMap.entrySet().stream().filter(entry -> entry.getKey().isAfter(zdtPivot)).count();

		assertEquals(durationMap.size(), expectedMapSize);
	}


	public void shouldReturnLastNthPostedItem()
	{
		final ZonedDateTime zdtPivot = cache.nthElementKey(Nth_ELEMENT_THRESHOLD);
		final Map<ZonedDateTime, ItemDetail> nthElementsMap = cache.subMap(zdtPivot, true);

		assertThat(nthElementsMap.size(), equalTo(Nth_ELEMENT_THRESHOLD));
	}


	public void shouldEvictOldItemsFromCache()
	{
		Integer beforeSize = cache.size();
		cache.evict(NUMBER_OF_OLD_ELEMENTS_TO_REMOVE);

		assertThat(cache.size(), equalTo(beforeSize - NUMBER_OF_OLD_ELEMENTS_TO_REMOVE));
	}

}
