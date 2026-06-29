package com.ezeeinfo.service.impl;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ezeeinfo.config.ProductFeignClient;
import com.ezeeinfo.controller.io.ProductIO;
import com.ezeeinfo.dao.ProductDAO;
import com.ezeeinfo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductFeignClient productFeignClient;
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	@Autowired
	ProductDAO productDAO;

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public List<ProductIO> getAllProducts(String namespaceCode) {

		List<ProductIO> productIOs = (List<ProductIO>) redisTemplate.opsForValue().get(namespaceCode);
		if (productIOs != null) {
			LOG.info("Products retrieved from REDIS CACHE for Namespace {}", namespaceCode);
		}
		if (productIOs == null) {
			productIOs = productFeignClient.getAllProducts(namespaceCode);
			if (productIOs != null) {
				LOG.info("Products retrived from DB for Namespace {}", namespaceCode);
				redisTemplate.opsForValue().set(namespaceCode, productIOs);
				redisTemplate.expire(namespaceCode, Duration.ofHours(2));
				LOG.info("After Products retrived from DB, stored in REDIS CACHE");
			}
		}
		return productIOs;
	}

	@Override
	public ProductIO getProductByCode(String code) {

		ProductIO productIO = (ProductIO) redisTemplate.opsForValue().get(code);
		if (productIO != null) {
			LOG.info("Product retrieved from REDIS CACHE for Product {}", code);
		}
		if (productIO == null) {
			productIO = productFeignClient.getProductByCode(code);
			if (productIO != null) {
				LOG.info("Product retrived from DB for Product Code : {}", code);
				redisTemplate.opsForValue().set(code, productIO);
				redisTemplate.expire(code, Duration.ofHours(2));
				LOG.info("After Product retrived from DB, stored in REDIS CACHE");
			}
		}
		return productIO;
	}

	@Override
	public ProductIO update(String token, ProductIO productIO) {
		ProductIO productIO2 = productFeignClient.update(token, productIO);
		if (productIO2 != null) {
			redisTemplate.opsForValue().set(productIO2.getCode(), productIO2);
			redisTemplate.expire(productIO2.getCode(), Duration.ofHours(2));
			redisTemplate.delete(productIO2.getNamespace().getCode());
			LOG.info("After Product is updated in DB, stored in REDIS CACHE and Cleared REDIS LIST CACHE");
		}
		return productIO2;
	}

	@Override
	public List<ProductIO> save(String namespaceCode) {
		List<ProductIO> productIOs = productFeignClient.getAllProducts(namespaceCode);

		return productDAO.save(productIOs);
	}

}
