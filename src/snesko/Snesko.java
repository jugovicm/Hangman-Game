package snesko;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Snesko {

    Random rnd;
    Scanner sc;
    private int pokusaj = 0;
    String pokusaji = " ";

    Snesko() {
        // ovde kreiramo novi objekat klase Random koji cemo koristiti kasnije 
        // u bilo kojoj metodi klase Snesko sa rnd.nextInt() ili neke slicne metode
        rnd = new Random();
        // donji red je dodat samo da bih vam pokazao kako se
        // koristi klasa Random. Najverovatnije cete ga moci kasnije ukloniti
        int slucBr = slucajanBroj(10);
        System.out.println("Primer generisanja broja 0 - 10: " + slucBr);
    }

    /**
     * Vraca slucajan broj u opsegu 0 - max Napomena: Stavio sam da je ova
     * metoda private jer nema smisla koristiti je van ove klase - npr iz klase
     * Igra kao snesko.slucajanBroj(10). Ova metoda je samo interna metoda koja
     * se koristi u ostalim metodama klase Snesko - tek cemo govoriti o private,
     * public i ostalim metodama
     *
     * @param max maksimalna vrednost slucajnog broja koja moze biti vracena
     * @return slucajan broj 0-max
     */
    private int slucajanBroj(int max) {
        int ret = rnd.nextInt(max + 1); //+1 zato sto nextInt vraca 0 - vrednost_parametra - 1
        return ret;
    }

    /**
     * Ova funkcija ce biti pozvana iz klase Igra Moj savet (nije obavezno) je
     * da dodajete novu metodu u ovoj klasi (slicnu metodi igraj) za svaki ili
     * barem za vecinu delova programa opisanih u dokumentu sa tekstom zadatka
     * (meni, citanje fajla dictionary.txt i biranje reci, unos slova/reci od
     * strane igraca, ...)
     */
    void igraj() throws IOException {
        sc = new Scanner(System.in);
        rnd = new Random();

        System.out.println("Мени: \n 1. Покрените игрицу \n 2. Уписивање речи у речник \n 3. Изађите из игрице");
        int meniIzbor = sc.nextInt();
        sc.nextLine();
        switch (meniIzbor) {
            case 1:
                System.out.println("Игра почиње.");
                String[] reciRecnik = citajIzFajla("dictionary.txt");
                int recBr = slucajanBroj(reciRecnik.length);
                System.out.println("Реч која се погађа: " + reciRecnik[recBr]);
                String recIzRecnika = reciRecnik[recBr];
                String skrivenaRecTmp = "";
                String skrivenaRec = pocetakIgre(recIzRecnika);
                skrivenaRecTmp = skrivenaRec.replaceAll("_", " _ ");
                System.out.println("Скривена реч: " + skrivenaRecTmp);
                System.out.println("Грешке: " + pokusaji);

                while ((recIzRecnika.equals(skrivenaRec) == false)) {
                    skrivenaRec = unosSlova(recIzRecnika, skrivenaRec);
                }
                System.out.println("Успешно сте погодили скривену реч.");
                break;
            case 2:
                System.out.println("Унесите реч коју желите да додате у речник");
                //sc.nextLine();
                String novaRec = sc.nextLine();
                upisNoveReci(novaRec);
                igraj();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Погрешан унос. Покушајте поново.");
                igraj();
                break;
        }

    }

    String[] citajIzFajla(String filename) throws FileNotFoundException, IOException {
        FileReader fileReader = new FileReader(filename);
        List<String> lines;
        try ( BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            lines = new ArrayList<>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines.toArray(String[]::new);
    }

    void upisNoveReci(String novaRec) throws IOException {
        boolean unosi = false;
        String[] reciRecnik = citajIzFajla("dictionary.txt");
        for (int i = 0; i < reciRecnik.length; i++) {
            if (reciRecnik[i].equals(novaRec) == true) {
                System.out.println("Унета реч постоји у речнику.");
                unosi = true;
                break;
            }
        }
        if (unosi == false) {
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("dictionary.txt", true)));
                out.println(novaRec);
                out.close();
                reciRecnik = citajIzFajla("dictionary.txt");
                int br = reciRecnik.length;
                br--;
                System.out.println("Унели сте реч: " + reciRecnik[br] + " у речник.");
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }

    private String pocetakIgre(String recKojuPogadjamo) {
        String skrivenaRec = "";
        for (int i = 0; i < recKojuPogadjamo.length(); i++) {
            skrivenaRec += "_";
        }
        return skrivenaRec;
    }

    private String unosSlova(String recKojaSePogadja, String skrivenaRec) throws IOException {
        String novaRecSlovo = "";
        String skrivenaRecTmp = "";
        boolean istiUlaz = false;
        System.out.println("Унесите слово/реч:");
        //sc.nextLine();
        novaRecSlovo = sc.nextLine();
        if (novaRecSlovo.length() > 1) {
            if (novaRecSlovo.equals(recKojaSePogadja) == true) {
                System.out.println("Успешно сте погодили скривену реч.");
                pokusaj = 0;
                pokusaji = " ";
                igraj();
            } else {
                pokusaji += novaRecSlovo + ", ";
                pokusaj++;
                if (pokusaj < 7) {
                    iscrtajSneska(pokusaj);
                } else if (pokusaj == 7) {
                    System.out.println("Скривена реч: " + skrivenaRecTmp);
                    System.out.println("Грешке: " + pokusaji);
                    iscrtajSneska(pokusaj);
                    pokusaj = 0;
                    pokusaji = " ";
                    System.out.println("Крај игре! Направили сте 7 грешака. Покушајте поново.");
                    igraj();
                }
                
                skrivenaRecTmp = skrivenaRec.replaceAll("_", " _ ");
                System.out.println("Скривена реч: " + skrivenaRecTmp);
                System.out.println("Грешке: " + pokusaji);
            }
        } else {
            if (recKojaSePogadja.contains(novaRecSlovo)) {
                for (int i = 0; i < recKojaSePogadja.length(); i++) {
                    if (recKojaSePogadja.charAt(i) == novaRecSlovo.charAt(0)) {
                        skrivenaRec = skrivenaRec.substring(0, i) + novaRecSlovo.charAt(0) + skrivenaRec.substring(i + 1);
                    }
                }
            } else {
                if (pokusaji.indexOf(' ' + novaRecSlovo + ',') >= 0) {
                    istiUlaz = true;
                    //break;
                } else {
                    pokusaji += novaRecSlovo + ", ";
                    pokusaj++;
                    if (pokusaj < 7) {
                        iscrtajSneska(pokusaj);
                    } else if (pokusaj == 7) {
                        System.out.println("Скривена реч: " + skrivenaRecTmp);
                        System.out.println("Грешке: " + pokusaji);
                        iscrtajSneska(pokusaj);
                        pokusaj = 0;
                        pokusaji = " ";
                        System.out.println("Крај игре! Направили сте 7 грешака. Покушајте поново.");
                        igraj();
                    }
                }
                
               // skrivenaRecTmp = skrivenaRec.replaceAll("_", " _ ");
              //  System.out.println("Trenutna rec:" + skrivenaRecTmp);
              //  System.out.println("Pokusaji:" + pokusaji);
            }
                skrivenaRecTmp = skrivenaRec.replaceAll("_", " _ ");
                System.out.println("Скривена реч: " + skrivenaRecTmp);
                System.out.println("Грешке: " + pokusaji);
        }
        return skrivenaRec;
    }

    private int iscrtajSneska(int pokusaj) throws FileNotFoundException {
        switch (pokusaj) {
            case 1:
                System.out.println("`======'");
                break;
            case 2:
                System.out.println("   (`^'^'`)\n"
                        + "   `======'");
                break;
            case 3:
                System.out.println("    (`^^')\n"
                        + "   (`^'^'`)\n"
                        + "   `======'");
                break;
            case 4:
                System.out.println(" >--(`^^')\n"
                        + "   (`^'^'`)\n"
                        + "   `======'");
                break;
            case 5:
                System.out.println("     ('')\n"
                        + " >--(`^^')\n"
                        + "   (`^'^'`)\n"
                        + "   `======'");
                break;
            case 6:
                System.out.println("     ('')___/\n"
                        + " >--(`^^')\n"
                        + "   (`^'^'`)\n"
                        + "   `======'");
                break;
            case 7:
                System.out.println("    _|==|_\n"
                        + "     ('')___/\n"
                        + " >--(`^^')\n"
                        + "   (`^'^'`)\n"
                        + "   `======'");
                break;
            default:
                break;
        }
        return pokusaj;
    }
}
