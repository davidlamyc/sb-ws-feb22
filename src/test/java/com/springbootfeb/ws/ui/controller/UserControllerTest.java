package com.springbootfeb.ws.ui.controller;

import com.springbootfeb.ws.service.impl.UserServiceImpl;
import com.springbootfeb.ws.shared.dto.AddressDTO;
import com.springbootfeb.ws.shared.dto.UserDto;
import com.springbootfeb.ws.ui.model.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    UserDto userDto;

    final String USER_ID = "asdfv4324";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setFirstName("Sergey");
        userDto.setLastName("Kargopolov");
        userDto.setEmail("test@test.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setAddresses(getAddressesDto());
        userDto.setEncryptedPassword("adcjkn42");
        userDto.setAddresses(getAddressesDto());
    }

    @Test
    final void testGetUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserRest userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
//        assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
    }

    private void assertNotNull(UserRest userRest) {
    }

    private List<AddressDTO> getAddressesDto() {
        AddressDTO addressDto = new AddressDTO();
        addressDto.setType("shipping");
        addressDto.setCity("Vancouver");
        addressDto.setCountry("Canada");
        addressDto.setPostalCode("ABC123");
        addressDto.setStreetName("123 Street Name");

        AddressDTO billingAddressDto = new AddressDTO();
        billingAddressDto.setType("billing");
        billingAddressDto.setCity("Vancouver");
        billingAddressDto.setCountry("Canada");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("123 Street Name");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDto);
        addresses.add(billingAddressDto);

        return addresses;
    }

}
