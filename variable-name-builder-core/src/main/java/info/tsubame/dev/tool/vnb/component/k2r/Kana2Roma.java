package info.tsubame.dev.tool.vnb.component.k2r;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import lombok.Data;

class Kana2Roma {

    private static final char[] BOIN = {'A', 'I', 'U', 'E', 'O'};

    private static final List<Shiin> SHIIN_LIST = new ArrayList<>();

    private static String KANA_CHARS = null;

    static {

        SHIIN_LIST.add(new Shiin("アイウエオ", ""));
        SHIIN_LIST.add(new Shiin("カキクケコ", "K"));
        SHIIN_LIST.add(new Shiin("サ／スセソ", "S"));
        SHIIN_LIST.add(new Shiin("／シ／／／", "SH")); // ヘボン式対応（「シ」はSIではなく、SHI）
        SHIIN_LIST.add(new Shiin("タ／／テト", "T"));
        SHIIN_LIST.add(new Shiin("／／ツ／／", "TS")); // ヘボン式対応（「ツ」はTUではなく、TSU）
        SHIIN_LIST.add(new Shiin("／チ／／／", "CH")); // ヘボン式対応（「チ」はTIではなく、CHI）
        SHIIN_LIST.add(new Shiin("ナニヌネノ", "N"));
        SHIIN_LIST.add(new Shiin("ハヒ／ヘホ", "H")); // ヘボン式対応（「フ」はHUではなく、FU）
        SHIIN_LIST.add(new Shiin("／／フ／／", "F"));
        SHIIN_LIST.add(new Shiin("マミムメモ", "M"));
        SHIIN_LIST.add(new Shiin("ヤ／ユ／ヨ", "Y"));
        SHIIN_LIST.add(new Shiin("ラリルレロ", "R"));
        SHIIN_LIST.add(new Shiin("ワ／／／ヲ", "W"));
        SHIIN_LIST.add(new Shiin("ガギグゲゴ", "G"));
        SHIIN_LIST.add(new Shiin("ザ／ズゼゾ", "Z")); // ヘボン式対応（「ジ」はZIではなく、JI）
        SHIIN_LIST.add(new Shiin("／ジ／／／", "J"));
        SHIIN_LIST.add(new Shiin("ダ／ヅデド", "D"));
        SHIIN_LIST.add(new Shiin("／ヂ／／／", "J")); // ヘボン式対応（「ヂ」はDIではなく、JI）

        SHIIN_LIST.add(new Shiin("バビブベボ", "B"));
        SHIIN_LIST.add(new Shiin("パピプペポ", "P"));

        KANA_CHARS = "";
        for (Shiin shin : SHIIN_LIST) {
            KANA_CHARS = KANA_CHARS + shin.getKana();
        }
    }

    @Data
    private static class Shiin {
        private String kana;
        private String roma;

        Shiin(String kana, String roma) {
            this.kana = kana;
            this.roma = roma;
        }
    }

    // 文字cが、AIUEOのどれかであれば、その位置を返す。
    private static int isBoin(char c) {
        for (int i = 0; i < BOIN.length; ++i) {
            if (BOIN[i] == c) {
                return i;
            }
        }

        return -1;
    }

    public String convert(String text) throws Kana2RomaException {
        if (StringUtils.isEmpty(text)) {
            return text;
        }

        StringBuilder sb = new StringBuilder();

        int len = text.length();
        char prevChar = (char) 0;

        for (int i = 0; i < len; ++i) {
            char c = text.charAt(i);
            kc2romajisb(c, sb, prevChar);

            prevChar = c;
        }

        return sb.toString();
    }

