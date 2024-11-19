package Nikkei;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebStream {
    private URL url;
    private HttpURLConnection connection;
    private InputStream stream;
    private InputStreamReader reader;

    public WebStream(URL url){
        this.url = url;
        try{
            connection = (HttpURLConnection) this.url.openConnection();
            connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6"
            );
            connection.setRequestProperty(
                "Referer",
            "http://www.google.com"
            );
            connection.setInstanceFollowRedirects(false);

            stream = connection.getInputStream();
            reader = new InputStreamReader(stream, "UTF-8");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // return InputStreamReader object
    public InputStreamReader reader(){
        return reader;
    }

    public InputStream stream(){
        return stream;
    }

    // return current handling URL
    public String toString(){
        return url.toString();
    }
    
    public static void main(String[] args){
        WebStream parser;
        try{
            parser = new WebStream(
                new URI("http://www.w3.org/").toURL()
            );
            System.out.println(parser.toString());
        }catch(MalformedURLException e){
            System.err.println(e);
        }catch(URISyntaxException e){
            System.err.println(e);
        }
    }
}