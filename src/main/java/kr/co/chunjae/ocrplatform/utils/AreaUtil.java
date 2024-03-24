package kr.co.chunjae.ocrplatform.utils;

public class AreaUtil {
    public static String switchArea(String id) {
        if (id == null) {
            return ""; // 또는 적절한 기본값 반환
        }
        switch (id) {
            case "1": return "국어";
            case "2": return "수학";
            case "3": return "영어";
            case "4": return "한국사";
            case "5": return "사회탐구";
            case "6": return "과학탐구";
            case "7": return "직업탐구";
            case "8": return "제2외국어/한문";
            default: return "";
        }
    }
}
