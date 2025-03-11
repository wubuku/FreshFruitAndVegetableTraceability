package org.dddml.ffvtraceability.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private List<GroupDto> groups;
    private Boolean passwordChangeRequired;
    private OffsetDateTime TempPasswordLastGenerated;
    private String email;
    private String employeeNumber;
    private String departmentId;
    private String directManagerName;
    private String telephoneNumber;
    private String mobileNumber;
    private String employeeTypeId;
    private OffsetDateTime fromDate;
    private String employeeContractNumber;
    private String certificationDescription;
    private String skillSetDescription;
    private String languageSkills;
    private String associatedGln;
    private String profileImageUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<String> permissions;

    public OffsetDateTime getTempPasswordLastGenerated() {
        return TempPasswordLastGenerated;
    }

    public void setTempPasswordLastGenerated(OffsetDateTime tempPasswordLastGenerated) {
        TempPasswordLastGenerated = tempPasswordLastGenerated;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<GroupDto> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDto> groups) {
        this.groups = groups;
    }

    public Boolean getPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDirectManagerName() {
        return directManagerName;
    }

    public void setDirectManagerName(String directManagerName) {
        this.directManagerName = directManagerName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(String employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public OffsetDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(OffsetDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public String getEmployeeContractNumber() {
        return employeeContractNumber;
    }

    public void setEmployeeContractNumber(String employeeContractNumber) {
        this.employeeContractNumber = employeeContractNumber;
    }

    public String getCertificationDescription() {
        return certificationDescription;
    }

    public void setCertificationDescription(String certificationDescription) {
        this.certificationDescription = certificationDescription;
    }

    public String getSkillSetDescription() {
        return skillSetDescription;
    }

    public void setSkillSetDescription(String skillSetDescription) {
        this.skillSetDescription = skillSetDescription;
    }

    public String getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(String languageSkills) {
        this.languageSkills = languageSkills;
    }

    public String getAssociatedGln() {
        return associatedGln;
    }

    public void setAssociatedGln(String associatedGln) {
        this.associatedGln = associatedGln;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
