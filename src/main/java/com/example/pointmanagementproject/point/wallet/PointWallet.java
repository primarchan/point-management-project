package com.example.pointmanagementproject.point.wallet;

import com.example.pointmanagementproject.point.IdEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;

@Table
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointWallet extends IdEntity {

    @Column(name = "user_id", unique = true, nullable = false)
    String userId;

    @Column(name = "amount", columnDefinition = "BIGINT")
    BigInteger amount;

}
