package cn.dreamjun.my.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * 方便将用户发表的 firelight 动态内容中的图片 URL 使用使用 '|' 分隔符进行拼接
 * 在获取数据的时候按照 '|' 分隔符进行分割存储到 Stirng[] 数组中去
 * @Classname ImagesTypeHandler
 * @Description TODO
 * @Date 2022/9/16 18:02
 * @Created by 翊
 */
@MappedJdbcTypes(JdbcType.VARCHAR)  //数据库里的数据类型
@MappedTypes({String[].class})          //java数据类型
public class ImagesTypeHandler extends BaseTypeHandler<String[]> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String[] strings, JdbcType jdbcType) throws SQLException {
        String s = dealListToOneStr(strings);
        preparedStatement.setString(i, s);
    }

    private String dealListToOneStr(String[] parameter) {
        if (parameter == null || parameter.length <= 0)
            return null;
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < parameter.length; i++) {
            if (i == parameter.length - 1) {
                res.append(parameter[i]);
                return res.toString();
            }
            res.append(parameter[i]).append("|");
        }
        return null;
    }

    @Override
    public String[] getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if (resultSet.getString(s) == null) {
            return new String[0];
        }
        return resultSet.getString(s).split("\\|");
    }

    @Override
    public String[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
        if (resultSet.getString(i) == null) {
            return new String[0];
        }
        return resultSet.getString(i).split("\\|");
    }

    @Override
    public String[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        if (callableStatement.getString(i) == null) {
            return new String[0];
        }
        return callableStatement.getString(i).split("\\|");
    }
}
