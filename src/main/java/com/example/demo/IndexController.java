package com.example.demo;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Person;
import com.example.demo.entity.PersonExample;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.service.PersonService;

@RestController
public class IndexController {

	@Autowired
	PersonMapper personMapper;

	@Autowired
	PersonService personService;

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

	/**
	 * @Cacheable(value = "person-key")
	 * redis会将Person的结果保存到redis里面 ， 对应的key为：person-key
	 * 
	 * 
	 * 在采用了
	 * @Cacheable(value = "Person#5#2", key="#id")
	 * 
	 * 之后会在缓存实现的前2秒时间主动的刷新缓存，重置失效的时间 (5s)，然缓存一直可以使用
	 * 
	 * link:https://www.cnblogs.com/ASPNET2008/p/6511500.html
	 * 
	 * @return
	 */
	
	
	@RequestMapping("/redis/cachePersons/{id}")
	@Cacheable(value = "Person#5#2"/*方法二*/ , key="#id")
	public Person cachePersons(@PathVariable("id") Integer id) {
		Person person = personService.findPersonById(id);
		System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
		return person;
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
