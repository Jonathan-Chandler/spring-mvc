//package com.jonathan.web.entities;
//import javax.persistence.*;
//
//@Entity
//@Table(name = "roles")
//public class Role {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
//
//	@Enumerated(EnumType.STRING)
//	@Column(length = 20)
//	private EnumRoleType name;
//
//	public Role() {
//	}
//
//	public Role(EnumRoleType name) {
//		this.name = name;
//	}
//
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//
//	public EnumRoleType getName() {
//		return name;
//	}
//
//	public void setName(EnumRoleType name) {
//		this.name = name;
//	}
//}
//
