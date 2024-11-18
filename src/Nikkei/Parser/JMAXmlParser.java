package Nikkei.Parser;

import java.io.InputStreamReader;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Nikkei.WebStream;

// Niiから取得した場合にHTMLを除去するためのクラスXMLExtractor
// <body><pre></pre></body>の中身だけにする
final class HTMLRemover{
    private String html;

    private static Pattern pat = Pattern.compile("^(<pre>(.*?)</pre>)");

    public HTMLRemover(String html){
        this.html = html;
    }

    public String getXML(){
        Matcher mat = pat.matcher(html);
        String xml = mat.replaceAll("");
        return xml;
    }

    public static void main(String[] args) {
        try{
            InputStreamReader reader = new WebStream(new URI("http://agora.ex.nii.ac.jp/cgi-bin/cps/report_xml.pl?id=20241116122547_0_VXSE53_010000").toURL()).reader();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

// XMLの取得元
// Nii
// http://agora.ex.nii.ac.jp/cgi-bin/cps/report_xml.pl?クエリ
// JMA
// 
public class JMAXmlParser {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}