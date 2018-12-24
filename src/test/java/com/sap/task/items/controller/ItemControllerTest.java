package com.sap.task.items.controller;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import com.sap.task.items.domain.ItemDetail;
import com.sap.task.items.service.ItemService;
import com.sap.task.items.util.TestUtil;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@RunWith(MockitoJUnitRunner.class)
public final class ItemControllerTest
{
	@InjectMocks
	private ItemController controller;

	@Mock
	private ItemService service;


	@Test
	public void shouldAddItemToCache()
	{
		final ItemDetail item = TestUtil.item(1);
		controller.addItem(item);
		verify(service, times(1)).addItem(item);
	}


	@Test
	public void shouldReturnListOfCacheItems()
	{
		final ItemDetail itemDetail = TestUtil.item(1);
		when(service.getItems()).thenReturn(Arrays.asList(itemDetail));
		assertThat(controller.getItems(), containsInAnyOrder(itemDetail));
	}
}
