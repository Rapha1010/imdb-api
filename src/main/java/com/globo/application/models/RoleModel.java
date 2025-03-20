package com.globo.application.models;

import com.globo.application.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="TB_ROLES")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;
}
