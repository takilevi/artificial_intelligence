import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by TAKI on 2016.10.15..
 */
public class NNSolutionFour {
    private int epoch;
    private double mu_batorsag;
    private double R_tanitoarany;
    private double S_mintakszama;

    private int bemenetek_szama;
    private int kimenetek_szama;
    private int[] rejtett_reteg;

    private double[][] tanitominta;
    private double[][] validaciosminta;
    private double[][][] W;
    private double[][][] W_derivated;
    private double[][][] S;
    private double[] C_koltsegfuggveny;
    private double[] final_of_algorithm;

    public void beolvasas() throws IOException {
        BufferedReader isr = new BufferedReader(new InputStreamReader(System.in));
        String line;
        line = isr.readLine();
        String[] std_in;
        std_in = line.split(",");
        epoch = Integer.parseInt(std_in[0]);
        mu_batorsag = Double.parseDouble(std_in[1]);
        R_tanitoarany = Double.parseDouble(std_in[2]);

        line = isr.readLine();
        std_in = line.split(",");
        int[] std_in_int = new int[std_in.length];
        for (int i = 0; i < std_in.length; i++) {
            String temp = std_in[i];
            std_in_int[i] = Integer.parseInt(temp);
        }
        bemenetek_szama = std_in_int[0];
        kimenetek_szama = std_in_int[std_in_int.length - 1];
        rejtett_reteg = new int[std_in_int.length - 2];
        for (int i = 1; i < std_in_int.length - 1; i++) {
            int temp = std_in_int[i];
            rejtett_reteg[i - 1] = temp;
        }

        W = new double[rejtett_reteg.length + 1][][];
        W_derivated = new double[rejtett_reteg.length + 1][][];

        for (int j = 0; j < rejtett_reteg.length; j++) {
            W[j] = new double[rejtett_reteg[j]][];
            W_derivated[j] = new double[rejtett_reteg[j]][];
            for (int i = 0; i < rejtett_reteg[j]; i++) {
                line = isr.readLine();
                std_in = line.split(",");
                int oszlopok_szama = std_in.length;
                W[j][i] = new double[oszlopok_szama];
                W_derivated[j][i] = new double[oszlopok_szama];
                for (int k = 0; k < oszlopok_szama; k++) {
                    W[j][i][k] = Double.parseDouble(std_in[k]);
                    W_derivated[j][i][k] = Double.parseDouble(std_in[k]);
                }
            }
        }

        W[rejtett_reteg.length] = new double[kimenetek_szama][];
        W_derivated[rejtett_reteg.length] = new double[kimenetek_szama][];
        for (int i = 0; i < kimenetek_szama; i++) {
            line = isr.readLine();
            std_in = line.split(",");
            int oszlopok_szama = std_in.length;
            W[rejtett_reteg.length][i] = new double[oszlopok_szama];
            W_derivated[rejtett_reteg.length][i] = new double[oszlopok_szama];
            for (int k = 0; k < oszlopok_szama; k++) {
                W[rejtett_reteg.length][i][k] = Double.parseDouble(std_in[k]);
                W_derivated[rejtett_reteg.length][i][k] = Double.parseDouble(std_in[k]);
            }
        }

        line = isr.readLine();
        S_mintakszama = Double.parseDouble(line);
        int temp_sorokszama = (int) Math.floor(S_mintakszama * R_tanitoarany);
        tanitominta = new double[temp_sorokszama][bemenetek_szama + kimenetek_szama];
        validaciosminta = new double[(int) S_mintakszama - temp_sorokszama][bemenetek_szama + kimenetek_szama];


        for (int i = 0; i < temp_sorokszama; i++) {
            line = isr.readLine();
            std_in = line.split(",");
            for (int j = 0; j < (bemenetek_szama + kimenetek_szama); j++) {

                tanitominta[i][j] = Double.parseDouble(std_in[j]);

            }
        }
        for (int i = 0; i < ((int) S_mintakszama - temp_sorokszama); i++) {
            line = isr.readLine();
            std_in = line.split(",");
            for (int j = 0; j < (bemenetek_szama + kimenetek_szama); j++) {

                validaciosminta[i][j] = Double.parseDouble(std_in[j]);

            }
        }

    }