    private void kc2romajisb(char c, StringBuilder sb, char prevChar) throws Kana2RomaException {
        int pos = KANA_CHARS.indexOf(c);

        if (pos >= 0) {

            int gyoIdx = pos / 5; // 行インデクス 0:ア行、1:カ行、2:サ行 …
            int danIdx = pos % 5; // 段インデクス 0:ア段、2:イ段、3:ウ段 …

            // 現在のバッファの最後の文字を取得
            char lastChar = (sb.length() > 0 ? sb.charAt(sb.length() - 1) : (char) 0);

            // すでにバッファに文字があり、かつ入力された文字が、ア行の場合
            if (sb.length() > 0 && gyoIdx == 0) {

                // バッファの末尾と次の文字が同じ場合、この文字を追加しない。
                if (isBoin(lastChar) == danIdx) { // オオノはOONOではなくONO
                    return;
                }

                if (isBoin(lastChar) == 4 && danIdx == 2) { // サトウはSATOUではなくSATO
                    return;
                }
            }

            // バッファの末尾の「ッ」を削除
            if (lastChar == 'ッ') {
                sb.deleteCharAt(sb.length() - 1);
            }

            // 入力された文字の、先頭ローマ字を取得
            String rh = SHIIN_LIST.get(gyoIdx).getRoma();

            /*
            char rhc = (rh.length() > 0 ? rh.charAt(0) : (char) 0);
            // 現在のバッファの末尾が「N」で、かつ続くアルファベットがB,M,Pの場合
            if (sb.toString().endsWith("N") && (rhc == 'B' || rhc == 'M' || rhc == 'P')) {
                sb.deleteCharAt(sb.length() - 1); // 末尾の「N」を「M」に変換
                sb.append('M');
            }
            // CHの前に「ッ」がある場合、CCHではなくTCHとする。
            if (rh.equals("CH"))
                rhc = 'T';
            */


            // sb.append((lastChar == 'ッ' ? rhc + rh : rh) + BOIN[d_idx]);
            sb.append(rh + BOIN[danIdx]);

        } else {

            // 数値であれば、そのまま追加する。
            if (String.valueOf(c).matches("[0-9]")) {
                sb.append(c);

                // ローマ字であれば、そのまま追加する。
            } else if (String.valueOf(c).matches("[a-zA-Z]")) {
                sb.append(c);

            } else {

                switch (c) {
                    case 'ッ': // 促音(小さい「ッ」の場合

                        // もし先頭である場合は例外を発生
                        if (sb.length() == 0) {
                            throw new Kana2RomaException(Kana2RomaException.Type.ILLEGAL_HEAD_CHAR, "ッ");
                        }

                        // もしバッファの末尾も「ッ」の場合、不正なカナの並びとして例外を発生
                        if (sb.charAt(sb.length() - 1) == 'ッ') {
                            throw new Kana2RomaException(Kana2RomaException.Type.ILLEGAL_KANA_SEQUENCE, "ッッ");
                        }

                        // とりあえずそのままバッファに追加
                        sb.append(c);
                        break;
                    case 'ン': // 撥音「ン」は、'N'に変換
                        sb.append('N');
                        break;
                    case 'ャ':
                    case 'ュ':
                    case 'ョ':
                        // もし先頭である場合は例外を発生
                        if (sb.length() == 0) {
                            throw new Kana2RomaException(Kana2RomaException.Type.ILLEGAL_HEAD_CHAR, "" + c);
                        }

                        char[] checks = {'ッ', 'ャ', 'ュ', 'ョ'};
                        for (char check : checks) {
                            if (prevChar == check) { // バッファ末尾が「ッ」もしくは「ャュョ」の場合、例外を発生
                                throw new Kana2RomaException(Kana2RomaException.Type.ILLEGAL_KANA_SEQUENCE,
                                        "" + prevChar + c);
                            }
                        }

                        if (sb.toString().endsWith("I")) { // バッファ末尾の「I」を削除
                            sb.deleteCharAt(sb.length() - 1);
                        }

                        // ヘボン式対応(チャ,チュ,チョは、CHA,CHU,CHOにする。)
                        if (sb.toString().endsWith("T")) { // バッファ末尾が「T」なら「CH」に変換
                            sb.deleteCharAt(sb.length() - 1);
                            sb.append("CH" + BOIN["ャ/ュ/ョ".indexOf(c)]);
                        } else if (sb.toString().endsWith("SH") || sb.toString().endsWith("CH")
                                || sb.toString().endsWith("J")) {
                            sb.append(BOIN["ャ/ュ/ョ".indexOf(c)]);
                        } else {
                            sb.append("Y" + BOIN["ャ/ュ/ョ".indexOf(c)]);
                        }

                        break;
                    case 'ー':
                        break;

                    default: // 処理できない文字が入力された旨の例外を発生
                        throw new Kana2RomaException(Kana2RomaException.Type.NOT_KATAKANA_CHARACTER, "" + c);
                }
            }

        }
    }
}
