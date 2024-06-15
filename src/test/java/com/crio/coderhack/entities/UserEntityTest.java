package com.crio.coderhack.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserEntityTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser");
    }

    @Test
    void testConstructor() {
        assertEquals("testUser", user.getUsername());
        assertEquals(0, user.getScore());
        assertTrue(user.getBadges().isEmpty());
    }

    @Test
    void testAddBadge() {
        // Given
        Badge badge = Badge.Code_Champ;

        // When
        user.addBadge(badge);

        // Then
        assertTrue(user.getBadges().contains(badge));
    }
}
