package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private User.UserBuilder createBasicUser() {
        String TEST_LOGIN = "login";
        String TEST_EMAIL = "test@test.ru";
        LocalDate TEST_BIRTHADY = LocalDate.of(2000, Month.DECEMBER, 1);

        return User.builder()
            .id(1L)
            .login(TEST_LOGIN)
            .email(TEST_EMAIL)
            .birthday(TEST_BIRTHADY);
    }

    @Test
    void shouldNotValidateEmailIfBlank() {
        User user = createBasicUser().email("").build();

        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void shouldNotValidateEmailIfNull() {
        User user = createBasicUser().email(null).build();

        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void shouldNotValidateEmailIfNotEmail() {
        String[] wrongEmails = {"plainaddress",
            "#@%^%#$@#$@#.com",
            "@example.com",
            "Joe Smith <email@example.com>",
            "email.example.com",
            "email@example@example.com",
            ".email@example.com",
            "email.@example.com",
            "email..email@example.com",
            "email@example.com (Joe Smith)",
            "email@example",
            "email@-example.com",
            "email@111.222.333.44444",
            "email@example..com",
            "Abc..123@example.com"};

        Arrays.stream(wrongEmails).forEach(email -> {
            assertFalse(validator.validate(createBasicUser().email(email).build()).isEmpty());
        });
    }

    @Test
    void shouldNotValidateIfLoginIsNull() {
        User user = createBasicUser().login(null).build();

        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void shouldNotValidateIfLoginIsBlank() {
        User user = createBasicUser().login("").build();

        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void shouldNotValidateIfLoginHasWhiteSpaces() {
        String[] wtongLogins = {
            "l o g i n",
            "log in",
            "logi n",
            "login ",
            " login",
            " "
        };

        Arrays.stream(wtongLogins).forEach(login -> {
            assertFalse(validator.validate(createBasicUser().login(login).build()).isEmpty());
        });
    }

    @Test
    void shouldNotValidateIfBirthdayInFeature() {
        User user = createBasicUser().birthday(LocalDate.now().plusDays(1)).build();

        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void shouldValidateIfBirthdayNowOrBefore() {
        assertTrue(validator.validate(createBasicUser().birthday(LocalDate.now()).build()).isEmpty());
        assertTrue(validator.validate(createBasicUser().birthday(LocalDate.now().minusDays(1)).build()).isEmpty());
        assertTrue(validator.validate(createBasicUser().birthday(LocalDate.now().minusYears(1000)).build()).isEmpty());
    }
}