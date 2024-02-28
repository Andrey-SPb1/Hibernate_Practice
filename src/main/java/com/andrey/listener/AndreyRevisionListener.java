package com.andrey.listener;

import com.andrey.entity.Revision;
import org.hibernate.envers.RevisionListener;

public class AndreyRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
//        SecurityContext.getUser().getId()
        ((Revision) revisionEntity).setUsername("andrey");
    }
}
