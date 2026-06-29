package com.ezeeinfo.config;

import java.util.List;

import com.ezeeinfo.controller.io.ProductIO;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface ProductFeignClient {

	@RequestLine("GET /product/{namespaceCode}")
	List<ProductIO> getAllProducts(@Param("namespaceCode") String namespaceCode);

	@RequestLine("GET /product/code/{code}")
	ProductIO getProductByCode(@Param("code") String code);

	@RequestLine("POST /product/update")
	@Headers({ "Content-Type:application/json", "Authorization: {token}" })
	ProductIO update(@Param("token") String token, ProductIO productIO);
}
