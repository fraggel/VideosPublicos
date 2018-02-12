package es.fraggel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    static String linea="";

    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    public static String getLinea(){
        return linea;
    }
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null) {
                if(!"".equals(line)){
                    linea=line;
                }
            }
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}