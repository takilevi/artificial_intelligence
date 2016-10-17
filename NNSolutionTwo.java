/**
 * Created by TAKI on 2016.10.06..
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NNSolutionTwo {
    private int bemeneti_reteg;
    private int kimeneti_dimenzio;
    private int[] rejtett_reteg;


    private int[] std_in_int;
    private double[] b;
    private double[][] x;
    private double[][][] W;
    private int bemenetek_szama;

    public void beolvas() throws IOException
    {

        BufferedReader isr =new BufferedReader( new InputStreamReader(System.in));
        String line;
        line = isr.readLine();
        String[] std_in;
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


        W = new double[rejtett_reteg.length+1][100][101];

        for(int j=0;j<rejtett_reteg.length;j++) {
            for (int i = 0; i < rejtett_reteg[j]; i++) {
                line = isr.readLine();
                std_in = line.split(",");
                int oszlopok_szama = std_in.length;
                for (int k = 0; k < oszlopok_szama; k++) {
                    W[j][i][k] = Double.parseDouble(std_in[k]);

                }
            }

        }

        for(int i = 0; i<kimeneti_dimenzio;i++){
            line = isr.readLine();
            std_in = line.split(",");
            int oszlopok_szama = std_in.length;
            for(int k=0;k<oszlopok_szama;k++)
            {
                W[rejtett_reteg.length][i][k] = Double.parseDouble(std_in[k]);
            }
        }




        line=isr.readLine();
        bemenetek_szama = Integer.parseInt(line);
        x = new double[bemenetek_szama][bemeneti_reteg];

        for(int i=0; i<bemenetek_szama;i++){
            line=isr.readLine();
            std_in = line.split(",");
            for(int j=0;j<bemeneti_reteg;j++){

                x[i][j]=Double.parseDouble(std_in[j]);

            }
        }




    }

    public void algoritmus(){

        if(rejtett_reteg.length==0){
            System.out.println(bemenetek_szama);
            for(int i=0;i<bemenetek_szama;i++) {
                double[] result = mtx_szorzas(W[0], x[i], kimeneti_dimenzio);
                //result = ReLu(result);
                System.out.print(result[0]);
                for(int j=1;j<result.length;j++){
                    System.out.print(","+result[j]);
                }
                System.out.println();
            }
        }
        else {
            System.out.println(bemenetek_szama);
            for(int i=0;i<bemenetek_szama;i++){
                double[] result = mtx_szorzas(W[0], x[i], rejtett_reteg[0]);
                result = ReLu(result);
                for(int j=1;j<rejtett_reteg.length;j++){
                    double[] tovabbvitel = mtx_szorzas(W[j],result,rejtett_reteg[j]);
                    tovabbvitel = ReLu(tovabbvitel);
                    result = tovabbvitel;
                }
                result = mtx_szorzas(W[rejtett_reteg.length], result, kimeneti_dimenzio);

                System.out.print(result[0]);
                for(int j=1;j<result.length;j++){
                    System.out.print(","+result[j]);
                }
                System.out.println();
            }

            double[] result = mtx_szorzas(W[0], x[0], rejtett_reteg[0]);
            result = ReLu(result);


        }
    }

    public double[] mtx_szorzas(double[][] bal,double[] jobb,int oszlopszam){
        double[] result = new double[oszlopszam];

        for(int i = 0; i<oszlopszam;i++){
            for(int j=0; j<jobb.length;j++){

                result[i] += bal[i][j]*jobb[j];
            }
        }

        for(int i=0;i<oszlopszam;i++){
            result[i] += bal[i][jobb.length];
        }

        return result;
    }
    public double[] ReLu(double[] javitando){
        double[] result = new double[javitando.length];

        for(int i=0; i<javitando.length;i++){
            if(javitando[i] < 0){result[i]=0.0;}
            else{result[i]=javitando[i];}
        }
        return result;
    }

    public static void main(String args[]) throws IOException{
        NNSolutionTwo proba = new NNSolutionTwo();
        proba.beolvas();
        proba.algoritmus();
    }

}

