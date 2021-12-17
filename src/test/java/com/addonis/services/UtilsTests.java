package com.addonis.services;

import com.addonis.exceptions.UnauthorizedOperationException;
import com.addonis.models.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.addonis.Helpers.createMockUserRole;

@ExtendWith(MockitoExtension.class)
public class UtilsTests {

    @Mock
    User user;

    @Test
    public void throwIfNotAdmin_Should_Throw_When_UserNotAdmin() {
        var role = createMockUserRole();

        Mockito.when(user.getRole()).thenReturn(role);

        Assertions.assertThrows(UnauthorizedOperationException.class, () ->
                Utils.throwIfNotAdmin(user));
    }
}
