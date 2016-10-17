import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by TAKI on 2016.10.07..
 */
public class NNSolutionThree {
    private int bemeneti_reteg;
    private int kimeneti_dimenzio;
    private int[] rejtett_reteg;


    private int[] std_in_int;
    private double[][] x;
    private double[][][] W;
    private double[][][] S;
    private double[][] W_L;
    private double[] final_of_algorithm;
    private int bemenetek_szama;

    public void beolvas() throws IOException {

        BufferedReader isr = new BufferedReader(new InputStreamReader(System.in));
        String line;
        line = isr.readLine();
        String[] std_in;
        std_in = line.split(",");
        std_in_int = new int[std_in.length];
        for (int i = 0; i < std_in.length; i++) {
            String temp = std_in[i];
            std_in_int[i] = Integer.parseInt(temp);
        }

        bemeneti_reteg = std_in_int[0];
        kimeneti_dimenzio = std_in_int[std_in_int.length - 1];
        rejtett_reteg = new int[std_in_int.length - 2];
        for (int i = 1; i < std_in_int.length - 1; i++) {
            int temp = std_in_int[i];
            rejtett_reteg[i - 1] = temp;
        }


        W = new double[rejtett_reteg.length + 1][100][100];

        for (int j = 0; j < rejtett_reteg.length; j++) {
            for (int i = 0; i < rejtett_reteg[j]; i++) {
                line = isr.readLine();
                std_in = line.split(",");
                int oszlopok_szama = std_in.length;
                for (int k = 0; k < oszlopok_szama; k++)
                    W[j][i][k] = Double.parseDouble(std_in[k]);


            }

        }

        for (int i = 0; i < 1; i++) {
            line = isr.readLine();
            std_in = line.split(",");
            int oszlopok_szama = std_in.length;
            for (int k = 0; k < oszlopok_szama; k++) {
                W[rejtett_reteg.length][i][k] = Double.parseDouble(std_in[k]);
            }
        }


        line = isr.readLine();
        bemenetek_szama = Integer.parseInt(line);
        x = new double[bemenetek_szama][bemeneti_reteg + 1];

        for (int i = 0; i < 1; i++) {
            line = isr.readLine();
            std_in = line.split(",");
            for (int j = 0; j < bemeneti_reteg; j++) {

                x[i][j] = Double.parseDouble(std_in[j]);

            }
            x[i][bemeneti_reteg] = 1.0;
        }


    }

    public void algoritmus() {
        System.out.print(bemeneti_reteg + ",");
        for (int i = 0; i < rejtett_reteg.length; i++) {
            System.out.print(rejtett_reteg[i] + ",");
        }
        System.out.println(kimeneti_dimenzio);

        if (rejtett_reteg.length == 0) {
            for (int i = 0; i < bemeneti_reteg; i++) {
                System.out.print(x[0][i] + ",");
            }
            System.out.println("1");
        } else {
            W_L = new double[1][rejtett_reteg[rejtett_reteg.length - 1]];
            S = new double[rejtett_reteg.length][100][100];
            for (int i = 0; i < W_L[0].length; i++) {
                W_L[0][i] = W[rejtett_reteg.length][0][i];
            }
            S_feltoltes();


            final_of_algorithm = dim1_szorzas(W_L[0], derivateReLu(S[rejtett_reteg.length - 1], rejtett_reteg[rejtett_reteg.length - 1])
                    ,rejtett_reteg[rejtett_reteg.length-1]);


            for (int k = rejtett_reteg.length - 1; k >= 1; k--) {
                final_of_algorithm = dim1_szorzas(final_of_algorithm,W[k],rejtett_reteg[k-1] );
                final_of_algorithm = dim1_szorzas(final_of_algorithm,derivateReLu(S[k-1],rejtett_reteg[k-1]),rejtett_reteg[k-1]);


            }

            for (int a = 0; a < final_of_algorithm.length; a++) {
                double temp = 0;
                temp = final_of_algorithm[a];
                System.out.print(x[0][0] * temp);
                for (int b = 1; b < x[0].length; b++) {
                    System.out.print("," + x[0][b] * temp);
                }
                System.out.println();
            }


            for (int i = 1; i < rejtett_reteg.length; i++) {// ez a belső rétegek deriváltjai
                final_of_algorithm = dim1_szorzas(W_L[0], derivateReLu(S[rejtett_reteg.length - 1], rejtett_reteg[rejtett_reteg.length - 1])
                        ,rejtett_reteg[rejtett_reteg.length-1]);


                for (int k = rejtett_reteg.length - 1; k > i; k--) {

                    final_of_algorithm = dim1_szorzas(final_of_algorithm,W[k],rejtett_reteg[k-1] );
                    final_of_algorithm = dim1_szorzas(final_of_algorithm,derivateReLu(S[k-1],rejtett_reteg[k-1]),rejtett_reteg[k-1]);

                }
                //itt írjuk ki.
                // s[i-1] lesz a bemenet!!!
                for (int a = 0; a < final_of_algorithm.length; a++) {
                    double temp = 0;
                    temp = final_of_algorithm[a];
                    System.out.print(S[i-1][0][0] * temp);
                    for (int b = 1; b < rejtett_reteg[i-1] ;b++) {
                        System.out.print("," + S[i-1][b][b] * temp);
                    }
                    System.out.print("," + 1 * temp);
                    System.out.println();
                }


            }


            for (int i = 0; i < rejtett_reteg[rejtett_reteg.length - 1]; i++) {
                System.out.print(S[rejtett_reteg.length - 1][i][i] + ",");
            }
            System.out.println("1.0");


        }
    }

