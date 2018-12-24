package com.sap.task.items.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * The item bean is used for marhsalling and unmarshalling JSON request and storing data in cache.
 *
 * @author mk
 */
public final class Item
{
	private Integer id;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private ZonedDateTime timestamp;


	public Integer getId()
	{
		return id;
	}


	public void setId(final Integer id)
	{
		this.id = id;
	}


	public ZonedDateTime getTimestamp()
	{
		return timestamp;
	}


	public void setTimestamp(final ZonedDateTime timestamp)
	{
		this.timestamp = timestamp;
	}


	@Override
	public boolean equals(final Object obj)
	{
		final Item that = (Item) obj;
		return Objects.equals(id, that.id)
			   && timestamp.isEqual(that.timestamp);
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(7, 37)
					   .append(id)
					   .append(timestamp)
					   .toHashCode();
	}
}
