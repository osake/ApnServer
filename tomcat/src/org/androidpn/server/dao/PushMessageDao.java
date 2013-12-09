package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.PushMessage;

public interface PushMessageDao
{
    public PushMessage getPushMessage(Long id);

    public PushMessage savePushMessage(PushMessage pushMessage);

    public void removePushMessage(Long id);

    public List<PushMessage> getPushMessages(String userName);

}