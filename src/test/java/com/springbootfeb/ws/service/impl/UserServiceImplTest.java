package com.springbootfeb.ws.service.impl;

import com.springbootfeb.ws.exceptions.UserServiceException;
import com.springbootfeb.ws.io.entity.AddressEntity;
import com.springbootfeb.ws.io.entity.UserEntity;
import com.springbootfeb.ws.io.repositories.UserRepository;
import com.springbootfeb.ws.shared.Utils;
import com.springbootfeb.ws.shared.dto.AddressDTO;
import com.springbootfeb.ws.shared.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String userId = "hhty57ehfy";
    String encryptedPassword = "gre4342";

    UserEntity userEntity;

//    @Mock
//    PasswordResetTokenRepository passwordResetTokenRepository;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Sergey");
        userEntity.setLastName("Kargopolov");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationToken("vw4e4v");
        userEntity.setAddresses(getAddressesEntity());
    }

    @Test
    final void testGetUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("Sergey", userDto.getFirstName());
    }

    @Test
    final void testGetUser_UsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUser("test@test.com");
        });
    }

    @Test
    final void testCreateUser_CreateUserServiceException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("Sergey");
        userDto.setLastName("Kargopolov");
        userDto.setPassword("12345678");
        userDto.setEmail("test@test.com");

        assertThrows(UserServiceException.class, () -> {
            userService.createUser(userDto);
        });
    }

    @Test
    final void testCreateUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("fada33sdf");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("Sergey");
        userDto.setLastName("Kargopolov");
        userDto.setPassword("12345678");
        userDto.setEmail("test@test.com");

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
        verify(utils, times(2)).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("12345678");
        verify(userRepository, times(1)).save(any(UserEntity.class));
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

    private List<AddressEntity> getAddressesEntity() {
        List<AddressDTO> addresses = getAddressesDto();

        Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
        return new ModelMapper().map(addresses, listType);
    }
}
