package com.park.restapi.domain.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouponHistory is a Querydsl query type for CouponHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponHistory extends EntityPathBase<CouponHistory> {

    private static final long serialVersionUID = -762307456L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouponHistory couponHistory = new QCouponHistory("couponHistory");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.park.restapi.domain.member.entity.QMember member;

    public QCouponHistory(String variable) {
        this(CouponHistory.class, forVariable(variable), INITS);
    }

    public QCouponHistory(Path<? extends CouponHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouponHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouponHistory(PathMetadata metadata, PathInits inits) {
        this(CouponHistory.class, metadata, inits);
    }

    public QCouponHistory(Class<? extends CouponHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.park.restapi.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

