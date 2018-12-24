package com.sap.task.items.controller;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sap.task.items.domain.ItemDetail;
import com.sap.task.items.service.ItemService;


/**
 * Item controller process item event information.
 * Provide Rest API to post item or pull items from cache.
 *
 * @author mk
 */

@RestController
@RequestMapping("/items")
public class ItemController
{
	private final ItemService service;


	@Autowired
	public ItemController(final ItemService service)
	{
		this.service = service;
	}


	/**
	 * Post item to data-store, cache.
	 *
	 * @param item instance of {@link ItemDetail}, not null.
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addItem(@RequestBody final ItemDetail item)
	{
		service.addItem(item);
	}


	/**
	 * Return the list of items posted in the last nth seconds or the list of last nth posted items.
	 *
	 * @return collection of item details.
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<ItemDetail> getItems()
	{
		return service.getItems();
	}

}
