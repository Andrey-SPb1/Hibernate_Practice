package com.andrey.listener;

import com.andrey.entity.Audit;
import org.hibernate.event.spi.*;

import java.io.Serial;

public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {
    @Serial
    private static final long serialVersionUID = -4253825832081803879L;

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        auditEntity(event, Audit.Operation.DELETE);
        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        auditEntity(event, Audit.Operation.INSERT);
        return false;
    }

    private void auditEntity(AbstractPreDatabaseOperationEvent event, Audit.Operation operation) {
        if(event.getEntity().getClass() != Audit.class) {
            Audit audit = Audit.builder()
                    .entityId(event.getId())
                    .entityName(event.getEntityName())
                    .entityContent(event.getEntity().toString())
                    .operation(operation)
                    .build();

            event.getSession().persist(audit);
        }
    }
}
