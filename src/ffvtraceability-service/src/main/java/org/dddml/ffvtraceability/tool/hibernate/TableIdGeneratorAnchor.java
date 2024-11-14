package org.dddml.ffvtraceability.tool.hibernate;

/**
 * This entity exists solely to bootstrap Hibernate's ID generator infrastructure.
 * It has no business meaning and should not be used directly.
 */
public class TableIdGeneratorAnchor {

    private Long id;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
