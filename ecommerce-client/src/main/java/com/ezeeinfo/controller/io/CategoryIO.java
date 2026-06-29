package com.ezeeinfo.controller.io;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryIO extends BaseIO implements Serializable{
	private NamespaceIO namespace;
}
