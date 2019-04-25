package com.github.windmill312.auth.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "principal_x_role", schema = "auth")
public class PrincipalRoleEntity {
    private Integer id;
    private PrincipalEntity principal;
    private RoleEntity role;

    public PrincipalRoleEntity() {
    }

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            schema = "auth", name = "auth.principal_x_role_id_seq",
            sequenceName = "auth.principal_x_role_id_seq", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auth.principal_x_role_id_seq")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "principal_id", nullable = false)
    public PrincipalEntity getPrincipal() {
        return principal;
    }

    public void setPrincipal(PrincipalEntity principal) {
        this.principal = principal;
    }

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
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
