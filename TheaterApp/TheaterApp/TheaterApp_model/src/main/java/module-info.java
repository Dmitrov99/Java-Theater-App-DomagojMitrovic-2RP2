module TheaterApp.model {
    requires java.sql;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires ch.qos.logback.classic;


    exports hr.algebra.model;
    exports hr.algebra.repository;
    exports hr.algebra.exceptions;
    exports hr.algebra.utilities;
    exports hr.algebra.repository.sql;
    exports hr.algebra.repository.sql.mapper;
    exports hr.algebra.repository.sql.queries;
}