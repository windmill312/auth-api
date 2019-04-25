package com.github.windmill312.auth.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role", schema = "auth")
public class RoleEntity {
    private Integer id;
    private SubsystemEntity subsystem;
    private String name;
    private Set<PrincipalRoleEntity> principals;

    public RoleEntity() {
    }

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            schema = "auth", name = "auth.role_id_seq",
            sequenceName = "auth.role_id_seq", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auth.role_id_seq")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "subsystem_id", nullable = false)
    public SubsystemEntity getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(SubsystemEntity subsystem) {
        this.subsystem = subsystem;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "role")
    public Set<PrincipalRoleEntity> getPrincipals() {
        return principals;
    }

    public void setPrincipals(Set<PrincipalRoleEntity> principals) {
        this.principals = principals;
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
