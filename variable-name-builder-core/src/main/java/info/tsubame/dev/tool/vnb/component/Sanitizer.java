package info.tsubame.dev.tool.vnb.component;

import org.springframework.stereotype.Component;
import com.ibm.icu.text.Transliterator;

@Component
public class Sanitizer {

    public String execute(String text) {

        StringBuffer sb = new StringBuffer();

        Transliterator full2halfTransliterator = Transliterator.getInstance("Fullwidth-Halfwidth");

        for (int i = 0; i < text.length(); i++) {

            // 全角数字
            if (String.valueOf(text.charAt(i)).matches("[０-９]")) {
                sb.append(full2halfTransliterator.transliterate(String.valueOf(text.charAt(i))));

                // 全角ローマ字
            } else if (String.valueOf(text.charAt(i)).matches("[ａ-ｚＡ-Ｚ]")) {
                sb.append(full2halfTransliterator.transliterate(String.valueOf(text.charAt(i))));

            } else {
                sb.append(text.charAt(i));
            }
        }

        return sb.toString();
    }
}
