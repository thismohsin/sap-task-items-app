package com.sap.task.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Initialization of Item Cache Application.
 *
 * @author mk
 */
@SpringBootApplication
@EnableScheduling
public class ItemCacheApplication
{

	public static void main(final String[] args)
	{
		SpringApplication.run(ItemCacheApplication.class, args);
	}

}
