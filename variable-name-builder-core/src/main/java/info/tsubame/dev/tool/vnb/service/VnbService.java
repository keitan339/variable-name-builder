package info.tsubame.dev.tool.vnb.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import info.tsubame.dev.tool.vnb.component.Sanitizer;
import info.tsubame.dev.tool.vnb.component.format.FormatterFactory;
import info.tsubame.dev.tool.vnb.component.k2e.EnglishConverter;
import info.tsubame.dev.tool.vnb.component.k2r.RomaConverter;
import info.tsubame.dev.tool.vnb.component.ma.MorphologicalAnalyzer;
import info.tsubame.dev.tool.vnb.component.ma.WordData;

@Service
public class VnbService {

    @Autowired
    private Sanitizer sanitizer;

    @Autowired
    private MorphologicalAnalyzer morphologicalAnalyzer;

    @Autowired
    private EnglishConverter englishConverter;

    @Autowired
    private RomaConverter romaConverter;

    @Autowired
    private FormatterFactory formatterFactory;

    public String build(String text, String format) {

        // ブランクの場合、そのまま返却する
        if (StringUtils.isEmpty(text)) {
            return text;
        }

        // サニタイズ
        String sanitizeText = sanitizer.execute(text);

        // 形態素解析
        List<WordData> words = morphologicalAnalyzer.execute(sanitizeText);

        // ローマ字変換
        List<String> romas = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            WordData wordData = words.get(i);

            // カタカナのみで構成されており、かつ次の単語もカタカナで構成されていた場合、連結する。
            List<WordData> concatedWordData = new ArrayList<>();
            if (isAllKana((wordData.getSurface()))) {

                for (int j = i + 1; j < words.size(); j++) {
                    WordData nextWordData = words.get(j);
                    if (isAllKana(nextWordData.getSurface())) {
                        concatedWordData.add(nextWordData);
                        wordData.setSurface(wordData.getSurface() + nextWordData.getSurface());
                        wordData.setReading(wordData.getReading() + nextWordData.getReading());
                        i++;
                    } else {
                        break;
                    }
                }
            }
            // カナ連結していた場合、英語辞書に登録があるかをチェックし、無ければ連結しない状態に戻す。
            for (int j = concatedWordData.size(); j > 0; j--) {
                if (englishConverter.canConvert(wordData.getSurface())) {
                    break;
                } else {
                    wordData.setSurface(
                            StringUtils.removeEnd(wordData.getSurface(), concatedWordData.get(j - 1).getSurface()));
                    wordData.setReading(
                            StringUtils.removeEnd(wordData.getReading(), concatedWordData.get(j - 1).getReading()));
                    i--;
                }
            }

            // 英語変換が可能であれば、英語返還を実施
            if (englishConverter.canConvert(wordData.getSurface())) {
                romas.addAll(Arrays.asList(englishConverter.execute(wordData.getSurface())));
            } else {
                romas.add(romaConverter.execute(wordData.getReading()));
            }

        }

        return formatterFactory.create(format).format(romas);
    }

    private boolean isAllKana(String text) {
        return Pattern.matches("^[ァ-ヶー]*$", text);
    }
}
