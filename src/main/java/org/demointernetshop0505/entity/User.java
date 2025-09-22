package org.demointernetshop0505.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class User {

    public enum Role {
        ADMIN,
        USER,
        MANAGER
    }

    public enum Status {
        NOT_CONFIRM,
        CONFIRMED,
        BANNED,
        DELETE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "First name is required and must be not blank)")
    @Size(min = 3, max = 15)
    private String firstName;

    private String lastName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private String hashPassword;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String photoLink;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileInfo> photos = new HashSet<>();

    private LocalDateTime createDate;

    private LocalDateTime updateDate;


}
