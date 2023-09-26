package com.example.pointmanagementproject.point.message;

import com.example.pointmanagementproject.point.IdEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Message extends IdEntity {

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    String content;

}
