package com.spyd.HRMS.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
public class AuthResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String jwt;
    private boolean authenticated;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String jwt, boolean authenticated, String message) {
        this.jwt = jwt;
        this.authenticated = authenticated;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthResponse that = (AuthResponse) o;
        return authenticated == that.authenticated &&
                Objects.equals(jwt, that.jwt) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt, authenticated, message);
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "jwt='" + jwt + '\'' +
                ", authenticated=" + authenticated +
                ", message='" + message + '\'' +
                '}';
    }
}
