package com.park.restapi.domain.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponSetting is a Querydsl query type for CouponSetting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponSetting extends EntityPathBase<CouponSetting> {

    private static final long serialVersionUID = 296699452L;

    public static final QCouponSetting couponSetting = new QCouponSetting("couponSetting");

    public final NumberPath<Integer> dailyCouponQuantity = createNumber("dailyCouponQuantity", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDailyCouponGenerate = createBoolean("isDailyCouponGenerate");

    public QCouponSetting(String variable) {
        super(CouponSetting.class, forVariable(variable));
    }

    public QCouponSetting(Path<? extends CouponSetting> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponSetting(PathMetadata metadata) {
        super(CouponSetting.class, metadata);
    }

}

