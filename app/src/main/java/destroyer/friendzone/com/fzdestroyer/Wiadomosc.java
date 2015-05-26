package destroyer.friendzone.com.fzdestroyer;

/**
 * Created by brzydki on 26/05/15.
 */
public class Wiadomosc
{
    String tresc;
    Boolean czy_od_rozmowcy;
    int zdjecie_rozmowcy;
    int zdjecie_uzytkownika;

    public Wiadomosc(String wiadomosc, Boolean czy_moja, int zdjecie_rozmowcy, int zdjecie_uzytkownika)
    {
        tresc = wiadomosc;
        czy_od_rozmowcy = !czy_moja;
        this.zdjecie_rozmowcy = zdjecie_rozmowcy;
        this.zdjecie_uzytkownika = zdjecie_uzytkownika;
    }

    public Wiadomosc(String wiadomosc, Boolean czy_moja)
    {
        tresc = wiadomosc;
        czy_od_rozmowcy = !czy_moja;
    }
}
