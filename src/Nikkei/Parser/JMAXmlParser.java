package Nikkei.Parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

import Nikkei.WebConnection;

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
            InputStreamReader reader = new WebConnection(new URI("http://agora.ex.nii.ac.jp/cgi-bin/cps/report_xml.pl?id=20241116122547_0_VXSE53_010000").toURL()).reader();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

// XMLの取得元
// Nii 余計なHTMLを含むためHTMLRemoverで前処理が必要
// http://agora.ex.nii.ac.jp/cgi-bin/cps/report_xml.pl?クエリ
// JMA
// 

// XML(URL) -> DOM
public class JMAXmlParser {
    private enum DataSource{
        NII,
        JMA,
        Unknown
    }

    private URL url;
    private DataSource source;
    private Document dom;


    public JMAXmlParser(String url){
        try{
            this.url = new URI(url).toURL();
        }catch(Exception e){
            e.printStackTrace();
        }

        source = switch(this.url.getHost()){
            case "agora.ex.nii.ac.jp" -> DataSource.NII;
            case "www.jma.go.jp" -> DataSource.JMA;
            default -> DataSource.Unknown;
        };

        switch(source){
            case NII -> {
                try{
                    InputStreamReader reader = new WebConnection(this.url).reader();
                    String xml = new HTMLRemover(reader.toString()).getXML();
                    dom = enDom(xml);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            case JMA -> {
                try{
                    InputStream stream = new WebConnection(this.url).stream();
                    dom = enDom(stream);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            case Unknown -> {
                System.err.println("Unknown data source");
            }
        }
    }

    private Document enDom(String xml){
        Document dom = null;
        try{
            // DOM実装
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("XML 1.0");
            // Input
            LSInput input = impl.createLSInput();
            input.setStringData(xml);
            // Parser
            LSParser parser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
            parser.getDomConfig().setParameter("namespaces", false);
            // Domを生成
            dom = parser.parse(input);
        }catch(Exception e){
            e.printStackTrace();
        }

        return dom;
    }

    // TODO:↑と実装をまとめたい
    private Document enDom(InputStream stream){
        Document dom = null;
        try{
            // DOM実装
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("XML 1.0");
            // Input
            LSInput input = impl.createLSInput();
            input.setByteStream(stream);
            input.setEncoding("UTF-8");

            // Parser
            LSParser parser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
            parser.getDomConfig().setParameter("namespaces", false);
            // Domを生成
            dom =  parser.parse(input);
        }catch(Exception e){
            e.printStackTrace();
        }
        return dom;
    }

    public Document getDom(){
        return dom;
    }
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}