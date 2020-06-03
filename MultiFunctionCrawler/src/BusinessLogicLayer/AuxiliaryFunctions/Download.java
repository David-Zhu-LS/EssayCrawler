package BusinessLogicLayer.AuxiliaryFunctions;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Download class:下载类
 */
public class Download {
    public static String download(URL url, String charset) throws Exception{
        try(InputStream input = url.openStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream()){
            byte[] data = new byte[1024];
            int length;
            while((length = input.read(data))!=-1){
                output.write(data,0,length);
            }
            byte[] content = output.toByteArray();
            return new String(content, Charset.forName(charset));
        }
    }
}
