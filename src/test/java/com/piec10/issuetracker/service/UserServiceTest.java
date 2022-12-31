package com.piec10.issuetracker.service;

import com.piec10.issuetracker.dao.RoleRepository;
import com.piec10.issuetracker.dao.UserRepository;
import com.piec10.issuetracker.entity.Role;
import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.service.UserService;
import com.piec10.issuetracker.form.FormUser;
import com.piec10.issuetracker.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Captor
    private ArgumentCaptor<User> capturedUser;

    private FormUser formUser;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Test
    public void createNewValidUserService() {

        formUser = new FormUser();
        formUser.setUsername("username");
        formUser.setEmail("email@email.com");
        formUser.setPassword("password");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(new Role("ROLE_USER"));
        userService.save(formUser);

        verify(userRepository).save(capturedUser.capture());

        assertEquals(formUser.getUsername(), capturedUser.getValue().getUsername(), "Username should match");
        assertEquals(formUser.getEmail(), capturedUser.getValue().getEmail(), "User email should match");

        assertTrue(passwordEncoder.matches(formUser.getPassword(), capturedUser.getValue().getPassword()), "User encoded password should match");

        assertEquals(1, capturedUser.getValue().getEnabled(), "User should be enabled by default");

        assertNotNull(capturedUser.getValue().getCreatedAt(), "Issue creation date should not be null");
        assertTrue(new Date().getTime() > capturedUser.getValue().getCreatedAt().getTime(), "User creation date should be older than current date");

        assertNull(capturedUser.getValue().getIssues(), "New user should not have any open issues");

        assertEquals(1, capturedUser.getValue().getRoles().size(), "New user should have one default role");
        assertEquals("ROLE_USER", capturedUser.getValue().getRoles().stream().toList().get(0).getName(), "New user should have default role 'ROLE_USER'");
    }

    @Test
    public void changePasswordService() {

        User user = new User();
        user.setUsername("user");
        user.setPassword("oldPassword");
        user.setEmail("user@email.com");
        Date createdAt = new Date();
        user.setCreatedAt(createdAt);
        Role role1 = new Role();
        Role role2 = new Role();
        role1.setName("ROLE_USER");
        role1.setId(1L);
        role2.setName("ROLE_ADMIN");
        role2.setId(2L);
        user.setRoles(Arrays.asList(role1,role2));
        user.setEnabled(1);

        String newPassword = "newPassword";

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        userService.changePassword(user.getUsername(), newPassword);

        verify(userRepository).save(capturedUser.capture());

        assertTrue(passwordEncoder.matches(newPassword, capturedUser.getValue().getPassword()), "Password should be updated");

        assertEquals(user.getUsername(), capturedUser.getValue().getUsername(), "Username should not be changed");
        assertEquals(user.getEmail(), capturedUser.getValue().getEmail(), "Email should not be changed");
        assertEquals(createdAt, capturedUser.getValue().getCreatedAt(), "Creation date should not be changed");
        assertEquals(user.getEnabled(), capturedUser.getValue().getEnabled(), "Enabled flag should not be changed");
        assertEquals(user.getRoles().size(), capturedUser.getValue().getRoles().size(), "Number of roles should not be changed");
        assertEquals(user.getRoles().stream().toList().get(0).getName(), capturedUser.getValue().getRoles().stream().toList().get(0).getName(), "First role name should not be changed");
        assertEquals(user.getRoles().stream().toList().get(1).getName(), capturedUser.getValue().getRoles().stream().toList().get(1).getName(), "Second role name should not be changed");

    }
}
