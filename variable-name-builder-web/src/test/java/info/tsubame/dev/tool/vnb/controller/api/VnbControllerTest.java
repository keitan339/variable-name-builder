package info.tsubame.dev.tool.vnb.controller.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import info.tsubame.dev.tool.vnb.component.format.FormatType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class VnbControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private VnbController vnbController;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(vnbController).build();
    }

    @Test
    public void generate_001() throws Exception {
        // Request
        RequestBuilder builder = get("/service/generate")//
                .param("text", "定期預金") //
                .param("format", FormatType.FIELD);
        
        // Result
        MvcResult result = mockMvc.perform(builder).andExpect(status().isOk()).andReturn();
        String responseStr = result.getResponse().getContentAsString();
        
        assertThat(responseStr).isEqualTo("teikiYokin");
    }
}
