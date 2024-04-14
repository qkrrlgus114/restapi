package com.park.restapi.domain.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApiRequestHistory is a Querydsl query type for ApiRequestHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApiRequestHistory extends EntityPathBase<ApiRequestHistory> {

    private static final long serialVersionUID = -1334821581L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApiRequestHistory apiRequestHistory = new QApiRequestHistory("apiRequestHistory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.park.restapi.domain.member.entity.QMember member;

    public final EnumPath<MethodType> methodType = createEnum("methodType", MethodType.class);

    public final BooleanPath request_status = createBoolean("request_status");

    public final StringPath requestContent = createString("requestContent");

    public final DateTimePath<java.time.LocalDateTime> requestDate = createDateTime("requestDate", java.time.LocalDateTime.class);

    public final StringPath responseContent = createString("responseContent");

    public QApiRequestHistory(String variable) {
        this(ApiRequestHistory.class, forVariable(variable), INITS);
    }

    public QApiRequestHistory(Path<? extends ApiRequestHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApiRequestHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApiRequestHistory(PathMetadata metadata, PathInits inits) {
        this(ApiRequestHistory.class, metadata, inits);
    }

    public QApiRequestHistory(Class<? extends ApiRequestHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.park.restapi.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

