package vn.prostylee.core.configuration.database;

import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

public class CustomPostgresDialect extends PostgreSQL95Dialect {

    public CustomPostgresDialect() {
        registerHibernateType(Types.NVARCHAR, 4000, "string");
        registerHibernateType(Types.BIGINT, StandardBasicTypes.LONG.getName());
        registerHibernateType(Types.DATE, StandardBasicTypes.TIMESTAMP.getName());
    }
}
