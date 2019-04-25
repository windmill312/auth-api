package com.github.windmill312.auth.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "principal", schema = "auth")
public class PrincipalEntity {

    private Integer id;
    private UUID extId = UUID.randomUUID();
    private Integer subsystemId;
    private Instant blockedTill;
    private Set<CredentialsEntity> credentials;
    private Set<PrincipalRoleEntity> roles;

    public PrincipalEntity() {
    }

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            schema = "auth", name = "auth.principal_id_seq",
            sequenceName = "auth.principal_id_seq", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auth.principal_id_seq")
    public Integer getId() {
        return id;
    }

    public PrincipalEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    @Type(type = "pg-uuid")
    @Column(name = "ext_id", nullable = false)
    public UUID getExtId() {
        return extId;
    }

    public PrincipalEntity setExtId(UUID extId) {
        this.extId = extId;
        return this;
    }

    @Column(name = "subsystem_id", nullable = false)
    public Integer getSubsystemId() {
        return subsystemId;
    }

    public PrincipalEntity setSubsystemId(Integer subsystemId) {
        this.subsystemId = subsystemId;
        return this;
    }

    @Column(name = "blocked_till")
    public Instant getBlockedTill() {
        return blockedTill;
    }

    public PrincipalEntity setBlockedTill(Instant blockedTill) {
        this.blockedTill = blockedTill;
        return this;
    }

    @OneToMany(mappedBy="principal")
    public Set<CredentialsEntity> getCredentials() {
        return credentials;
    }

    public void setCredentials(Set<CredentialsEntity> credentials) {
        this.credentials = credentials;
    }

    @OneToMany(mappedBy = "principal")
    public Set<PrincipalRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<PrincipalRoleEntity> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
