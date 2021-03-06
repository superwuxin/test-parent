package org.harmony.test.javaee.jpa.persistence;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author wuxii@foxmail.com
 */
@Entity
@Table(name = "T_LOB_ENTITY")
public class LobEntity {

    @Id
    private String id;
    @Lob
    private Serializable exceptions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Exception[] getExceptions() {
        return (Exception[]) exceptions;
    }

    public void setExceptions(Exception[] exceptions) {
        this.exceptions = exceptions;
    }

}
