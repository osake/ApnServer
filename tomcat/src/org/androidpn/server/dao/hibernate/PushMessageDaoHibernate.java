package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.PushMessageDao;
import org.androidpn.server.model.PushMessage;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PushMessageDaoHibernate extends HibernateDaoSupport implements PushMessageDao
{

	public PushMessage getPushMessage(Long id)
	{
		return (PushMessage) getHibernateTemplate().get(PushMessage.class, id);
	}

	public PushMessage savePushMessage(PushMessage pushMessage)
	{
		getHibernateTemplate().saveOrUpdate(pushMessage);
		getHibernateTemplate().flush();
		return pushMessage;
	}


	@SuppressWarnings("unchecked")
	public List<PushMessage> getPushMessages(String userName)
	{
		return  (List<PushMessage>)getHibernateTemplate().get(PushMessage.class, userName);
	}

	public void removePushMessage(Long id)
	{
		getHibernateTemplate().delete(getPushMessage(id));
		
	}
}
