package com.capgemini.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClientEntity is a Querydsl query type for ClientEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QClientEntity extends EntityPathBase<ClientEntity> {

    private static final long serialVersionUID = 71561476L;

    public static final QClientEntity clientEntity = new QClientEntity("clientEntity");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath adress = createString("adress");

    //inherited
    public final DateTimePath<java.util.Date> created = _super.created;

    public final DateTimePath<java.util.Calendar> dateBirth = createDateTime("dateBirth", java.util.Calendar.class);

    public final StringPath email = createString("email");

    public final StringPath firstName = createString("firstName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath lastName = createString("lastName");

    public final NumberPath<Integer> phoneNumber = createNumber("phoneNumber", Integer.class);

    public final ListPath<TransactionEntity, QTransactionEntity> transactions = this.<TransactionEntity, QTransactionEntity>createList("transactions", TransactionEntity.class, QTransactionEntity.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> updated = _super.updated;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QClientEntity(String variable) {
        super(ClientEntity.class, forVariable(variable));
    }

    public QClientEntity(Path<? extends ClientEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClientEntity(PathMetadata metadata) {
        super(ClientEntity.class, metadata);
    }

}

