package io.github.kevinlaig.backend.configuration;

import io.github.kevinlaig.backend.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for security configuration.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    public void shouldGiveForbiddenIfNoToken() throws Exception {
        mockMvc.perform(get("/api/admin/secure-endpoint"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    public void shouldGiveOkIfValidTokenForAdmin() throws Exception {
        String token = jwtUtil.generateToken("testAdmin");

        mockMvc.perform(get("/api/admin/secure-endpoint")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
