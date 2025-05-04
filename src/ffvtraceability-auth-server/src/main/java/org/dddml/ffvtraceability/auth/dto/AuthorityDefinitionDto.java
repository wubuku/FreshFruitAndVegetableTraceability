package org.dddml.ffvtraceability.auth.dto;

public class AuthorityDefinitionDto {
    private String authorityId;
    private String description;
    private Boolean enabled;
    
    public String getAuthorityId() {
        return authorityId;
    }
    
    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
