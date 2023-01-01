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

    private final String TEST_EMAIL = "test@test.ru";
    private final String TEST_LOGIN = "login";
    private final LocalDate TEST_BIRTHADY = LocalDate.of(2000, Month.DECEMBER, 1);

    @Test
    void shouldNotValidateEmailIfBlank() {
        User user = new User("", TEST_LOGIN, TEST_BIRTHADY);

        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void shouldNotValidateEmailIfNull() {
        User user = new User(null, TEST_LOGIN, TEST_BIRTHADY);

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
            System.out.println(email);
            assertFalse(validator.validate(new User(email, TEST_LOGIN, TEST_BIRTHADY)).isEmpty());
        });
    }

    @Test
    void shouldNotValidateIfLoginIsNull() {
        User user = new User(TEST_EMAIL, null, TEST_BIRTHADY);

        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void shouldNotValidateIfLoginIsBlank() {
        User user = new User(TEST_EMAIL, "", TEST_BIRTHADY);

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
            assertFalse(validator.validate(new User(TEST_EMAIL, login, TEST_BIRTHADY)).isEmpty());
        });
    }

    @Test
    void shouldNotValidateIfBirthdayInFeature() {
        User user = new User(TEST_EMAIL, TEST_LOGIN, LocalDate.now().plusDays(1));

        assertFalse(validator.validate(user).isEmpty());
    }

    @Test
    void shouldValidateIfBirthdayNowOrBefore() {
        assertTrue(validator.validate(new User(TEST_EMAIL, TEST_LOGIN, LocalDate.now())).isEmpty());
        assertTrue(validator.validate(new User(TEST_EMAIL, TEST_LOGIN, LocalDate.now().minusDays(1))).isEmpty());
        assertTrue(validator.validate(new User(TEST_EMAIL, TEST_LOGIN, LocalDate.now().minusYears(1000))).isEmpty());
    }
}