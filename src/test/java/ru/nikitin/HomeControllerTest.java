package ru.nikitin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.nikitin.controllers.HomeController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;   // внедряем MockMVC

    @Test
    public void testHomePage() throws Exception{
        mockMvc.perform(get("/"))                       //выполнить запрос GET
                .andExpect(status().isOk())                       //Ожидается код ответа HTTP 200
                .andExpect(view().name("home"))   //Ожидается имя представления "home"
                .andExpect(content().string(
                        containsString("Welcome to ...") //Ожидается наличие строки "Welcome to..."
                ));
    }
}
