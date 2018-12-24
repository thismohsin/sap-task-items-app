package com.sap.task.items.util;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.sap.task.items.domain.Item;
import com.sap.task.items.domain.ItemDetail;


public final class TestUtil
{

	public static ItemDetail item(final Integer id, final ZonedDateTime zonedDateTime)
	{
		ItemDetail itemDetail = new ItemDetail();
		Item item = new Item();
		item.setId(id);
		zonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
		item.setTimestamp(zonedDateTime);
		itemDetail.setItem(item);
		return itemDetail;
	}


	public static ItemDetail item(final Integer id)
	{
		return item(id, ZonedDateTime.now());
	}
}
