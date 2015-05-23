package destroyer.friendzone.com.fzdestroyer;

/**
 * Created by brzydki on 05/05/15.
 */
public class Element
{
    String imie;
    String nazwisko;
    String login;
    int obrazek;

    public Element(String imie, String nazwisko, String login, int zdjecie)
    {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.login = login;
        obrazek = zdjecie;
    }

    public Element(String login, int zdjecie)
    {
        imie = "";
        nazwisko = "";
        this.login = login;
        obrazek = zdjecie;
    }

    public Element(String login)
    {
        imie = "";
        nazwisko = "";
        this.login = login;
    }
}
