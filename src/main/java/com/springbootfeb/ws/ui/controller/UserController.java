package com.springbootfeb.ws.ui.controller;

import com.springbootfeb.ws.service.AddressService;
import com.springbootfeb.ws.service.UserService;
import com.springbootfeb.ws.shared.dto.AddressDTO;
import com.springbootfeb.ws.shared.dto.UserDto;
import com.springbootfeb.ws.ui.model.request.UserDetailsRequestModel;
import com.springbootfeb.ws.ui.model.response.AddressesRest;
import com.springbootfeb.ws.ui.model.response.OperationStatusModel;
import com.springbootfeb.ws.ui.model.response.RequestOperationStatus;
import com.springbootfeb.ws.ui.model.response.UserRest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @ApiOperation(value="Get User Details", notes="This is the description.")
    @GetMapping(path="/{id}")
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetails, userDto);

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path="/{id}")
    public UserRest updateUser(
            @PathVariable String id,
            @RequestBody UserDetailsRequestModel userDetails
    ) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path="/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationsName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Bearer JWT Token", paramType="header")
    })
    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="0") int page,
                                   @RequestParam(value="limit", defaultValue="25") int limit) {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto: users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    @GetMapping(path="/{id}/addresses")
    public List<AddressesRest> getUserAddresses(@PathVariable String id) {
        List<AddressesRest> returnValue = new ArrayList<>();

        List<AddressDTO> addressesDTO = addressService.getAddresses(id);

        if (addressesDTO != null && !addressesDTO.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
            returnValue = new ModelMapper().map(addressesDTO, listType);
        }

        return returnValue;
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}")
    public AddressesRest getUserAddress(@PathVariable String addressId) {
        AddressDTO addressesDTO = addressService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(addressesDTO, AddressesRest.class);
    }
}
