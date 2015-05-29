package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterWiadomosci extends ArrayAdapter<Wiadomosc>
{
    Context kontekst;
    int layout_id;
    ArrayList<Wiadomosc> tablica_wiadomosci;

    public AdapterWiadomosci(Context context, int layout_id, ArrayList<Wiadomosc> tablica)
    {
        super(context, layout_id, tablica);
        kontekst = context;
        this.layout_id = layout_id;
        tablica_wiadomosci = tablica;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View widok = convertView;
        Item dodawany;

        if (widok == null)
        {
            LayoutInflater layoutInflater = ((Activity)kontekst).getLayoutInflater();
            widok = layoutInflater.inflate(layout_id, parent, false);

            dodawany = new Item();
            dodawany.obrazek_rozmowcy = (ImageView) widok.findViewById(R.id.zdjecie_rozmowcy);
            dodawany.wiadomosc_rozmowcy = (TextView) widok.findViewById(R.id.wiadomosc_rozmowcy);
            dodawany.nasz_obrazek = (ImageView) widok.findViewById(R.id.nasze_zdjecie);
            dodawany.nasza_wiadomosc = (TextView) widok.findViewById(R.id.nasza_wiadomosc);

            widok.setTag(dodawany);
        }
        else
            dodawany = (Item) widok.getTag();

        if (tablica_wiadomosci.size() > position)
        {
            Wiadomosc nowa = tablica_wiadomosci.get(position);

            if (nowa.czy_od_rozmowcy) // jesli to od naszego rozmowcy
            {
                dodawany.obrazek_rozmowcy.setImageResource(nowa.zdjecie_rozmowcy);
                dodawany.wiadomosc_rozmowcy.setText(nowa.tresc);
            }
            else // jesli to od biezacego uzytkownika
            {
                dodawany.nasz_obrazek.setImageResource(nowa.zdjecie_uzytkownika);
                dodawany.nasza_wiadomosc.setText(nowa.tresc);
            }
        }

        return widok;
    }

    static class Item
    {
        ImageView obrazek_rozmowcy;
        TextView wiadomosc_rozmowcy;
        ImageView nasz_obrazek;
        TextView nasza_wiadomosc;
    }
}
