package com.escape.plan.rednit.model.base;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;

public abstract class BaseDoc implements Persistable<String> {

    @Id private String id;
    @Version private Long version;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
