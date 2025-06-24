package com.scriptum.backend.infrastructure.database.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TAG")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "COLOR")
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserJpaEntity user;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Notes> notes = new HashSet<>();
}
