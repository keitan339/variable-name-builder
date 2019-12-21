package info.tsubame.dev.tool.vnb.component.ma;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.atilika.kuromoji.unidic.Token;
import com.atilika.kuromoji.unidic.Tokenizer;
import com.atilika.kuromoji.unidic.Tokenizer.Builder;
import com.ibm.icu.text.Transliterator;

@Component
public class MorphologicalAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(MorphologicalAnalyzer.class);

    private Tokenizer tokenizer = null;

    public MorphologicalAnalyzer() {
        setup();
    }

    private void setup() {
        try (InputStream edictStream = new ClassPathResource("kuromoji-dic-edict.csv").getInputStream();
                InputStream userStream = new ClassPathResource("kuromoji-dic-user.csv").getInputStream();
                InputStream resultStream = new SequenceInputStream(edictStream, userStream)) {
            Builder builder = new Tokenizer.Builder();
            tokenizer = builder.userDictionary(resultStream).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<WordData> execute(String text) {

        // 返却用の単語配列
        List<WordData> words = new ArrayList<>();

        // 形態素解析を実施
        List<Token> tokens = tokenizer.tokenize(text);

        // 解析結果をもとに表記と読みを取得
        for (int i = 0; i < tokens.size(); i++) {
            Token current = tokens.get(i);
            Token prev = (i > 0) ? tokens.get(i - 1) : null;

            if (logger.isTraceEnabled()) {
                logger.trace("--------------------------");
                logger.trace("getSurface():" + current.getSurface());
                logger.trace("getPartOfSpeechLevel1():" + current.getPartOfSpeechLevel1());
                logger.trace("getPartOfSpeechLevel2():" + current.getPartOfSpeechLevel2());
                logger.trace("getPartOfSpeechLevel3():" + current.getPartOfSpeechLevel3());
                logger.trace("getPartOfSpeechLevel4():" + current.getPartOfSpeechLevel4());
                logger.trace("getLemma():" + current.getLemma());
                logger.trace("getLemmaReadingForm():" + current.getLemmaReadingForm());
                logger.trace("getConjugationForm():" + current.getConjugationForm());
                logger.trace("getConjugationType():" + current.getConjugationType());
                logger.trace("getFinalSoundAlterationForm():" + current.getFinalSoundAlterationForm());
                logger.trace("getFinalSoundAlterationType():" + current.getFinalSoundAlterationType());
                logger.trace("getLanguageType():" + current.getLanguageType());
                logger.trace("getPronunciation():" + current.getPronunciation());
                logger.trace("getPronunciationBaseForm():" + current.getPronunciationBaseForm());
                logger.trace("getAllFeatures():" + current.getAllFeatures());
            }

            if (current.getLanguageType().equals("記号")) {
                String partOfSpeechLevel2 = current.getPartOfSpeechLevel2();
                if (!StringUtils.equals(partOfSpeechLevel2, "文字") && !StringUtils.equals(partOfSpeechLevel2, "一般")) {
                    continue;
                }
            }

            String surface = current.getSurface();
            String reading = getReading(current, prev);

            WordData wordData = new WordData(surface, reading);
            words.add(wordData);

        }

        // ローマ字１文字が続いた場合は、結合する。
        Transliterator full2halfTransliterator = Transliterator.getInstance("Fullwidth-Halfwidth");
        for (int i = 0; i < words.size(); i++) {
            WordData current = words.get(i);
            if (full2halfTransliterator.transliterate(current.getSurface()).matches("^[a-zA-Z]*$")) {
                if (i + 1 < words.size()) {
                    WordData next = words.get(i + 1);
                    if (next.getSurface().length() == 1
                            && full2halfTransliterator.transliterate(next.getSurface()).matches("^[a-zA-Z]*$")) {
                        current.setSurface(
                                full2halfTransliterator.transliterate(current.getSurface() + next.getSurface()));
                        current.setReading(current.getSurface());

                        words.remove(i + 1);
                        i = i - 1;
                    }
                }
            }
        }

        // 表記が1文字の場合は連結する。
        int beforeConcatIndex = 0;
        for (int i = 1; i < words.size(); i++) {
            WordData current = words.get(i);
            if (current.getSurface().length() == 1 && beforeConcatIndex != i) {

                // 一文字が平仮名一文字の場合は後ろと接続する。[お][客]→[お客]
                // if (current.getSurface().matches("[あ-ん]")) {
                if (current.getSurface().matches("[おかけごさど]")) {
                    WordData next = words.get(i + 1);
                    // 連結先がカタカナのみであれば、連結しない。（英語変換にむけて）
                    if (!allowConcat(next.getSurface())) {
                        continue;
                    }
                    next.setSurface(current.getSurface() + next.getSurface());
                    next.setReading(current.getReading() + next.getReading());

                    // 平仮名でなければ前と接続する。[適用][日]→[適用日]
                } else {
                    WordData prev = words.get(i - 1);
                    // 連結先がカタカナのみであれば、連結しない。（英語変換にむけて）
                    if (!allowConcat(prev.getSurface())) {
                        continue;
                    }
                    prev.setSurface(prev.getSurface() + current.getSurface());
                    prev.setReading(prev.getReading() + current.getReading());
                }

                beforeConcatIndex = i;
                words.remove(i);
                i = i - 1;

            }
        }
        if (words.get(0).getSurface().length() == 1 && words.size() > 1) {
            WordData current = words.get(0);
            WordData next = words.get(1);

            // 連結先がカタカナのみであれば、連結しない。（英語変換にむけて）
            if (allowConcat(next.getSurface())) {
                next.setSurface(current.getSurface() + next.getSurface());
                next.setReading(current.getReading() + next.getReading());
                words.remove(0);
            }
        }

        // 結果を返却
        return words;
    }

    private boolean allowConcat(String dest) {
        if (dest.matches("^[ァ-ヶー]*$") || dest.matches("^[a-zA-Z]*$")) {
            return false;
        } else {
            return true;
        }
    }

    private String getReading(Token token, Token beforeToken) {
        String reading;

        // 数字の場合は表記を採用
        if (token.getPartOfSpeechLevel2().equals("数詞")) {
            if (token.getSurface().matches("^[0-9]*$")) {
                reading = token.getSurface();

                // 漢数字表記を考慮
            } else {
                reading = token.getPronunciation();
            }

            // 読みが取れなかった場合（ローマ字単語とか？）は表記を採用
        } else if (token.getLemmaReadingForm().equals("*") && token.getLemma().equals("*")) {
            reading = token.getSurface();

            // ローマ字のみの場合は表記を採用
        } else if (token.getSurface().matches("^[a-zA-Z]*$")) {
            reading = token.getSurface();

        } else {

            // 「日」のみの場合、読みを「ビ」にする。ただし、直前の単語が数詞（１日→1/日など）の場合は、補正しない
            if (StringUtils.equals(token.getSurface(), "日")
                    && !StringUtils.equals(beforeToken.getPartOfSpeechLevel2(), "数詞")) {
                reading = "ビ";

            } else {
                if (!token.getLemmaReadingForm().equals("*")) {
                    if (StringUtils.contains(token.getPronunciation(), "ー")) {
                        reading = token.getLemmaReadingForm();
                    } else {
                        reading = token.getPronunciation();
                    }
                } else {
                    reading = token.getLemma();
                }
            }
        }

        return reading;
    }

}
