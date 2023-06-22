package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlEntities {
    private static final Pattern HTML_ENTITY_PATTERN = Pattern.compile("&(#?[0-9a-zA-Z]+);");
    private static final Map<String, Character> lookup = new HashMap();

    static {
        lookup.put("Agrave", 192);
        lookup.put("agrave", 224);
        lookup.put("Aacute", 193);
        lookup.put("aacute", 225);
        lookup.put("Acirc", 194);
        lookup.put("acirc", 226);
        lookup.put("Atilde", 195);
        lookup.put("atilde", 227);
        lookup.put("Auml", 196);
        lookup.put("auml", 228);
        lookup.put("Aring", 197);
        lookup.put("aring", 229);
        lookup.put("AElig", 198);
        lookup.put("aelig", 230);
        lookup.put("Ccedil", 199);
        lookup.put("ccedil", 231);
        lookup.put("Egrave", 200);
        lookup.put("egrave", 232);
        lookup.put("Eacute", 201);
        lookup.put("eacute", 233);
        lookup.put("Ecirc", 202);
        lookup.put("ecirc", 234);
        lookup.put("Euml", 203);
        lookup.put("euml", 235);
        lookup.put("Igrave", 204);
        lookup.put("igrave", 236);
        lookup.put("Iacute", 205);
        lookup.put("iacute", 237);
        lookup.put("Icirc", 206);
        lookup.put("icirc", 238);
        lookup.put("Iuml", 207);
        lookup.put("iuml", 239);
        lookup.put("ETH", 208);
        lookup.put("eth", 240);
        lookup.put("Ntilde", 209);
        lookup.put("ntilde", 241);
        lookup.put("Ograve", 210);
        lookup.put("ograve", 242);
        lookup.put("Oacute", 211);
        lookup.put("oacute", 243);
        lookup.put("Ocirc", 212);
        lookup.put("ocirc", 244);
        lookup.put("Otilde", 213);
        lookup.put("otilde", 245);
        lookup.put("Ouml", 214);
        lookup.put("ouml", 246);
        lookup.put("Oslash", 216);
        lookup.put("oslash", 248);
        lookup.put("Ugrave", 217);
        lookup.put("ugrave", 249);
        lookup.put("Uacute", 218);
        lookup.put("uacute", 250);
        lookup.put("Ucirc", 219);
        lookup.put("ucirc", 251);
        lookup.put("Uuml", 220);
        lookup.put("uuml", 252);
        lookup.put("Yacute", 221);
        lookup.put("yacute", 253);
        lookup.put("THORN", 222);
        lookup.put("thorn", 254);
        lookup.put("szlig", 223);
        lookup.put("yuml", 255);
        lookup.put("Yuml", 376);
        lookup.put("OElig", 338);
        lookup.put("oelig", 339);
        lookup.put("Scaron", 352);
        lookup.put("scaron", 353);
        lookup.put("Alpha", 913);
        lookup.put("Beta", 914);
        lookup.put("Gamma", 915);
        lookup.put("Delta", 916);
        lookup.put("Epsilon", 917);
        lookup.put("Zeta", 918);
        lookup.put("Eta", 919);
        lookup.put("Theta", 920);
        lookup.put("Iota", 921);
        lookup.put("Kappa", 922);
        lookup.put("Lambda", 923);
        lookup.put("Mu", 924);
        lookup.put("Nu", 925);
        lookup.put("Xi", 926);
        lookup.put("Omicron", 927);
        lookup.put("Pi", 928);
        lookup.put("Rho", 929);
        lookup.put("Sigma", 931);
        lookup.put("Tau", 932);
        lookup.put("Upsilon", 933);
        lookup.put("Phi", 934);
        lookup.put("Chi", 935);
        lookup.put("Psi", 936);
        lookup.put("Omega", 937);
        lookup.put("alpha", 945);
        lookup.put("beta", 946);
        lookup.put("gamma", 947);
        lookup.put("delta", 948);
        lookup.put("epsilon", 949);
        lookup.put("zeta", 950);
        lookup.put("eta", 951);
        lookup.put("theta", 952);
        lookup.put("iota", 953);
        lookup.put("kappa", 954);
        lookup.put("lambda", 955);
        lookup.put("mu", 956);
        lookup.put("nu", 957);
        lookup.put("xi", 958);
        lookup.put("omicron", 959);
        lookup.put("pi", 960);
        lookup.put("rho", 961);
        lookup.put("sigmaf", 962);
        lookup.put("sigma", 963);
        lookup.put("tau", 964);
        lookup.put("upsilon", 965);
        lookup.put("phi", 966);
        lookup.put("chi", 967);
        lookup.put("psi", 968);
        lookup.put("omega", 969);
        lookup.put("thetasym", 977);
        lookup.put("upsih", 978);
        lookup.put("piv", 982);
        lookup.put("iexcl", 161);
        lookup.put("cent", 162);
        lookup.put("pound", 163);
        lookup.put("curren", 164);
        lookup.put("yen", 165);
        lookup.put("brvbar", 166);
        lookup.put("sect", 167);
        lookup.put("uml", 168);
        lookup.put("copy", 169);
        lookup.put("ordf", 170);
        lookup.put("laquo", 171);
        lookup.put("not", 172);
        lookup.put("shy", 173);
        lookup.put("reg", 174);
        lookup.put("macr", 175);
        lookup.put("deg", 176);
        lookup.put("plusmn", 177);
        lookup.put("sup2", 178);
        lookup.put("sup3", 179);
        lookup.put("acute", 180);
        lookup.put("micro", 181);
        lookup.put("para", 182);
        lookup.put("middot", 183);
        lookup.put("cedil", 184);
        lookup.put("sup1", 185);
        lookup.put("ordm", 186);
        lookup.put("raquo", 187);
        lookup.put("frac14", 188);
        lookup.put("frac12", 189);
        lookup.put("frac34", 190);
        lookup.put("iquest", 191);
        lookup.put("times", 215);
        lookup.put("divide", 247);
        lookup.put("fnof", 402);
        lookup.put("circ", 710);
        lookup.put("tilde", 732);
        lookup.put("lrm", 8206);
        lookup.put("rlm", 8207);
        lookup.put("ndash", 8211);
        lookup.put("endash", 8211);
        lookup.put("mdash", 8212);
        lookup.put("emdash", 8212);
        lookup.put("lsquo", 8216);
        lookup.put("rsquo", 8217);
        lookup.put("sbquo", 8218);
        lookup.put("ldquo", 8220);
        lookup.put("rdquo", 8221);
        lookup.put("bdquo", 8222);
        lookup.put("dagger", 8224);
        lookup.put("Dagger", 8225);
        lookup.put("bull", 8226);
        lookup.put("hellip", 8230);
        lookup.put("permil", 8240);
        lookup.put("prime", 8242);
        lookup.put("Prime", 8243);
        lookup.put("lsaquo", 8249);
        lookup.put("rsaquo", 8250);
        lookup.put("oline", 8254);
        lookup.put("frasl", 8260);
        lookup.put("euro", 8364);
        lookup.put("image", 8465);
        lookup.put("weierp", 8472);
        lookup.put("real", 8476);
        lookup.put("trade", 8482);
        lookup.put("alefsym", 8501);
        lookup.put("larr", 8592);
        lookup.put("uarr", 8593);
        lookup.put("rarr", 8594);
        lookup.put("darr", 8595);
        lookup.put("harr", 8596);
        lookup.put("crarr", 8629);
        lookup.put("lArr", 8656);
        lookup.put("uArr", 8657);
        lookup.put("rArr", 8658);
        lookup.put("dArr", 8659);
        lookup.put("hArr", 8660);
        lookup.put("forall", 8704);
        lookup.put("part", 8706);
        lookup.put("exist", 8707);
        lookup.put("empty", 8709);
        lookup.put("nabla", 8711);
        lookup.put("isin", 8712);
        lookup.put("notin", 8713);
        lookup.put("ni", 8715);
        lookup.put("prod", 8719);
        lookup.put("sum", 8721);
        lookup.put("minus", 8722);
        lookup.put("lowast", 8727);
        lookup.put("radic", 8730);
        lookup.put("prop", 8733);
        lookup.put("infin", 8734);
        lookup.put("ang", 8736);
        lookup.put("and", 8743);
        lookup.put("or", 8744);
        lookup.put("cap", 8745);
        lookup.put("cup", 8746);
        lookup.put("int", 8747);
        lookup.put("there4", 8756);
        lookup.put("sim", 8764);
        lookup.put("cong", 8773);
        lookup.put("asymp", 8776);
        lookup.put("ne", 8800);
        lookup.put("equiv", 8801);
        lookup.put("le", 8804);
        lookup.put("ge", 8805);
        lookup.put("sub", 8834);
        lookup.put("sup", 8835);
        lookup.put("nsub", 8836);
        lookup.put("sube", 8838);
        lookup.put("supe", 8839);
        lookup.put("oplus", 8853);
        lookup.put("otimes", 8855);
        lookup.put("perp", 8869);
        lookup.put("sdot", 8901);
        lookup.put("lceil", 8968);
        lookup.put("rceil", 8969);
        lookup.put("lfloor", 8970);
        lookup.put("rfloor", 8971);
        lookup.put("lang", 9001);
        lookup.put("rang", 9002);
        lookup.put("loz", 9674);
        lookup.put("spades", 9824);
        lookup.put("clubs", 9827);
        lookup.put("hearts", 9829);
        lookup.put("diams", 9830);
        lookup.put("gt", '>');
        lookup.put("GT", '>');
        lookup.put("lt", '<');
        lookup.put("LT", '<');
        lookup.put("quot", '\"');
        lookup.put("QUOT", '\"');
        lookup.put("amp", '&');
        lookup.put("AMP", '&');
        lookup.put("apos", '\'');
        lookup.put("nbsp", 160);
        lookup.put("ensp", 8194);
        lookup.put("emsp", 8195);
        lookup.put("thinsp", 8201);
        lookup.put("zwnj", 8204);
        lookup.put("zwj", 8205);
    }

    public static Character toCharacter(String entityName) {
        return lookup.get(entityName);
    }

    public static String decodeHtmlText(String htmlText) {
        if (htmlText.length() == 0 || htmlText.indexOf(38) == -1) {
            return htmlText;
        }
        StringBuilder output = new StringBuilder();
        int lastMatchEnd = 0;
        Matcher matcher = HTML_ENTITY_PATTERN.matcher(htmlText);
        while (matcher.find()) {
            String entity = matcher.group(1);
            Character convertedEntity = null;
            if (entity.startsWith("#x")) {
                String hhhh = entity.substring(2);
                try {
                    System.out.println("hex number is " + hhhh);
                    convertedEntity = Character.valueOf((char) Integer.parseInt(hhhh, 16));
                } catch (NumberFormatException e) {
                }
            } else if (entity.startsWith("#")) {
                try {
                    convertedEntity = Character.valueOf((char) Integer.parseInt(entity.substring(1)));
                } catch (NumberFormatException e2) {
                }
            } else {
                convertedEntity = lookup.get(entity);
            }
            if (convertedEntity != null) {
                output.append(htmlText.substring(lastMatchEnd, matcher.start()));
                output.append(convertedEntity);
                lastMatchEnd = matcher.end();
            }
        }
        if (lastMatchEnd < htmlText.length()) {
            output.append(htmlText.substring(lastMatchEnd));
        }
        return output.toString();
    }
}
