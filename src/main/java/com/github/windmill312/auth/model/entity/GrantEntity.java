package com.github.windmill312.auth.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "grant", schema = "auth")
public class GrantEntity {
    private Integer id;
    private Integer principalId;
    private String value;

    public GrantEntity() {
    }

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            schema = "auth", name = "auth.grant_id_seq",
            sequenceName = "auth.grant_id_seq", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auth.grant_id_seq")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "principal_id", nullable = false)
    public Integer getPrincipalId() {
        return principalId;
    }

    public GrantEntity setPrincipalId(Integer principalId) {
        this.principalId = principalId;
        return this;
    }

    @Column(name = "value", nullable = false)
    public String getValue() {
        return value;
    }

    public GrantEntity setValue(String value) {
        this.value = value;
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

