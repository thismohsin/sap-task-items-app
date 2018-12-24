package com.sap.task.items.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyInt;


import com.sap.task.items.cache.Cache;


@RunWith(MockitoJUnitRunner.class)
public final class CacheManagerTest
{
	@InjectMocks
	private CacheManager manager;

	@Mock
	private Cache cache;


	@Test
	public void shouldNotInvokeEvictionWhenMemoryUsageIsNotHigh()
	{
		final Integer highMemoryUsageThreshold = 85;
		final Integer percentageOfCacheEviction = 25;
		manager = new CacheManager(cache, highMemoryUsageThreshold, percentageOfCacheEviction);
		manager.evictionPolicy();
		verify(cache, times(0)).evict(anyInt());
	}


	@Test
	public void shouldInvokeEvictionWhenMemoryUsageIsHigh()
	{
		final Integer highMemoryUsageThreshold = 1;
		final Integer percentageOfCacheEviction = 25;
		manager = new CacheManager(cache, highMemoryUsageThreshold, percentageOfCacheEviction);
		when(cache.size()).thenReturn(100);
		manager.evictionPolicy();
		verify(cache, times(1)).evict(anyInt());
	}
}
