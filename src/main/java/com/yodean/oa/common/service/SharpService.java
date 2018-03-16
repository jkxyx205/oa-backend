package com.yodean.oa.common.service;

import com.yodean.oa.common.dto.PageModel;
import com.yodean.oa.common.util.sql.SqlFormatter;
import com.yodean.oa.sys.user.entity.User;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2018/3/16.
 */

@Service
public class SharpService {
    private static Logger logger = LoggerFactory.getLogger(SharpService.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    public interface JdbcTemplateCallback<T> {
        T query(JdbcTemplate jdbcTemplate, String sql,
                Object[] args);
    }

    public  List<Map> query(String sql, Map<String, Object> params) {
        return query(sql, params, Map.class);
    }

    public <T> List<T> query(String sql, Map<String, Object> params, Class<T> clazz) {
        return query(sql, params, new JdbcTemplateCallback<List<T>>() {
            @Override
            public List<T> query(JdbcTemplate jdbcTemplate, String sql, Object[] args) {
                logger.debug("sql:{}, args:{}", sql, args);
                if (clazz == String.class || Number.class.isAssignableFrom(clazz) || Character.class ==clazz || Boolean.class == clazz) {
                    return jdbcTemplate.queryForList(sql, args, clazz);
                } else if (clazz == Map.class) {
                    return (List) jdbcTemplate.queryForList(sql, args);
                }

                return  jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<>(clazz));
            }
        });
    }

    public <T> List<T> query(String sql, Map<String, Object> params, JdbcTemplateCallback<List<T>> jdbcTemplateCallback) {
        Map<String, Object> formatMap = new HashMap<>();

        String formatSql = SqlFormatter.formatSql(sql, params, formatMap);

        Object[] args = NamedParameterUtils.buildValueArray(formatSql,
                formatMap);

        formatSql = formatSql.replaceAll(SqlFormatter.PARAM_REGEX,"?"); //mysql
        return jdbcTemplateCallback.query(jdbcTemplate, formatSql, args);

    }


    public String getSQL(String queryName) {
        MappedStatement mappedStatement = sqlSessionFactory.getConfiguration().getMappedStatement(queryName);
        String sql = mappedStatement.getBoundSql(Collections.emptyMap()).getSql();
        return sql;
    }

}