    public void algoritmus() {


        if (rejtett_reteg.length == 0) {
            for(int e=0; e<epoch;e++){
                C_koltsegfuggveny = new double[validaciosminta.length];
                for(int i=0; i<tanitominta.length;i++){
                    double[] y_kimenet = y_kiszamitas_basic_kimenet(i);
                    double[] epszilon_hiba = new double[y_kimenet.length];

                    for (int j = 0; j < kimenetek_szama; j++) {
                        epszilon_hiba[j] = tanitominta[i][j + bemenetek_szama] - y_kimenet[j];
                    }
                    double[] modosito_szorzo = new double[epszilon_hiba.length];
                    for (int j = 0; j < modosito_szorzo.length; j++) {
                        modosito_szorzo[j] = mu_batorsag * 2 * epszilon_hiba[j];
                    }

                    for(int sorok_szama=0; sorok_szama<W_derivated[0].length;sorok_szama++){
                        for(int oszlopok=0; oszlopok<W_derivated[0][sorok_szama].length-1;oszlopok++){
                            W_derivated[0][sorok_szama][oszlopok] += modosito_szorzo[sorok_szama]*tanitominta[i][oszlopok];
                        }
                        W_derivated[0][sorok_szama][W_derivated[0][sorok_szama].length-1] += modosito_szorzo[sorok_szama]*1;
                    }

                    W_visszamasolas();
                }
                for(int i=0;i<validaciosminta.length;i++){
                    double[] y_kimenet = y_kiszamitas_validalas(i);
                    double[] epszilon_hiba = new double[y_kimenet.length];

                    for (int j = 0; j < kimenetek_szama; j++) {
                        epszilon_hiba[j] = validaciosminta[i][j + bemenetek_szama] - y_kimenet[j];
                        epszilon_hiba[j] = epszilon_hiba[j]*epszilon_hiba[j];
                        C_koltsegfuggveny[i]+=epszilon_hiba[j];
                    }
                }
                double c_kiir=0;
                for(int c = 0;c<C_koltsegfuggveny.length;c++){
                    c_kiir += C_koltsegfuggveny[c];
                }
                c_kiir = c_kiir/kimenetek_szama;
                c_kiir = c_kiir/validaciosminta.length;
                System.out.println(c_kiir);
            }

        } else {

            for (int epoch_szam = 0; epoch_szam < epoch; epoch_szam++) {

                C_koltsegfuggveny = new double[validaciosminta.length];

                for (int i = 0; i < tanitominta.length; i++) {
                    double[] y_kimenet = y_kiszamitas_basic_kimenet(i);
                    double[] epszilon_hiba = new double[y_kimenet.length];

                    for (int j = 0; j < kimenetek_szama; j++) {
                        epszilon_hiba[j] = tanitominta[i][j + bemenetek_szama] - y_kimenet[j];
                    }
                    double[] modosito_szorzo = new double[epszilon_hiba.length];
                    for (int j = 0; j < modosito_szorzo.length; j++) {
                        modosito_szorzo[j] = mu_batorsag * 2 * epszilon_hiba[j];
                    }

                    sulymodositas(modosito_szorzo, tanitominta[i]);
                }
                for (int i = 0; i < validaciosminta.length; i++) {

                    double[] y_kimenet = y_kiszamitas_validalas(i);
                    double[] epszilon_hiba = new double[y_kimenet.length];

                    epszilon_hiba[0] = validaciosminta[i][bemenetek_szama]-y_kimenet[0];
                    C_koltsegfuggveny[i] = epszilon_hiba[0]*epszilon_hiba[0];

                    //legvégén írjuk itt ki a c_koltsegfüggvényt}
                }
                double c_kiirando = 0;
                for(int c = 0;c<C_koltsegfuggveny.length;c++){
                    c_kiirando += C_koltsegfuggveny[c];
                }
                c_kiirando = c_kiirando / C_koltsegfuggveny.length;
                System.out.println(c_kiirando);
            }

        }

        System.out.print(bemenetek_szama + ",");
        for(int r=0;r<rejtett_reteg.length;r++){
            System.out.print(rejtett_reteg[r]+",");
        }
        System.out.println(kimenetek_szama);

        W_kiir();

    }

