package com.example.pointmanagementproject.point;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class ExpiredPointSummary {

    String userId;  // 유저 ID
    BigInteger amount;  // 만료 금액

    @QueryProjection
    public ExpiredPointSummary(String userId, BigInteger amount) {
        this.userId = userId;
        this.amount = amount;
    }

}
