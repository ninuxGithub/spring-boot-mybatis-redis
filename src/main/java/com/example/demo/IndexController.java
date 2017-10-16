package com.example.demo;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Person;
import com.example.demo.entity.PersonExample;
import com.example.demo.mapper.PersonMapper;

@RestController
public class IndexController {

	@Autowired
	PersonMapper personMapper;

	/**
	 * 测试是否可以正常调用到数据库的数据， OK
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/", "/index", "/persons" })
	public List<Person> persons() {
		PersonExample pe = new PersonExample();
		pe.setLimitStart(0);
		pe.setCount(10);
		return personMapper.selectByExample(pe);
	}

	@RequestMapping("/redis/cachePersons")
	@Cacheable(value = "person-key")
	public List<Person> cachePersons() {
		PersonExample pe = new PersonExample();
		pe.setLimitStart(0);
		pe.setCount(10);
		return personMapper.selectByExample(pe);
	}

	@RequestMapping(value = "/redis/uid")
	public String uid(HttpSession session) {
		UUID uuid = (UUID) session.getAttribute("uid");
		if (null == uuid) {
			uuid = UUID.randomUUID();
		}
		session.setAttribute("uid", uuid);
		return session.getId();
	}
}
