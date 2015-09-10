package com.yu.service.test;

import org.junit.Before;
import org.junit.Test;

import com.yu.article.service.ArticleService;
import com.yu.junit.BaseTest;

public class ArticleServiceTest extends BaseTest {

	private ArticleService articleService;
	
	@Before
	public void getService(){
		articleService = applicationContext.getBean(ArticleService.class);
	}
	
	@Test
	public void testFindPage() {
		log.info("totalPage:" + articleService.getArticles(10, 1).getTotalPage());
	}

}