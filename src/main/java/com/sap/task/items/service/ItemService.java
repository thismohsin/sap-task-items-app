package com.sap.task.items.service;


import java.util.Collection;

import com.sap.task.items.cache.Cache;
import com.sap.task.items.domain.ItemDetail;


/**
 * Item Service provides ability to perform operation on {@link Cache}
 */
public interface ItemService
{
	/**
	 * Post item to data-store, cache.
	 *
	 * @param item instance of {@link ItemDetail}, not null.
	 */
	void addItem(ItemDetail item);


	/**
	 * Return the list of items posted in the last nth seconds or the list of last nth posted items.
	 *
	 * @return collection of item details.
	 */
	Collection<ItemDetail> getItems();
}
