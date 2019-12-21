package info.tsubame.dev.tool.vnb.component.ma;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import info.tsubame.dev.tool.vnb.TestConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, initializers = ConfigFileApplicationContextInitializer.class)
public class MorphologicalAnalyzerTest {

    @Autowired
    private MorphologicalAnalyzer morphologicalAnalyzer;

    @Test
    public void execute_001() {
        String text = "定期預金通帳";
        List<WordData> result = morphologicalAnalyzer.execute(text);

        Assertions.assertThat(result).containsExactly(new WordData("定期", "テイキ"), new WordData("預金", "ヨキン"),
                new WordData("通帳", "ツウチョウ"));
    }

    @Test
    public void execute_002() {
        String text = "引落口座番号";
        List<WordData> result = morphologicalAnalyzer.execute(text);

        Assertions.assertThat(result).containsExactly(new WordData("引落", "ヒキオトシ"), new WordData("口座", "コウザ"),
                new WordData("番号", "バンゴウ"));
    }

    @Test
    public void execute_003() {
        String text = "限度額(1日あたり)";
        List<WordData> result = morphologicalAnalyzer.execute(text);

        Assertions.assertThat(result).containsExactly(new WordData("限度額", "ゲンドガク"), new WordData("1日", "1ニチ"),
                new WordData("あたり", "アタリ"));
    }

    @Test
    public void execute_004() {
        String text = "キャッシュカード";
        List<WordData> result = morphologicalAnalyzer.execute(text);

        Assertions.assertThat(result).containsExactly(new WordData("キャッシュカード", "キャッシュカード"));
    }

    @Test
    public void execute_005() {
        String text = "NISA限度額";
        List<WordData> result = morphologicalAnalyzer.execute(text);

        Assertions.assertThat(result).containsExactly(new WordData("NISA", "NISA"), new WordData("限度額", "ゲンドガク"));
    }
}
