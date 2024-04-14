package com.park.restapi.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailConfirm is a Querydsl query type for EmailConfirm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailConfirm extends EntityPathBase<EmailConfirm> {

    private static final long serialVersionUID = -737761594L;

    public static final QEmailConfirm emailConfirm = new QEmailConfirm("emailConfirm");

    public final StringPath certificationNumber = createString("certificationNumber");

    public final BooleanPath certificationStatus = createBoolean("certificationStatus");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QEmailConfirm(String variable) {
        super(EmailConfirm.class, forVariable(variable));
    }

    public QEmailConfirm(Path<? extends EmailConfirm> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailConfirm(PathMetadata metadata) {
        super(EmailConfirm.class, metadata);
    }

}