    public void S_feltoltes() {

        double[] result = mtx_szorzas(W[0], x[0], rejtett_reteg[0]);
        result = ReLu(result);
        for (int i = 0; i < rejtett_reteg[0]; i++) {
            S[0][i][i] = result[i];
        }

        for (int i = 1; i < rejtett_reteg.length; i++) {
            //for(int j=0;j<rejtett_reteg[i];j++){
            double[] tovabbvitel = mtx_szorzas_2dim(W[i], S[i - 1], rejtett_reteg[i],rejtett_reteg[i-1]);
            tovabbvitel = ReLu(tovabbvitel);
            result = tovabbvitel;
            //ez a result már az s[i]
            for (int k = 0; k < rejtett_reteg[i]; k++) {
                S[i][k][k] = result[k];
            }
            //}
        }
    }

    public double[] dim1_szorzas(double[] bal, double[][] jobb, int segedszam ) {//segedszam a jobb-ra kell annak az oszlopai

        double[] result = new double[segedszam];

        for(int i=0;i<segedszam;i++){
            for(int j=0; j<bal.length;j++){
                result[i] += bal[j]*jobb[j][i];
            }
        }
        return result;
    }

    public double[] mtx_szorzas(double[][] bal, double[] jobb, int oszlopszam) {
        double[] result = new double[oszlopszam];

        for (int i = 0; i < oszlopszam; i++) {
            for (int j = 0; j < jobb.length; j++) {

                result[i] += bal[i][j] * jobb[j];
            }
        }

        /*for (int i = 0; i < oszlopszam; i++) {
            result[i] += bal[i][jobb.length];
        }*/

        return result;
    }

    public double[] mtx_szorzas_2dim(double[][] bal, double[][] jobb, int sorszam, int oszlopszam) {
        double[] result = new double[sorszam];

        for (int i = 0; i < sorszam; i++) {
            for (int j = 0; j < oszlopszam; j++) {

                result[i] += bal[i][j] * jobb[j][j];
            }
        }

        for (int i = 0; i < sorszam; i++) {
            result[i] += bal[i][oszlopszam];
        }

        return result;
    }


    public double[] ReLu(double[] javitando) {
        double[] result = new double[javitando.length];

        for (int i = 0; i < javitando.length; i++) {
            if (javitando[i] < 0) {
                result[i] = 0.0;
            } else {
                result[i] = javitando[i];
            }
        }
        return result;
    }

    public double[][] derivateReLu(double[][] choice, int oszlopszam) {
        double[][] result = new double[oszlopszam][oszlopszam];
        for (int i = 0; i < oszlopszam; i++) {
            if (choice[i][i] > 0) {
                result[i][i] = 1.0;
            } else {
                result[i][i] = 0.0;
            }
        }
        return result;

    }

    public static void main(String args[]) throws IOException {
        NNSolutionThree pelda = new NNSolutionThree();
        pelda.beolvas();
        pelda.algoritmus();

    }
}
