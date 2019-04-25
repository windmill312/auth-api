package com.github.windmill312.auth.model;

import java.util.UUID;

public class PrincipalOuterKey {
    private final Integer subsystemCode;
    private final UUID principalExtId;

    public PrincipalOuterKey(Integer subsystemCode, UUID principalExtId) {
        this.subsystemCode = subsystemCode;
        this.principalExtId = principalExtId;
    }

    public Integer getSubsystemCode() {
        return subsystemCode;
    }

    public UUID getPrincipalExtId() {
        return principalExtId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrincipalOuterKey that = (PrincipalOuterKey) o;

        if (!subsystemCode.equals(that.subsystemCode)) {
            return false;
        }
        return principalExtId.equals(that.principalExtId);
    }

    @Override
    public int hashCode() {
        int result = subsystemCode.hashCode();
        result = 31 * result + principalExtId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PrincipalOuterKey{" +
                "subsystemCode=" + subsystemCode +
                ", principalExtId='" + principalExtId + '\'' +
                "} " + super.toString();
    }

}
