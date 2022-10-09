// import java.io.IOException;

public class SPL {
    public static int Cramer(Matriks M, Matriks Mhsl) {
        /** KAMUS LOKAL */
        Matriks M1 = new Matriks();
        Matriks M2 = new Matriks();
        float D1, D2;
        int i, j, kategori = 1; // kategori: 1. solusi tunggal, 4. tidak dapat menggunakan metode yang dipilih

        /** ALGORITMA */
        Matriks.MakeEmpty(M1, M.BrsEff, M.KolEff - 1);
        for (i = M1.FirsIdxBrs; i <= M1.LastIdxBrs; i++) {
            for (j = M1.FirsIdxBrs; j <= M1.LastIdxBrs; j++) {
                M1.isi[i][j] = M.isi[i][j];
            }
        }

        D1 = Matriks.DeterminanKofaktor(M1);
        if ((M1.BrsEff != M1.KolEff) || D1 == 0) {
            kategori = 4;
        }

        if (kategori == 1) {
            Matriks.MakeEmpty(Mhsl, M.KolEff - 1, M.KolEff);
            for (j = 0; j <= M.KolEff - 1; j++) {
                Matriks.CopyMatriks(M1, M2);
                for (i = M2.FirsIdxBrs; i <= M2.LastIdxBrs; i++) {
                    M2.isi[i][j] = M.isi[i][M.LastIdxKol];
                }
                D2 = Matriks.DeterminanKofaktor(M2);
                Mhsl.isi[j][0] = D2 / D1;
            }
        }

        return kategori;
    }

    public static int Invers(Matriks M, Matriks Mhsl) {
        /** KAMUS LOKAL */
        Matriks A = new Matriks();
        Matriks B = new Matriks();
        Matriks C = new Matriks();
        int i, j;
        int kategori = 1; // kategori: 1. solusi tunggal, 4. tidak dapat menggunakan metode yang dipilih
        
        /** ALGORITMA */
        Matriks.MakeEmpty(A, M.BrsEff, M.KolEff - 1);
        Matriks.MakeEmpty(B, M.BrsEff, 1);

        if (A.BrsEff != A.KolEff) {
            kategori = 4;
        } else {
            for (i = A.FirsIdxBrs; i <= A.LastIdxBrs; i++) {
                for (j = A.FirstIdxKol; j <= A.LastIdxKol; j++) {
                    A.isi[i][j] = M.isi[i][j];
                }
            }

            for (i = B.FirsIdxBrs; i <= B.LastIdxBrs; i++) {
                B.isi[i][0] = M.isi[i][M.LastIdxKol];
            }

            A = Matriks.inverseMatriks(A);

            Matriks.KaliMatriks(A, B, C);
            Matriks.MakeEmpty(Mhsl, M.KolEff - 1, M.KolEff);
            for (i = Mhsl.FirsIdxBrs; i <= Mhsl.LastIdxBrs; i++) {
                Mhsl.isi[i][0] = C.isi[i][0];
            }

        }

        return kategori;
    }

    public static int Gauss(Matriks M, Matriks Mhsl) {
        // kategori: 1. solusi tunggal, 2. banyak solusi, 3. tidak ada solusi

        /** KAMUS LOKAL */
        int i, j, kategori;
        float sum;
        Matriks A = new Matriks();
        
        /** ALGORITMA */
        Matriks.MakeEselon(M);
        Matriks.MakeEmpty(Mhsl, M.KolEff - 1, M.KolEff);
        Matriks.MakeEmpty(A, M.BrsEff, M.KolEff - 1);

        for (i = A.FirsIdxBrs; i <= A.LastIdxBrs; i++) {
            for (j = A.FirstIdxKol; j <= A.LastIdxKol; j++) {
                A.isi[i][j] = M.isi[i][j];
            }
        }

        kategori = 1;
        i = A.FirsIdxBrs;
        while ((kategori != 3) && i <= A.LastIdxBrs) {
            if (Matriks.AllZero(A, i)) {  // jika seluruh elemen pada baris mtriks tsb. 0 semua
                if (M.isi[i][M.LastIdxKol] == 0) {
                    kategori = 2;
                    i++;
                } else {
                    kategori = 3;
                }
            } else {
                i++;
            }
        }
        if (kategori == 1 || (kategori == 2 && M.BrsEff > M.KolEff - 1)) {
            Mhsl.isi[Mhsl.LastIdxBrs][0] = M.isi[M.LastIdxKol - 1][M.LastIdxKol];
            for (i = M.LastIdxKol - 2; i >= 0; i--) {
                sum = 0;
                for (j = i + 1; j < M.KolEff - 1; j++) {
                    sum += M.isi[i][j] * Mhsl.isi[j][0];
                    Mhsl.isi[i][0] = (M.isi[i][M.LastIdxKol] - sum) / M.isi[i][i];
                }
            }
            kategori = 1;
        } else if (kategori == 2) {
            int indekszero = -1;
            for (i = M.FirsIdxBrs; i <= M.LastIdxBrs; i++) {
                if (Matriks.AllZero(M, i)) {
                    Mhsl.isi[i][i + 1] = 1;
                    if (indekszero == -1) {
                        indekszero = i;
                    }
                }
                Mhsl.isi[i][0] = M.isi[i][M.LastIdxKol];
            }

            for (i = M.FirsIdxBrs; i < indekszero; i++) {
                for (j = i + 1; j < M.LastIdxKol; j++) {
                    Mhsl.isi[i][j + 1] = M.isi[i][j];
                }
            }

        }
        return kategori;
    }

