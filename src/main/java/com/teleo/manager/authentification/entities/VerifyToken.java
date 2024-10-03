package com.teleo.manager.authentification.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.teleo.manager.authentification.entities.token.AbstractToken;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "tokens")
public class VerifyToken extends AbstractToken implements Serializable {

    public VerifyToken() {
    }

    public VerifyToken(String token) {
        super(token);
    }

    public VerifyToken(Account user, String token) {
        super(user, token);
    }
}
