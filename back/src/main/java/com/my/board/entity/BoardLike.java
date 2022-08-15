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
@Table(name = "board_like")
public class BoardLike {
    @Id
    private String id;
    private int boardNo;
    private int likeStatus;
}
