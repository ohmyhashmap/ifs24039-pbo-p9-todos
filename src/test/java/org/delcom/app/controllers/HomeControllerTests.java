package org.delcom.app.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerUnitTest {
    @Test
    @DisplayName("Mengembalikan pesan selamat datang yang benar")
    void hello_ShouldReturnWelcomeMessage() throws Exception {
        // Arrange
        HomeController controller = new HomeController();

        // Act
        String result = controller.hello();

        // Assert
        assertEquals("Hay, selamat datang di Spring Boot!", result);
    }
}
