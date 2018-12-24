package com.sap.task.items.it;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.sap.task.items.ItemCacheApplication;
import com.sap.task.items.cache.Cache;
import com.sap.task.items.cache.CacheImpl;
import com.sap.task.items.controller.ItemController;
import com.sap.task.items.domain.ItemDetail;
import com.sap.task.items.service.ItemService;
import com.sap.task.items.service.ItemServiceImpl;
import com.sap.task.items.util.TestUtil;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = {ItemController.class, ItemService.class, Cache.class,
				   ItemServiceImpl.class, CacheImpl.class, ItemCacheApplication.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemServiceIntegrationTest
{

	@LocalServerPort
	private int port;

	private URL base;

	@Autowired
	private TestRestTemplate template;


	@Before
	public void setUp() throws Exception
	{
		base = new URL("http://localhost:" + port + "/items");
	}


	private List<ItemDetail> itemInstances()
	{
		return Arrays.asList(TestUtil.item(101));
	}


	@Test
	public void shouldAddItemsToCache()
	{
		itemInstances().forEach(item -> {
			HttpEntity<ItemDetail> request = new HttpEntity<>(item);
			ResponseEntity<String> result = template.postForEntity(base.toString(), request, String.class);
			assertThat(201, equalTo(result.getStatusCodeValue()));
		});
	}


	@Test
	public void shouldGetItemsFromCache()
	{
		ItemDetail[] items = template.getForObject(base.toString(), ItemDetail[].class);
		assertThat(items.length, equalTo(itemInstances().size()));
	}
}
