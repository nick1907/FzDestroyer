package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AdapterWiadomosci extends ArrayAdapter<Wiadomosc>
{
    Context kontekst;
    int layout_id;
    Wiadomosc tablica_wiadomosci [];

    public AdapterWiadomosci(Context context, int layout_id, Wiadomosc tablica[])
    {
        super(context, layout_id, tablica);
        kontekst = context;
        this.layout_id = layout_id;
        tablica_wiadomosci = tablica;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater;
        View widok = convertView;
        Item dodawany;

        if (widok == null)
        {
            layoutInflater = ((Activity)kontekst).getLayoutInflater();
            widok = layoutInflater.inflate(layout_id, parent, false);

            dodawany = new Item();
        }
        else
            dodawany = (Item) widok.getTag();

        Wiadomosc nowa = tablica_wiadomosci[position];

        if (nowa.czy_od_rozmowcy) // jesli to od naszego rozmowcy
        {
            dodawany.obrazek_rozmowcy = (ImageView) widok.findViewById(R.id.zdjecie_rozmowcy);
            dodawany.wiadomosc_rozmowcy = (TextView) widok.findViewById(R.id.wiadomosc_rozmowcy);
            dodawany.obrazek_rozmowcy.setVisibility(View.VISIBLE);
            dodawany.wiadomosc_rozmowcy.setVisibility(View.VISIBLE);
        }
        else // jesli to od biezacego uzytkownika
        {
            dodawany.obrazek_rozmowcy = (ImageView) widok.findViewById(R.id.nasze_zdjecie);
            dodawany.wiadomosc_rozmowcy = (TextView) widok.findViewById(R.id.nasza_wiadomosc);
            dodawany.obrazek_rozmowcy.setVisibility(View.VISIBLE);
            dodawany.wiadomosc_rozmowcy.setVisibility(View.VISIBLE);
        }

        return widok;
    }

    static class Item
    {
        ImageView obrazek_rozmowcy;
        TextView wiadomosc_rozmowcy;
    }
}
