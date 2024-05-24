package com.park.restapi.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1271603036L;

    public static final QMember member = new QMember("member1");

    public final DateTimePath<java.time.LocalDateTime> bannedDate = createDateTime("bannedDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> loginLastDate = createDateTime("loginLastDate", java.time.LocalDateTime.class);

    public final ListPath<MemberRole, QMemberRole> memberRoles = this.<MemberRole, QMemberRole>createList("memberRoles", MemberRole.class, QMemberRole.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final EnumPath<SocialType> socialType = createEnum("socialType", SocialType.class);

    public final NumberPath<Integer> token = createNumber("token", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> withdrawalDate = createDateTime("withdrawalDate", java.time.LocalDateTime.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

