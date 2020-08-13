package com.revseev.batch.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revseev.batch.model.PersonType;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class PersonPreparedStatementSetter implements ItemPreparedStatementSetter<PersonType> {

	@Override
	public void setValues(PersonType personType, PreparedStatement ps) throws SQLException {
		ps.setInt(1, personType.getPersonId());
		ps.setString(2, personType.getFirstName());
		ps.setString(3, personType.getLastName());
		ps.setString(4, personType.getEmail());
		ps.setInt(5, personType.getAge());
	}

}
