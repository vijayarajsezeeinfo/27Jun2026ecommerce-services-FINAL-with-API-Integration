package com.ezeeinfo.controller.io;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductIO extends BaseIO implements Serializable{
	private String description;
	private Double price;
	private BrandIO brand;
	private CategoryIO category;
	private NamespaceIO namespace;
}