    public void sulymodositas(double[] modosito_szorzo, double[] bemenet) {


        S_feltoltes(bemenet);
        double[][] W_L = new double[1][rejtett_reteg[rejtett_reteg.length - 1]];

        for (int i = 0; i < W_L[0].length; i++) {
            W_L[0][i] = W[rejtett_reteg.length][0][i];
        }
        final_of_algorithm = dim1_szorzas(modosito_szorzo, W_L);
        final_of_algorithm = dim1_szorzas(final_of_algorithm, derivateReLu(S[rejtett_reteg.length - 1]));

        for (int k = rejtett_reteg.length - 1; k >= 1; k--) {
            final_of_algorithm = dim1_szorzas_2(final_of_algorithm, W[k], W[k][0].length-1);
            final_of_algorithm = dim1_szorzas(final_of_algorithm, derivateReLu(S[k - 1]));


        }

        for (int a = 0; a < final_of_algorithm.length; a++) {
            double temp = 0;
            temp = final_of_algorithm[a];
            //bele kell rakni a W_derivated[0]-ba!!
            for (int b = 0; b < bemenetek_szama; b++) {
                W_derivated[0][a][b] += bemenet[b] * temp;
                //vajon ebbe hogy teszem a biast?
            }
            W_derivated[0][a][bemenetek_szama] += 1 * temp;

        }


        for (int i = 1; i < rejtett_reteg.length; i++) {// ez a belső rétegek deriváltjai
            final_of_algorithm = dim1_szorzas(modosito_szorzo, W_L);
            final_of_algorithm = dim1_szorzas(final_of_algorithm, derivateReLu(S[rejtett_reteg.length - 1]));

            for (int k = rejtett_reteg.length - 1; k > i; k--) {

                final_of_algorithm = dim1_szorzas_2(final_of_algorithm, W[k], W[k][0].length-1);
                final_of_algorithm = dim1_szorzas(final_of_algorithm, derivateReLu(S[k - 1]));

            }
            //itt írjuk ki.
            // s[i-1] lesz a bemenet!!!
            /*for (int a = 0; a < final_of_algorithm.length; a++) {
                double temp = 0;
                temp = final_of_algorithm[a];

                for (int b = 1; b < rejtett_reteg[i - 1]; b++) {
                    W_derivated[i][a][b] += S[i - 1][b][b] * temp;
                    //System.out.print("," + S[i-1][b][b] * temp);
                }
                W_derivated[0][a][rejtett_reteg[i - 1]] += 1 * temp;

            }*/
            for (int a = 0; a < final_of_algorithm.length; a++) {
                double temp = 0;
                temp = final_of_algorithm[a];

                for (int b = 0; b < rejtett_reteg[i - 1]; b++) {
                    W_derivated[i][a][b] += S[i - 1][b][b] * temp;
                    //System.out.print("," + S[i-1][b][b] * temp);
                }
                W_derivated[i][a][rejtett_reteg[i - 1]] += 1 * temp;

            }


        }


        for (int i = 0; i < rejtett_reteg[rejtett_reteg.length - 1]; i++) {
            W_derivated[rejtett_reteg.length][0][i] += S[rejtett_reteg.length - 1][i][i] * modosito_szorzo[0];
            //System.out.print(S[rejtett_reteg.length - 1][i][i] + ",");
        }
        W_derivated[rejtett_reteg.length][0][rejtett_reteg[rejtett_reteg.length - 1]] += 1 * modosito_szorzo[0];
        //System.out.println("1.0");

        W_visszamasolas();


    }

