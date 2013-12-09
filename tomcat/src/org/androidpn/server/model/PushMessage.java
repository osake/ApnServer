package org.androidpn.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "push_message")
public class PushMessage implements Serializable
{

	private static final long serialVersionUID = 4089299699763678950L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "username", nullable = false, length = 64)
	private String username;

	@Column(name = "title", length = 64)
	private String title;

	@Column(name = "message", length = 64)
	private String message;

	@Column(name = "uri", length = 64)
	private String uri;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getUri()
	{
		return uri;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof PushMessage))
		{
			return false;
		}

		final PushMessage obj = (PushMessage) o;
		if (username != null ? !username.equals(obj.username)
				: obj.username != null)
		{
			return false;
		}
		if (title != null ? !title.equals(obj.title) : obj.title != null)
		{
			return false;
		}
		if (message != null ? !message.equals(obj.message) : obj.message != null)
		{
			return false;
		}
		if (uri != null ? !uri.equals(obj.uri) : obj.uri != null)
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int result = 0;
		result = 29 * result + (username != null ? username.hashCode() : 0);
		result = 29 * result + (title != null ? title.hashCode() : 0);
		result = 29 * result + (message != null ? message.hashCode() : 0);
		result = 29 * result + (uri != null ? uri.hashCode() : 0);
		
		return result;
	}

}
