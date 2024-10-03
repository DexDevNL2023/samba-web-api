package com.teleo.manager.authentification.entities.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.generic.utils.GenericUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractToken implements Serializable {
    @Serial
    private static final long serialVersionUID = -7551160985498051566L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Account user;
    private Date expiryDate;

    public AbstractToken(String token) {
        super();
        this.token = token;
        this.expiryDate = GenericUtils.calculateExpiryDate(AppConstants.TOKEN_EXPIRATION);
    }

    public AbstractToken(Account user, String token) {
        super();
        this.token = token;
        this.user = user;
        this.expiryDate = GenericUtils.calculateExpiryDate(AppConstants.TOKEN_EXPIRATION);
    }

    public void updateToken(String token) {
        this.token = token;
        this.expiryDate = GenericUtils.calculateExpiryDate(AppConstants.TOKEN_EXPIRATION);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractToken other = (AbstractToken) obj;
        if (expiryDate == null) {
            if (other.expiryDate != null) {
                return false;
            }
        } else if (!expiryDate.equals(other.expiryDate)) {
            return false;
        }
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        if (user == null) {
            return other.user == null;
        } else return user.equals(other.user);
    }

    @Override
    public String toString() {
		return "Token [String=" + token + "]" + "[Expires" + expiryDate + "]";
    }
}