    public void W_visszamasolas() {
        for (int i = 0; i < W.length; i++) {
            for (int j = 0; j < W[i].length; j++) {
                for (int k = 0; k < W[i][j].length; k++) {
                    W[i][j][k] = W_derivated[i][j][k];
                }
            }
        }
    }
    public void W_kiir(){
        for (int i = 0; i < W.length; i++) {
            for (int j = 0; j < W[i].length; j++) {
                System.out.print(W[i][j][0]);
                for (int k = 1; k < W[i][j].length; k++) {
                    System.out.print(","+W[i][j][k]);
                }
                System.out.println();
            }
        }
    }

    public void S_feltoltes(double[] bemenet) {
        S = new double[rejtett_reteg.length][][];
        double[] result = mtx_szorzas(W[0], bemenet);
        result = ReLu(result);

        S[0] = new double[rejtett_reteg[0]][rejtett_reteg[0]];
        for (int i = 0; i < rejtett_reteg[0]; i++) {
            S[0][i][i] = result[i];
        }

        for (int i = 1; i < rejtett_reteg.length; i++) {
            S[i] = new double[rejtett_reteg[i]][rejtett_reteg[i]];
            double[] tovabbvitel = ortogonalis_szorzas(W[i], S[i - 1]);
            tovabbvitel = ReLu(tovabbvitel);
            result = tovabbvitel;
            //ez a result már az s[i]
            for (int k = 0; k < rejtett_reteg[i]; k++) {
                S[i][k][k] = result[k];
            }

        }

    }

    public double[] ortogonalis_szorzas(double[][] bal, double[][] jobb) {
        double[] result = new double[bal.length];

        for (int i = 0; i < bal.length; i++) {
            for (int j = 0; j < jobb.length; j++) {

                result[i] += bal[i][j] * jobb[j][j];
            }
        }

        for (int i = 0; i < bal.length; i++) {
            result[i] += bal[i][jobb.length];
        }

        return result;
    }

    public double[] y_kiszamitas_basic_kimenet(int tanitominta_szama) {

        double[] result = mtx_szorzas(W[0], tanitominta[tanitominta_szama]);
        for (int i = 1; i < W.length; i++) {
            result = ReLu(result);
            result = mtx_szorzas(W[i], result);

        }
        return result;

    }
    public double[] y_kiszamitas_validalas(int validaciosminta_szama) {

        double[] result = mtx_szorzas(W[0], validaciosminta[validaciosminta_szama]);
        for (int i = 1; i < W.length; i++) {
            result = ReLu(result);
            result = mtx_szorzas(W[i], result);

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

    public double[] mtx_szorzas(double[][] bal, double[] jobb) {
        double[] result = new double[bal.length];

        for (int i = 0; i < bal.length; i++) {
            for (int j = 0; j < bal[0].length - 1; j++) { //jobb.length nemjo egyelőre

                result[i] += bal[i][j] * jobb[j];
            }
        }
        for (int i = 0; i < bal.length; i++) {
            result[i] += bal[i][bal[0].length - 1];
        }
        return result;
    }

    public double[] dim1_szorzas(double[] bal, double[][] jobb) {//segedszam a jobb-ra kell annak az oszlopai

        double[] result = new double[jobb[0].length];

        for (int i = 0; i < jobb[0].length; i++) {
            for (int j = 0; j < bal.length; j++) {
                result[i] += bal[j] * jobb[j][i];
            }
        }
        return result;
    }
    public double[] dim1_szorzas_2(double[] bal, double[][] jobb, int oszlop) {//segedszam a jobb-ra kell annak az oszlopai

        double[] result = new double[oszlop];

        for (int i = 0; i < oszlop; i++) {
            for (int j = 0; j < bal.length; j++) {
                result[i] += bal[j] * jobb[j][i];
            }
        }
        return result;
    }

    public double[][] derivateReLu(double[][] choice) {
        double[][] result = new double[choice.length][choice.length];
        for (int i = 0; i < choice.length; i++) {
            if (choice[i][i] > 0) {
                result[i][i] = 1.0;
            } else {
                result[i][i] = 0.0;
            }
        }
        return result;

    }


    public static void main(String args[]) throws IOException {
        NNSolutionFour pelda = new NNSolutionFour();

        pelda.beolvasas();
        pelda.algoritmus();

    }
}
