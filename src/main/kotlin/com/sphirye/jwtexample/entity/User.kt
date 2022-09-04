package com.sphirye.jwtexample.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "`user`")
data class User(
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null,

    @Column(name = "username")
    var username: String? = null,

    @Column(name = "password") @JsonIgnore
    var password: String? = null,

    @Column(name = "activated")
    var isActivated: Boolean = false,

    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role", referencedColumnName = "role")]
    )
    var authorities: Set<Authority>? = null
)