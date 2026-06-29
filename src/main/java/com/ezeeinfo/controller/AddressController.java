package com.ezeeinfo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.controller.io.AddressIO;
import com.ezeeinfo.controller.io.NamespaceIO;
import com.ezeeinfo.controller.io.UserIOResponse;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.AddressDTO;
import com.ezeeinfo.dto.NamespaceDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	AddressService addressService;
	@Autowired
	UserController userController;
	@Autowired
	NamespaceController namespaceController;
	@Autowired
	UserDAO userDAO;

	private static final Logger LOG = LoggerFactory.getLogger(AddressController.class);

	@RequestMapping(value = "/{namespaceCode}", method = RequestMethod.GET)
	public List<AddressIO> getAllAddresses(@PathVariable("namespaceCode") String namespaceCode) {
		List<AddressIO> allAddresses = addressService.getAllAddresses(namespaceCode).stream().map(dto -> addressDTOToIO(dto)).toList();
		LOG.info("Getting all addresses in Namespace {} : {}", namespaceCode, allAddresses);
		return allAddresses;
	}

	@RequestMapping(value = "/code/{code}", method = RequestMethod.GET)
	public AddressIO getAddressByCode(@PathVariable("code") String code) {
		AddressIO address = addressDTOToIO(addressService.getAddressByCode(code));
		LOG.info("Getting address for Code {} : {}", code, address);
		return address;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public AddressIO update(@RequestBody AddressIO addressIO, HttpServletRequest request) throws Exception {
		LOG.info("Input Address for Save or Update : {}", addressIO);
		AddressDTO addressDTO = addressService.update(addressIOToDTO(addressIO),request);
		return addressDTOToIO(addressDTO);
	}

	public AddressIO addressDTOToIO(AddressDTO addressDTO) {
		LOG.info("Input for Address DTO to IO : {}", addressDTO);
		UserIOResponse userIO = userController.userDTOToIO(addressDTO.getUser());
		NamespaceIO namespaceIO = namespaceController.namespaceDTOToIO(addressDTO.getNamespace());
		AddressIO addressIO = new AddressIO();
		addressIO.setCode(addressDTO.getCode());
		addressIO.setDoorNo(addressDTO.getDoorNo());
		addressIO.setStreet(addressDTO.getStreet());
		addressIO.setPlace(addressDTO.getPlace());
		addressIO.setCity(addressDTO.getCity());
		addressIO.setState(addressDTO.getState());
		addressIO.setCountry(addressDTO.getCountry());
		addressIO.setPincode(addressDTO.getPincode());
		addressIO.setUser(userIO);
		addressIO.setNamespace(namespaceIO);
		addressIO.setActiveFlag(addressDTO.getActiveFlag());
		LOG.info("output for Address DTO to IO : {}", addressIO);
		return addressIO;
	}

	public AddressDTO addressIOToDTO(AddressIO addressIO) throws Exception {
		LOG.info("Input for Address IO to DTO: {}", addressIO);
		UserDTO userDTO = userDAO.getUserByCode(addressIO.getUser().getCode());
		NamespaceDTO namespaceDTO = namespaceController.namespaceIOToDTO(addressIO.getNamespace());
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setCode(addressIO.getCode());
		addressDTO.setDoorNo(addressIO.getDoorNo());
		addressDTO.setStreet(addressIO.getStreet());
		addressDTO.setPlace(addressIO.getPlace());
		addressDTO.setCity(addressIO.getCity());
		addressDTO.setState(addressIO.getState());
		addressDTO.setCountry(addressIO.getCountry());
		addressDTO.setPincode(addressIO.getPincode());
		addressDTO.setUser(userDTO);
		addressDTO.setNamespace(namespaceDTO);
		addressDTO.setActiveFlag(addressIO.getActiveFlag());
		LOG.info("Output for Address IO to DTO: {}", addressDTO);
		return addressDTO;
	}
}
