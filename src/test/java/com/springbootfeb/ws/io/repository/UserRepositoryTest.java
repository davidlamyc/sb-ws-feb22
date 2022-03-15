package com.springbootfeb.ws.io.repository;

import com.springbootfeb.ws.io.entity.AddressEntity;
import com.springbootfeb.ws.io.entity.UserEntity;
import com.springbootfeb.ws.io.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    static boolean recordsCreated = false;

    @BeforeEach
    void setUp() throws Exception {
        if (!recordsCreated) createRecords();
    }

    @Test
    final void testGetVerifiedUsers() {
        Pageable pageableRequest = PageRequest.of(0, 1);
        Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
        assertNotNull(pages);

        List<UserEntity> userEntities = pages.getContent();
        assertNotNull(userEntities);
        assertTrue(userEntities.size() == 1);
    }

    @Test
    final void testFindUserByFirstName() {
        String firstName = "Sergey";
        List<UserEntity> users = userRepository.findUserByFirstName(firstName);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getFirstName().equals(firstName));
    }

    @Test
    final void testFindUserByLastName() {
        String lastName = "Kargopolov";
        List<UserEntity> users = userRepository.findUserByLastName(lastName);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().equals(lastName));
    }

    @Test
    final void testFindUserByKeyword() {
        String keyword = "Kar";
        List<UserEntity> users = userRepository.findUserByKeyword(keyword);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(
                user.getLastName().contains(keyword) ||
                user.getLastName().contains(keyword));
    }

    @Test
    final void testFindUserFirstNameAndLastNameByKeyword() {
        String keyword = "Kar";
        List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        Object[] user = users.get(0);

        assertTrue(user.length == 2);

        String userFirstName = String.valueOf(user[0]);
        String userLastName = String.valueOf(user[1]);

        assertNotNull(userFirstName);
        assertNotNull(userLastName);

        System.out.println("First name = " + userFirstName);
        System.out.println("Last name = " + userLastName);
    }

    @Test
    final void testUpdateUserEmailVerificationStatus() {
        boolean newEmailVerificationStatus = false;
        userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "12dfaml");

        UserEntity storedUserDetails = userRepository.findByUserId("12dfaml");

        boolean storedEmailVerificationStatus = storedUserDetails.isEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    }

    private void createRecords() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Sergey");
        userEntity.setLastName("Kargopolov");
        userEntity.setUserId("12dfaml");
        userEntity.setEncryptedPassword("xxx");
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationStatus(true);

        AddressEntity addressEntity= new AddressEntity();
        addressEntity.setType("shipping");
        addressEntity.setAddressId("3948gj");
        addressEntity.setCity("Vancouver");
        addressEntity.setCountry("Canada");
        addressEntity.setPostalCode("ABC123");
        addressEntity.setStreetName("123 Street Name");

        List<AddressEntity> addresses = new ArrayList<>();
        addresses.add(addressEntity);

        userEntity.setAddresses(addresses);

        userRepository.save(userEntity);


        UserEntity userEntity2 = new UserEntity();
        userEntity2.setFirstName("Sergey");
        userEntity2.setLastName("Kargopolov");
        userEntity2.setUserId("12dfamlsdf");
        userEntity2.setEncryptedPassword("xxx");
        userEntity2.setEmail("test@test.com");
        userEntity2.setEmailVerificationStatus(true);

        AddressEntity addressEntity2 = new AddressEntity();
        addressEntity2.setType("shipping");
        addressEntity2.setAddressId("3948fdgj");
        addressEntity2.setCity("Vancouver");
        addressEntity2.setCountry("Canada");
        addressEntity2.setPostalCode("ABC123");
        addressEntity2.setStreetName("123 Street Name");

        List<AddressEntity> addresses2 = new ArrayList<>();
        addresses.add(addressEntity2);

        userEntity2.setAddresses(addresses2);

        userRepository.save(userEntity2);

        recordsCreated = true;
    }

}
