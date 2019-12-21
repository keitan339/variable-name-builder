package info.tsubame.dev.tool.vnb.component.k2r;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RomaConverterTest {

    @Autowired
    private RomaConverter romaConverter;

    @Test
    public void execute_001() {

        String kana = "テイキ";
        String result = romaConverter.execute(kana);

        assertThat(result).isEqualTo("teiki");
    }

    @Test
    public void execute_002() {

        String kana = "ニュウキン";
        String result = romaConverter.execute(kana);

        assertThat(result).isEqualTo("nyukin");
    }

    @Test
    public void execute_003() {

        String kana = "フツウ";
        String result = romaConverter.execute(kana);

        assertThat(result).isEqualTo("futsu");
    }

    @Test
    public void execute_004() {

        String kana = "シュッキン";
        String result = romaConverter.execute(kana);

        assertThat(result).isEqualTo("shukin");
    }

    @Test
    public void execute_005() {

        String kana = "バンゴウ";
        String result = romaConverter.execute(kana);

        assertThat(result).isEqualTo("bango");
    }
}
