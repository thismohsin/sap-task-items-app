package com.sap.task.items.domain;

/**
 * This class encapsulate item, having event information.
 *
 * @author mk
 */
public final class ItemDetail
{
	private Item item;


	public Item getItem()
	{
		return item;
	}


	public void setItem(final Item item)
	{
		this.item = item;
	}


	/**
	 * ItemDetails identity is defined by item object.
	 */
	@Override
	public boolean equals(final Object that)
	{
		return item.equals(((ItemDetail) that).item);
	}


	@Override
	public int hashCode()
	{
		return item.hashCode();
	}

}
