package hr.algebra.repository.sql;

import hr.algebra.repository.sql.mapper.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseSqlRepository <T>{
    protected final DataSource dataSource;

    protected BaseSqlRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    protected abstract T mapRow(ResultSet resultSet) throws SQLException;

    protected List<T> findMany(String sql, SqlConsumer<PreparedStatement> parameterSetter) throws SQLException {
        List<T> result=new ArrayList<>();

           try (Connection con=dataSource.getConnection();
           PreparedStatement ps=con.prepareStatement(sql)){
               parameterSetter.accept(ps);
               try(ResultSet rs= ps.executeQuery()){
                   while (rs.next()){
                       result.add(mapRow(rs));
                   }
               }
           }
        return result;
    }

    protected <R> List<R> findMany(String sql, SqlConsumer<PreparedStatement> parameterMapper, RowMapper<R> rowMapper) throws SQLException {
        List<R> results = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            parameterMapper.accept(ps);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(rowMapper.map(rs));
                }
            }
        }

        return results;
    }


    protected Optional<T> findSingleResult(String sql, SqlConsumer<PreparedStatement> parameterSetter )throws SQLException
    {
        try(Connection con= dataSource.getConnection();
        PreparedStatement ps=con.prepareStatement(sql))
        {
            parameterSetter.accept(ps);
            try (ResultSet rs=ps.executeQuery())
            {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    protected int executeUpdate(String sql, SqlConsumer<PreparedStatement> parameterSetter) throws SQLException
    {
        try(Connection con= dataSource.getConnection();
        PreparedStatement ps=con.prepareStatement(sql))
        {
            parameterSetter.accept(ps);
            return ps.executeUpdate();
        }
    }

    protected long executeInsertReturningId (String sql, SqlConsumer<PreparedStatement> parameterSetter) throws SQLException
    {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            parameterSetter.accept(ps);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }

        throw new SQLException("Insert failed; generated ID was not returned.");
    }

    protected abstract void mapEntityToTable(PreparedStatement ps, T entity) throws SQLException;

    @FunctionalInterface
    protected interface SqlConsumer<T> {
        void accept(T value) throws SQLException;
    }
}
