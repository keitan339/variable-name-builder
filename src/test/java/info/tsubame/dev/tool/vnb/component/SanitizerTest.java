package info.tsubame.dev.tool.vnb.component;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SanitizerTest {

    @Autowired
    private Sanitizer sanitizer;

    @Test
    public void execute_001() {
        String text = "限度額（１日あたり）";
        String result = sanitizer.execute(text);

        assertThat(result).isEqualTo("限度額（1日あたり）");
    }

    @Test
    public void execute_002() {
        String text = "限度額（1日あたり）";
        String result = sanitizer.execute(text);

        assertThat(result).isEqualTo("限度額（1日あたり）");
    }

    @Test
    public void execute_003() {
        String text = "限度額(1日あたり)";
        String result = sanitizer.execute(text);

        assertThat(result).isEqualTo("限度額(1日あたり)");
    }

    @Test
    public void execute_004() {
        String text = "NISA限度額";
        String result = sanitizer.execute(text);

        assertThat(result).isEqualTo("NISA限度額");
    }
}
