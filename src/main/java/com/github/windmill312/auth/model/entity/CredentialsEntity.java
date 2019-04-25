package com.github.windmill312.auth.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "credentials", schema = "auth")
public class CredentialsEntity {
    private Integer id;
    private String identifier;
    private String secret;
    private PrincipalEntity principal;
    private SubsystemEntity subsystem;

    public CredentialsEntity() {
    }

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            schema = "auth", name = "auth.credentials_id_seq",
            sequenceName = "auth.credentials_id_seq", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auth.credentials_id_seq")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "identifier", nullable = false)
    public String getIdentifier() {
        return identifier;
    }

    public CredentialsEntity setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    @Column(name = "secret", nullable = false)
    public String getSecret() {
        return secret;
    }

    public CredentialsEntity setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    @ManyToOne
    @JoinColumn(name = "principal_id", nullable = false)
    public PrincipalEntity getPrincipal() {
        return principal;
    }

    public CredentialsEntity setPrincipal(PrincipalEntity principal) {
        this.principal = principal;
        return this;
    }

    @ManyToOne
    @JoinColumn(name = "subsystem_id", nullable = false)
    public SubsystemEntity getSubsystem() {
        return subsystem;
    }

    public CredentialsEntity setSubsystem(SubsystemEntity subsystem) {
        this.subsystem = subsystem;
        return this;
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
