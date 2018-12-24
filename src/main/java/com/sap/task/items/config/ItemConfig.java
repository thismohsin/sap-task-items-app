package com.sap.task.items.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


/**
 * Register, inject or override configuration in springframework, required for application.
 *
 * @author mk
 */
@Configuration
public class ItemConfig
{

	@Bean()
	public ThreadPoolTaskScheduler taskScheduler()
	{
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(2);
		taskScheduler.setThreadNamePrefix("Async-");
		return taskScheduler;
	}
}
