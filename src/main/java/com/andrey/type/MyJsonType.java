package com.andrey.type;

import com.andrey.entity.MyJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class MyJsonType implements UserType<MyJson> {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public int getSqlType() {
        return SqlTypes.JSON;
    }

    @Override
    public Class<MyJson> returnedClass() {
        return MyJson.class;
    }

    @Override
    public boolean equals(MyJson x, MyJson y) {
        return false;
    }

    @Override
    public int hashCode(MyJson x) {
        return 0;
    }

    @Override
    public MyJson nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        final String cellContent = rs.getString(position);
        if (cellContent == null) {
            return null;
        }
        try {
            return MAPPER.readValue(cellContent.getBytes(StandardCharsets.UTF_8), returnedClass());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to MyJson: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, MyJson value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
            return;
        }
        try {
            final StringWriter w = new StringWriter();
            MAPPER.writeValue(w, value);
            w.flush();
            st.setObject(index, w.toString(), Types.OTHER);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert MyJson to String: " + ex.getMessage(), ex);
        }
    }

    @Override
    public MyJson deepCopy(MyJson value) {
        try {
            // use serialization to create a deep copy
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            bos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            MyJson obj = (MyJson)new ObjectInputStream(bais).readObject();
            bais.close();
            return obj;
        } catch (ClassNotFoundException | IOException ex) {
            throw new HibernateException(ex);
        }
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(MyJson value) {
        return null;
    }

    @Override
    public MyJson assemble(Serializable cached, Object owner) {
        return null;
    }
}
