/**
 * Created by TAKI on 2016.10.06..
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class NNSolutionOne {

    private int bemeneti_reteg;
    private int kimeneti_dimenzio;
    private int[] rejtett_reteg;

    private String[] std_in;
    private int[] std_in_int;

    public void beolvas() throws IOException
    {

        BufferedReader isr =new BufferedReader( new InputStreamReader(System.in));
        String line;
        line = isr.readLine();

        std_in = line.split(",");
        std_in_int = new int[std_in.length];
        for(int i = 0; i<std_in.length;i++)
        {
            String temp = std_in[i];
            std_in_int[i] = Integer.parseInt(temp);
        }

        bemeneti_reteg = std_in_int[0];
        kimeneti_dimenzio = std_in_int[std_in_int.length - 1];
        rejtett_reteg = new int[std_in_int.length-2];
        for(int i = 1; i <std_in_int.length -1; i++){
            int temp = std_in_int[i];
            rejtett_reteg[i-1]=temp;
        }

    }
    public void kimenet()
    {
        System.out.print(bemeneti_reteg + ",");
        for(int i = 0; i<rejtett_reteg.length;i++)
        {
            System.out.print(rejtett_reteg[i]+",");
        }
        System.out.println(kimeneti_dimenzio);
        Random r=new Random();
        if(rejtett_reteg.length==0){
            for(int i=0; i<kimeneti_dimenzio;i++){
                for(int j = 0; j<bemeneti_reteg;j++){
                    double random = r.nextGaussian()*0.1;
                    System.out.print(random+",");
                }
                System.out.println("0.0");
            }
        }
        else{
            for(int j = 0;j<rejtett_reteg[0];j++){
                for(int i = 0; i <bemeneti_reteg;i++){
                    double random = r.nextGaussian()*0.1;
                    System.out.print(random+",");
                }
                System.out.println("0.0");
            }
            for(int i = 1; i<rejtett_reteg.length;i++)
            {
                for(int j=0;j<rejtett_reteg[i];j++)
                {
                    for(int k=0;k<rejtett_reteg[i-1];k++){
                        double random = r.nextGaussian()*0.1;
                        System.out.print(random+",");
                    }
                    System.out.println("0.0");
                }
            }
            for(int j = 0;j<kimeneti_dimenzio;j++){
                for(int i = 0; i <rejtett_reteg[rejtett_reteg.length-1];i++){
                    double random = r.nextGaussian()*0.1;
                    System.out.print(random+",");
                }
                System.out.println("0.0");
            }
        }

    }
    public static void main(String args[]) throws IOException{

        NNSolutionOne pelda = new NNSolutionOne();

        pelda.beolvas();
        pelda.kimenet();

    }

}
