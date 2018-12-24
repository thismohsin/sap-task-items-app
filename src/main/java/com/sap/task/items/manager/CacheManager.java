package com.sap.task.items.manager;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sap.task.items.cache.Cache;


/**
 * This cache component is responsible to cleanup cache item.
 * Thread is invoked after every 5 minutes to evaluates memory foot print of application.
 * If the routine considers the memory usage threshold is exceeding 80% then the thread
 * will evict element from cache as per the configured percentage of total element.
 * Since cache maintain order of insertion then removal will be ordered in FIFO
 * i.e, recently added item will be on top and oldest item would be removed.
 *
 * @author mk
 */
@Component
public final class CacheManager
{

	private final Cache cache;

	private final Integer highMemoryUsageThreshold;

	private final Integer percentageOfCacheEviction;


	@Autowired
	public CacheManager(final Cache cache,
						@Value("${item.memory.usage.threshold:80}") final Integer highMemoryUsageThreshold,
						@Value("${item.cache.eviction.percentage:25}") final Integer percentageOfCacheEviction)
	{
		this.cache = cache;
		this.highMemoryUsageThreshold = highMemoryUsageThreshold;
		this.percentageOfCacheEviction = percentageOfCacheEviction;
	}

	/**
	 * Spring scheduler will invoke this task after every 5 min.
	 */
	@Scheduled(fixedDelay = 300000)
	public void evictionPolicy()
	{
		if (evictionRule())
		{
			cache.evict(pointOfEvictPartition());
		}
	}


	/**
	 * Evaluates memory usage.
	 *
	 * @return decision whether to clean up cache.
	 */
	private Boolean evictionRule()
	{
		Boolean isMemUsageHigh = Boolean.FALSE;
		final long totalMemory = Runtime.getRuntime().totalMemory();
		final long freeMemory = Runtime.getRuntime().freeMemory();
		final long usedMemory = totalMemory - freeMemory;
		final long usedMemoryPercentage = Math.round(((usedMemory * 1.0) / totalMemory) * 100);

		if (usedMemoryPercentage > highMemoryUsageThreshold)
		{
			isMemUsageHigh = Boolean.TRUE;
		}
		return isMemUsageHigh;
	}


	/**
	 * Total Number of element to be removed based on configured percentage.
	 *
	 * @return number of item to evict from cache.
	 */
	private Integer pointOfEvictPartition()
	{
		return Math.round(percentageOfCacheEviction * cache.size() / 100);
	}


}
