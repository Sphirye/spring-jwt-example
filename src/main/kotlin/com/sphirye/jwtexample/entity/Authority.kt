package com.sphirye.jwtexample.entity

import javax.persistence.*

@Entity
@Table(name = "authority")
data class Authority (
    @Id @Column(name = "role")
    var role: String? = null
)