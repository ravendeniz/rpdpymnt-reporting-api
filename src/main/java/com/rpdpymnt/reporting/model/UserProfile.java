package com.rpdpymnt.reporting.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "USER_PROFILE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("PMD.TooManyFields")
public class UserProfile extends AbstractEntity implements Serializable {

    public static final long serialVersionUID = -8685507339034942104L;

    private String name;
    private String email;
    private String password;
    private long roles;
}
