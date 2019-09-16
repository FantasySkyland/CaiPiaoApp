package com.example.caipiaoapp.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import com.example.base.base.App;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final String TAG = "StringUtil";

    private static final String REGEX_PHONE = "\\d{11}$";

    /**
     * 字符串是否是空
     */
    public static boolean isEmpty(String temp) {

        return temp == null || temp.trim().length() == 0;
    }

    /**
     * 字符串是否是空（\t,\r,\n均为空）
     */
    public static boolean isEmptyExtra(String input) {
        if (input == null || "".equals(input)) {
            return true;
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是数字开头
     */
    public static boolean isNumOpen(String text) {

        if (StringUtil.isEmpty(text)) {
            return false;
        }

        char s = text.charAt(0);

        if (s >= '0' && s <= '9') {
            return true;
        }
        return false;
    }

    /**
     * 是否是数字开头
     */
    public static boolean isNum(String text) {

        if (StringUtil.isEmpty(text)) {
            return false;
        }

        char s = text.charAt(0);

        if (s >= '0' && s <= '9') {
            return true;
        }
        return false;
    }

    private static final boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    private static final boolean isChineseFont(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
            return true;
        }
        return false;
    }

    public static final boolean isChinese(String strName) {

        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    public static final boolean isValidID(String ID) {
        char[] ch = ID.toCharArray();
        if (ch.length != 18) {
            return false;
        }
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            // 最后一位可以为x
            if (i == ch.length - 1) {
                if (!(c >= '0' && c <= '9' || c == 'x' || c == 'X')) {
                    return false;
                }
            } else {
                if (!(c >= '0' && c <= '9')) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isLegalPoliceOrgName(String orgName) {
        return orgName.matches(REGULAR_POLICE_ORG);
    }

    /** 公安机关 */
    public static final String REGULAR_POLICE_ORG = "^[0-9\\u4e00-\\u9fa5]+$";

    /** 十五位纯数字 或者18位 尾位置可能是x 的身份证 */
    public static final String REGULAR_EXP_ID = "^\\d{15}(?:\\d{3}|\\d{2}[xX])?$";

    /** 中文名 还有新疆人名字带点的情况 */
    public static final String REGULAR_EXP_REALNAME
            = "^[\\u4e00-\\u9fa5]+(?:[.·．•][\\u4e00-\\u9fa5]+)*$";

    /** 只能输入中文和英文 */
    public static final String REGULAR_EXP_CE = "^[A-Za-z\\u4e00-\\u9fa5]*$";

    public static boolean isValidIdByRegularExp(String Id) {
        return Id.matches(REGULAR_EXP_ID);

    }

    public static boolean isValidRealNameByRegularExp(String realName) {

        return realName.matches(REGULAR_EXP_REALNAME);
    }

    public static boolean isValidNameByRegularExp(String name) {
        return name.matches(REGULAR_EXP_CE);
    }

    public static final boolean isValidRelName(String strName) {

        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!(isChineseFont(c) || c == '.')) {
                return false;
            }
        }
        return true;
    }


    /**
     * 若连接资源中有中文字符 则encode转码
     */
    public static String getEncodeUrl(String url) {

        String fileName = "";
        String encodeUrl = "";
        // 从路径中获取
        if (url == null || "".equals(url.trim())) {
            return "";
        }
        fileName = url.substring(url.lastIndexOf("/") + 1);
        if (StringUtil.isChinese(fileName)) {
            try {
                encodeUrl = url.replace(fileName, URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "", e);
            }
        } else {
            return url;
        }
        return encodeUrl;
    }

    public static String getReturn(String str) {
        Float f = Float.parseFloat(str);
        if (f > 10000 * 10000) {
            return String.format("%.3f亿", f / 10000 / 10000);
        } else if (f > 10000) {
            return String.format("%.3f万", f / 10000);
        } else {
            return str;
        }
    }

    public static boolean isLetterOpen(String text) {

        if (StringUtil.isEmpty(text)) {
            return false;
        }

        char s = text.charAt(0);

        if ((s >= 'a' && s <= 'z') || (s >= 'A' && s <= 'Z')) {
            return true;
        }

        return false;
    }

    /**
     * 手机号码判断. 包括空判断,手机号合法性判断, 号码段： 移动：134、135、136、137、138、139、147、
     * 150、151、152、157、158、159、182、187、188 联通：130、131、132、155、156、185、186 电信：133、153、180、189 <br>
     * 130-139 <br>
     * 153,155,156, <br>
     * 180,182,185,186,187,188,189,
     *
     * @return boolean
     */
    public static boolean isMobileNO(String mobiles) {
        if (isEmpty(mobiles)) {
            return false;
        }
        return mobiles.matches(REGEX_PHONE);
    }


    public static boolean isValidMailAddress(String mailStr) {
        // String check =
        // "^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        // Pattern regex = Pattern.compile(check);
        // Matcher matcher = regex.matcher(mailStr);
        // boolean isMatched = matcher.matches();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mailStr).matches();
    }

    public static boolean isValidQQ(String mailStr) {
        String check = "^\\d+$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(mailStr);
        boolean isMatched = matcher.matches();
        return isMatched;
    }



    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 将含有空格、tab、换行等空白的字符串中的空白去掉
     */
    public static String removeSpaceInString(String strWithSpace) {
        return strWithSpace.replaceAll("\\s+", "");
    }


    /**
     * TextView 绝对大小显示
     */
    public static void absoluteSizeSpanText(TextView textview, int textSize, int startIndex,
            int endIndex, int color) {
        SpannableString spanText = new SpannableString(textview.getText().toString());
        spanText.setSpan(new AbsoluteSizeSpan(textSize, true), startIndex, endIndex,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textview.setText(spanText);
    }

    /** 设置某个字段的字体大小 */
    public static void setAbsoluteSizeSpan(SpannableString ssb, int startIndex, int endIndex,
            int size) {
        ssb.setSpan(new AbsoluteSizeSpan(dipToPixels(size)), startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    /** 设置某个字段的字体颜色 */
    public static void setForegroundColorSpan(SpannableString ssb, int startIndex, int endIndex,
            int color) {
        ssb.setSpan(new ForegroundColorSpan(color), startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /** 设置某个字段的字体加粗 */
    public static void setBoldSpan(SpannableString ssb, int startIndex, int endIndex) {
        ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    /** 设置某个字段的字体加粗 */
    public static void setBoldSpan(SpannableString ssb, int startIndex, int endIndex,
            boolean boldOrNot) {
        if (boldOrNot) {
            ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
            ssb.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), startIndex, endIndex,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
    }

    /**
     * 文本设置图片
     */
    public static void addImageSpan(SpannableString ssb, int startIndex, int endIndex, int imageSrc, Context context) {
        Drawable d = context.getResources().getDrawable(imageSrc);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ssb.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    public static void setSpannableSpan(SpannableString ssb, int startIndex, int endIndex,
            int size) {
        setAbsoluteSizeSpan(ssb, startIndex, endIndex, size);

    }

    public static void setSpannableSpan(SpannableString ssb, int startIndex, int endIndex, int size,
            int color) {
        setAbsoluteSizeSpan(ssb, startIndex, endIndex, size);
        setForegroundColorSpan(ssb, startIndex, endIndex, color);
    }

    /** 设置某个字段的字体大小颜色加粗 */
    public static void setSpannableSpan(SpannableString ssb, int startIndex, int endIndex, int size,
            int color, boolean needBold) {
        setAbsoluteSizeSpan(ssb, startIndex, endIndex, size);
        setForegroundColorSpan(ssb, startIndex, endIndex, color);
        if (needBold) {
            setBoldSpan(ssb, startIndex, endIndex);
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 设置指定位置的字体大小
     *
     * @param spValue    字体大小(单位:sp)
     * @param startIndex 起始位置
     * @param endIndex   结束位置
     */
    public static void setAbsoluteSizeSpanSp(SpannableString ssb, float spValue, int startIndex,
            int endIndex) {
        ssb.setSpan(new AbsoluteSizeSpan(dipToPixels(spValue)), startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    // dip转像素
    public static int dipToPixels(Context context ,final float dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        return (int) (dip * SCALE + 0.5f);
    }
    // dip转像素
    public static int dipToPixels( final float dip) {
        final float SCALE = App.getInstant().getResources().getDisplayMetrics().density;
        return (int) (dip * SCALE + 0.5f);
    }
    // 像素转dip
    public static float pixelsToDip(Context context,final int pixels) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        return pixels / SCALE;
    }
}