    public static int GaussJordan(Matriks M, Matriks Mhsl) {
        // kategori: 1. solusi tunggal, 2. banyak solusi, 3. tidak ada solusi
        /** KAMUS LOKAL */
        int i, j, kategori;
        float sum;
        Matriks A = new Matriks();
        /** ALGORITMA */
        Matriks.MakeReducedEselon(M);
        Matriks.MakeEmpty(Mhsl, M.KolEff - 1, M.KolEff);
        Matriks.MakeEmpty(A, M.BrsEff, M.KolEff - 1);

        for (i = A.FirsIdxBrs; i <= A.LastIdxBrs; i++) {
            for (j = A.FirstIdxKol; j <= A.LastIdxKol; j++) {
                A.isi[i][j] = M.isi[i][j];
            }
        }

        kategori = 1;
        i = A.FirsIdxBrs;
        while ((kategori != 3) && i <= A.LastIdxBrs) {
            if (Matriks.AllZero(A, i)) {
                if (M.isi[i][M.LastIdxKol] == 0) {
                    kategori = 2;
                    i++;
                } else {
                    kategori = 3;
                }
            } else {
                i++;
            }
        }
        if (kategori == 1 || (kategori == 2 && M.BrsEff > M.KolEff - 1)) {
            Mhsl.isi[Mhsl.LastIdxBrs][0] = M.isi[M.LastIdxKol - 1][M.LastIdxKol];
            for (i = M.LastIdxKol - 2; i >= 0; i--) {
                sum = 0;
                for (j = i + 1; j < M.KolEff - 1; j++) {
                    sum += M.isi[i][j] * Mhsl.isi[j][0];
                    Mhsl.isi[i][0] = (M.isi[i][M.LastIdxKol] - sum) / M.isi[i][i];
                }
            }
            kategori = 1;
        } else if (kategori == 2) {
            int indekszero = -1;
            for (i = M.FirsIdxBrs; i <= M.LastIdxBrs; i++) {
                if (Matriks.AllZero(M, i)) {
                    Mhsl.isi[i][i + 1] = 1;
                    if (indekszero == -1) {
                        indekszero = i;
                    }
                }
                Mhsl.isi[i][0] = M.isi[i][M.LastIdxKol];
            }

            for (i = M.FirsIdxBrs; i < indekszero; i++) {
                for (j = i + 1; j < M.LastIdxKol; j++) {
                    Mhsl.isi[i][j + 1] = M.isi[i][j];
                }
            }

        }
        return kategori;
    }

    public static void TulisHasil(Matriks Mhsl) {
        int i, j;
        for (i = Mhsl.FirsIdxBrs; i <= Mhsl.LastIdxBrs; i++) {
            for (j = Mhsl.FirstIdxKol; j <= Mhsl.LastIdxKol; j++) {
                boolean first = true;
                if (j == 0) {
                    if (Mhsl.isi[i][j] != 0) {
                        System.out.printf("X%d = %.2f ", (i + 1), Mhsl.isi[i][j]);
                        first = false;
                    } else {
                        System.out.printf("X%d = ", (i + 1));
                    }
                } else {
                    if (Mhsl.isi[i][j] != 0) {
                        if (!first && Mhsl.isi[i][j] > 0) {
                            System.out.print("+");
                            first = false;
                        }
                        if (Mhsl.isi[i][j] == 1) {
                            System.out.printf(" X%d ", (j));
                        } else {
                            System.out.printf(" %.2fX%d ", Mhsl.isi[i][j], (j));
                        }
                    }
                }
            }
            System.out.println("");
        }
    }

    public static void SPLMenu() {
        /* KAMUS DATA */
        int cara = 0;
        int kategori = 0;
        Matriks M = new Matriks();
        Matriks Mhsl = new Matriks();
        
        /* ALGORITMA */
        
        System.out.println("\n  !! Masukkan SPL dalam bentuk Matriks (dipisahkan spasi) !!");
        Matriks.BacaKeyboard(M);
        

        while (cara<1 || cara > 4){
            System.out.println("\n+==============================================+");
            System.out.println("+-------- Pilih Metode Penyelesaian SPL -------+");
            System.out.println("|                                              |");
            System.out.println("|   1. Eliminasi Gauss                         |");
            System.out.println("|   2. Eliminasi Gauss-Jordan                  |");
            System.out.println("|   3. Matriks Invers                          |");
            System.out.println("|   4. Kaidah Cramer                           |");
            System.out.println("|                                              |");
            System.out.println("+----------------------------------------------+");

            System.out.print("  >>  Silahkan pilih metode penyelesaian SPL : ");
            cara = Menu.baca.nextInt();
        }

        if (cara == 1) {
            // Gauss
			Matriks.GaussianSPL(M);
			System.out.println("Matriks Hasil Eliminasi Gaussnya adalah");
			Matriks.TulisLayar(M);
			System.out.println("");
			Matriks.solusiGauss(M);
        } else if (cara == 2) {
            // Gauss Jordan
			Matriks.GaussJordan(M);
			System.out.println("Matriks Hasil Gauss Jordannya adalah");
			Matriks.TulisLayar(M);
			System.out.println("");
			Matriks.eliminasiBaris(M);
			Matriks.solusiGaussJordan(M);
        }
        else if(cara==3) {
            kategori = SPL.Invers(M, Mhsl);
        }
        else {
            kategori = SPL.Cramer(M, Mhsl);
        }

        if (kategori ==4) {
            System.out.println("Tidak dapat menggunakan metode yang dipilih.");
        }
        else if (kategori == 3) {
            System.out.println("SPL tidak memiliki solusi.");
        }
        else if (kategori == 2) {
            System.out.println("SPL tidak memiliki solusi tunggal.");
            SPL.TulisHasil(Mhsl);            
        }
        else if (kategori == 1) {
            System.out.println("Solusi SPL berturut-turut adalah: ");
            SPL.TulisHasil(Mhsl);
        }
    }

}
