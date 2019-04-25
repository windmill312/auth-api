package com.github.windmill312.auth.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "oauth_refresh_token", schema = "auth")
public class OAuthRefreshToken {

    private Integer id;
    private String clientId;
    private UUID principalId;
    private String refreshToken;
    private Instant expiresIn;

    public OAuthRefreshToken() {
    }

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            schema = "auth", name = "auth.oauth_refresh_token_id_seq",
            sequenceName = "auth.oauth_refresh_token_id_seq", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auth.oauth_refresh_token_id_seq")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "client_id", nullable = false)
    public String getClientId() {
        return clientId;
    }

    public OAuthRefreshToken setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    @Column(name = "principal_id", nullable = false)
    public UUID getPrincipalId() {
        return principalId;
    }

    public OAuthRefreshToken setPrincipalId(UUID principalId) {
        this.principalId = principalId;
        return this;
    }

    @Column(name = "refresh_token", nullable = false)
    public String getRefreshToken() {
        return refreshToken;
    }

    public OAuthRefreshToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @Column(name = "expires_in", nullable = false)
    public Instant getExpiresIn() {
        return expiresIn;
    }

    public OAuthRefreshToken setExpiresIn(Instant expiresIn) {
        this.expiresIn = expiresIn;
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
