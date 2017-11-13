package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Person;
import com.example.demo.mapper.PersonMapper;

@Service
public class PersonService {
	@Autowired
	PersonMapper personMapper;
	

	public Person findPersonById(Integer id) {
		return personMapper.selectByPrimaryKey(id);
	}
	
}
