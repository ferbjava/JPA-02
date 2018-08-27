package com.capgemini.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthorEntity is a Querydsl query type for AuthorEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAuthorEntity extends EntityPathBase<AuthorEntity> {

    private static final long serialVersionUID = 1794428612L;

    public static final QAuthorEntity authorEntity = new QAuthorEntity("authorEntity");

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastName = createString("lastName");

    public QAuthorEntity(String variable) {
        super(AuthorEntity.class, forVariable(variable));
    }

    public QAuthorEntity(Path<? extends AuthorEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthorEntity(PathMetadata metadata) {
        super(AuthorEntity.class, metadata);
    }

}

