package com.sap.task.items.service;


import java.time.ZonedDateTime;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sap.task.items.cache.CacheImpl;
import com.sap.task.items.domain.ItemDetail;
import com.sap.task.items.util.TestUtil;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public final class ItemServiceTest
{
	private ItemService service;

	/**
	 * This inject actual {@link CacheImpl} implementation rather mock.
	 * As part of this test will invoke underlying cache for verification and assertion.
	 */
	@InjectMocks
	private CacheImpl cache;


	@Test
	public void shouldReturnLastPosted4ItemsGivenThatThereWereOnlyTwoRequestInLastTwoSeconds()
	{
		final Integer Nth_ELEMENT_THRESHOLD = 4; 	// last posted 4 items.
		final long ELEMENT_IN_LAST_Nth_SECONDS = 2;	//consider item in last two seconds.
		final Integer MAXIMUM_ITEM_SIZE = 5;		// total item added in cache.
		submitRequest(Nth_ELEMENT_THRESHOLD, ELEMENT_IN_LAST_Nth_SECONDS, MAXIMUM_ITEM_SIZE);
		assertThat(service.getItems().size(), equalTo(Nth_ELEMENT_THRESHOLD));
	}


	@Test
	public void shouldReturnItemFromLastFourSecondsGivenThatLastPostedTwoItemHasLowerThreshold()
	{
		final Integer Nth_ELEMENT_THRESHOLD = 2; 	//last posted 2 items.
		final long ELEMENT_IN_LAST_Nth_SECONDS = 4;	//consider item in last 4 seconds.
		final Integer MAXIMUM_ITEM_SIZE = 5; 		//total item added in cache.
		submitRequest(Nth_ELEMENT_THRESHOLD, ELEMENT_IN_LAST_Nth_SECONDS, MAXIMUM_ITEM_SIZE);
		assertEquals(service.getItems().size(), ELEMENT_IN_LAST_Nth_SECONDS);
	}


	private void submitRequest(final Integer nthElementThreshold, final long elementInLastNthSeconds, final Integer maximumItemSize)
	{
		service = new ItemServiceImpl(cache, nthElementThreshold, elementInLastNthSeconds);
		IntStream.range(0, maximumItemSize).forEach(index -> {
			final ItemDetail itemDetail = TestUtil.item(index, ZonedDateTime.now().minusSeconds(index));
			service.addItem(itemDetail);
		});
	}
}
