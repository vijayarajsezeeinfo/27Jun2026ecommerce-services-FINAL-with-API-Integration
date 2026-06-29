package com.ezeeinfo.controller.io;

import java.io.Serializable;

import lombok.Data;

@Data
public class BaseIO implements Serializable {
	private String code;
	private String name;
	private Integer activeFlag;
}
