## spring-boot-mybatis-redis
这个项目比test-redis项目更加的完善

### Mybatis代码自动生成
	此项目是通过MyBatisGeneratorTool自动根据数据库的表生成model(entity) , mapper xml mapper interface 等，  做到代码自动生成的功能
	
### redis-cache
	在indexControll 中：
```java
	/**
	 * redis会将Person的结果保存到redis里面 ， 对应的key为：person-key
	 * @return
	 */
	@RequestMapping("/redis/cachePersons")
	@Cacheable(value = "person-key")
	public Person cachePersons() {
		Person person = personService.findPersonById(1);
		System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
		return person;
	}
```
```java
	//采用命令：keys '*sessions*' 可以看见缓存的sessionId
	@RequestMapping(value = "/redis/uid")
	public String uid(HttpSession session) {
		UUID uuid = (UUID) session.getAttribute("uid");
		if (null == uuid) {
			uuid = UUID.randomUUID();
		}
		session.setAttribute("uid", uuid);
		return session.getId();
	}
```

### 加入了主动刷新缓存，重置缓存的时间





