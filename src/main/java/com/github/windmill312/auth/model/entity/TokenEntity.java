package com.github.windmill312.auth.model.entity;

import com.github.windmill312.auth.model.TokenType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "token", schema = "auth")
public class TokenEntity {
    private Integer id;
    private UUID principalExtId;
    private String value;
    private TokenType tokenType;
    private Instant validFrom;
    private Instant validTill;
    private Instant lastAccess;

    public TokenEntity() {
    }

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            schema = "auth", name = "auth.token_id_seq",
            sequenceName = "auth.token_id_seq", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auth.token_id_seq")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Type(type = "pg-uuid")
    @Column(name = "principal_ext_id", nullable = false)
    public UUID getPrincipalExtId() {
        return principalExtId;
    }

    public TokenEntity setPrincipalExtId(UUID principalExtId) {
        this.principalExtId = principalExtId;
        return this;
    }

    @Column(name = "value", nullable = false)
    public String getValue() {
        return value;

    }

    public TokenEntity setValue(String value) {
        this.value = value;
        return this;
    }

    @Column(name = "type", nullable = false)
    public TokenType getTokenType() {
        return tokenType;
    }

    public TokenEntity setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    @Column(name = "valid_from", nullable = false)
    public Instant getValidFrom() {
        return validFrom;
    }

    public TokenEntity setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    @Column(name = "valid_till", nullable = false)
    public Instant getValidTill() {
        return validTill;
    }

    public TokenEntity setValidTill(Instant validTill) {
        this.validTill = validTill;
        return this;
    }

    @Column(name = "last_access", nullable = false)
    public Instant getLastAccess() {
        return lastAccess;
    }

    public TokenEntity setLastAccess(Instant lastAccess) {
        this.lastAccess = lastAccess;
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
