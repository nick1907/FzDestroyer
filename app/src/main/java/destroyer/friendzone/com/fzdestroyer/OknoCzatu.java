package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

// to bedzie fragment zajmujacy sie obsluga czatu
public class OknoCzatu extends Fragment
{
    ListView lista;
    Activity aktywnosc;
    ArrayList<Wiadomosc> elementy;
    AdapterWiadomosci adapterWiadomosci;
    Bundle dane;
    Handler handlerPobierania;

    public OknoCzatu()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View widok = inflater.inflate(R.layout.fragment_okno_czatu, container, false);

        dane = getActivity().getIntent().getExtras();

        aktywnosc = getActivity();
        elementy = new ArrayList<>();
        lista = (ListView) widok.findViewById(R.id.rozmowa);
        adapterWiadomosci = new AdapterWiadomosci(getActivity(), R.layout.wiadomosc_czatu, elementy);

        lista.setAdapter(adapterWiadomosci);

        handlerPobierania = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                elementy.add((Wiadomosc)msg.obj);
                adapterWiadomosci.notifyDataSetChanged();
            }
        };

        new Thread(new PobieranieWiadomosci());


        return widok;
    }

    public void wyslijWiadomosc(View view)
    {

    }

    class PobieranieWiadomosci implements Runnable
    {
        @Override
        public void run()
        {
            String profil1 = dane.getString("profil1");
            String profil2 = dane.getString("profil2");

            String ostatnia_wiadomosc = "";

            do
            {
                try
                {
                    // zapytanie do bazy danych (utworzenie nowego uzytkownika programu)
                    String data = URLEncoder.encode("login_nadawcy", "UTF-8") + "=" + URLEncoder.encode(profil1, "UTF-8");
                    data += "&" + URLEncoder.encode("login_odbiorcy", "UTF-8") + "=" + URLEncoder.encode(profil2, "UTF-8");

                    try
                    {
                        URL url = new URL("http://vigorous-cheetah-65-226242.euw1.nitrousbox.com/odczytaj_wiadomosc.php");

                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        BufferedReader reader;
                        wr.write(data);
                        wr.flush();

                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        int numer_linii = 0;
                        Wiadomosc wiadomosc = new Wiadomosc();

                        // odczytaj odpowiedz serwera
                        while ((line = reader.readLine()) != null)
                        {
                            if (numer_linii == 0)
                            {
                                if (line.equals("Error"))
                                    break;
                                else
                                {
                                    if (line.equals(profil1))
                                        wiadomosc.czy_od_rozmowcy = true;
                                    else
                                        wiadomosc.czy_od_rozmowcy = false;

                                    ++numer_linii;
                                }
                            }
                            else if (numer_linii == 2)
                            {
                                ++numer_linii;
                            }
                            else if (numer_linii == 3)
                            {
                                ostatnia_wiadomosc = ostatnia_wiadomosc.concat(line);
                            }
                            //   sb.append(new StringBuilder(line + "\n"));
                        }

                        if (line != null && line.equals("Error"))
                        {
                            wiadomosc.tresc = ostatnia_wiadomosc;
                            //elementy.add(wiadomosc);
                            //adapterWiadomosci.notifyDataSetChanged();

                            Message message = new Message();
                            message.obj = wiadomosc;

                            handlerPobierania.sendMessage(message);
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}
