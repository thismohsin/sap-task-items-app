package com.sap.task.items.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.sap.task.items.manager.CacheManager;


/**
 * Register, inject or override configuration in springframework, required for application.
 *
 * @author mk
 */
@Configuration
public class ItemConfig
{

	/**
	 * Configuring threads pool executor for applicatiion.
	 * {@link CacheManager} will use this thread pool executor to spawn cache cleanup task.
	 *
	 * @return thread pool executor
	 */
	@Bean()
	public ThreadPoolTaskScheduler taskScheduler()
	{
		final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(2);
		taskScheduler.setThreadNamePrefix("Async-");
		return taskScheduler;
	}
}
