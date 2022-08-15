package com.my.board.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    private String id;
    private String pwd;
    private String name;
    private String phoneNumber;
    private int userStatus;
}
