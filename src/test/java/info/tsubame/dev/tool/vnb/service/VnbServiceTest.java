package info.tsubame.dev.tool.vnb.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import info.tsubame.dev.tool.vnb.component.format.FormatterFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VnbServiceTest {

    @Autowired
    private VnbService vnbService;

    @Test
    public void build_001() {

        String text = "定期預金通帳";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("TeikiYokinTsucho");
    }

    @Test
    public void build_002() {

        String text = "引落口座番号";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("HikiotoshiKozaBango");
    }

    @Test
    public void build_003() {

        String text = "普通預金出金";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("FutsuYokinShukin");
    }

    @Test
    public void build_004() {

        String text = "限度額（１日あたり）";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("Gendogaku1nichiAtari");
    }

    @Test
    public void build_005() {

        String text = "限度額（1日あたり）";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("Gendogaku1nichiAtari");
    }

    @Test
    public void build_006() {

        String text = "限度額(1日あたり)";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("Gendogaku1nichiAtari");
    }

    @Test
    public void build_007() {

        String text = "キャッシュ";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("Cache");
    }

    @Test
    public void build_008() {

        String text = "キャッシュカード";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("CashCard");
    }

    @Test
    public void build_009() {

        String text = "サーバ";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("Server");
    }

    @Test
    public void build_010() {

        String text = "JNISA開設";
        String result = vnbService.build(text, FormatterFactory.CLASS);

        assertThat(result).isEqualTo("JnisaKaisetsu");
    }

    @Test
    public void build_011() {

        String text = "JNISA開設";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("jnisaKaisetsu");
    }

    @Test
    public void build_012() {

        String text = "JNISA開設";
        String result = vnbService.build(text, FormatterFactory.CONSTANT);

        assertThat(result).isEqualTo("JNISA_KAISETSU");
    }

    @Test
    public void build_013() {

        String text = "基準日";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("kijunbi");
    }

    @Test
    public void build_014() {

        String text = "適用開始日";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("tekiyoKaishibi");
    }

    @Test
    public void build_015() {

        String text = "限度額（一日あたり）";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("gendogakuIchinichiAtari");
    }

    @Test
    public void build_016() {

        String text = "パスワード";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("password");
    }

    @Test
    public void build_017() {

        String text = "ログインパスワード";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("loginPassword");
    }

    @Test
    public void build_018() {

        String text = "更新者";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("koshinsha");
    }

    @Test
    public void build_019() {

        String text = "ログインパスワードリセット要否";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("loginPasswordResetYohi");
    }

    @Test
    public void build_020() {

        String text = "生年月日";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("seinengapi");
    }

    @Test
    public void build_021() {

        String text = "不備事由マスタID";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("fubiJiyuMasterId");
    }

    @Test
    public void build_022() {

        String text = "定期預金（お客さま画面）";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("teikiYokinOkyakuSamaGamen");
    }

    @Test
    public void build_023() {

        String text = "旧Card";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("kyuCard");
    }

    @Test
    public void build_024() {

        String text = "おまとめ相手";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("omatomeAite");
    }

    @Test
    public void build_025() {

        String text = "最終更新年月日";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("saishuKoshinNengapi");
    }

    @Test
    public void build_026() {

        String text = "表示フォーマット";
        String result = vnbService.build(text, FormatterFactory.FIELD);

        assertThat(result).isEqualTo("hyojiFormat");
    }
}
