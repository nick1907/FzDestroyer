package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
    ImageButton przycisk_wyslania;
    EditText pole_tekstowe;
    boolean wyslano;

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
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                elementy.add((Wiadomosc)msg.obj);
                Log.d("handle_odpowiedz: ", ((Wiadomosc) msg.obj).tresc);
                adapterWiadomosci.notifyDataSetChanged();
            }
        };

        new Thread(new PobieranieWiadomosci());
        pole_tekstowe = (EditText) widok.findViewById(R.id.nowa_wiadomosc);

        // dodanie sluchacza do przycisku wyslania wiadomosci
        przycisk_wyslania = (ImageButton) widok.findViewById(R.id.przycisk_wyslania_wiadomosci);
        przycisk_wyslania.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                wyslano = false;
                new WyslijWiadomosc().execute();

                if (wyslano)
                    pole_tekstowe.setText("");
            }
        });


        return widok;
    }

    class WyslijWiadomosc extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String ... params)
        {

//                Toast.makeText(getActivity(), "wysylam dane", Toast.LENGTH_SHORT).show();
            String wczytany_tekst = pole_tekstowe.getText().toString();

            //jesli podano jakas tresc
            if (wczytany_tekst != null && !wczytany_tekst.isEmpty())
            {
                String profil1 = "882998035056017"; //dane.getString("profil1");
                String profil2 = "900358920032103"; //dane.getString("profil2");

                try
                {
                    // zapytanie do bazy danych (utworzenie nowego uzytkownika programu)
                    String data = URLEncoder.encode("login_nadawcy", "UTF-8") + "=" + URLEncoder.encode(profil1, "UTF-8");
                    data += "&" + URLEncoder.encode("login_odbiorcy", "UTF-8") + "=" + URLEncoder.encode(profil2, "UTF-8");
                    data += "&" + URLEncoder.encode("wiadomosc", "UTF-8") + "=" + URLEncoder.encode(wczytany_tekst, "UTF-8");

                    try
                    {
                        URL url = new URL("http://vigorous-cheetah-65-226242.euw1.nitrousbox.com/wstaw_wiadomosc_rev2.php");

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

                        // odczytaj odpowiezdz serwera
                        while ((line = reader.readLine()) != null)
                        {
                            if (line.equals("OK"))
                                wyslano = true;
                            Log.d("odpowiedz_serwera:", line);
                        }

                        reader.close();
                        wr.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    class PobieranieWiadomosci implements Runnable
    {
        @Override
        public void run()
        {
            String ostatnia_wiadomosc = "";

            do
            {
                new PobierzWiadomosc().execute();
            } while (1 == 1);
        }
    }

    class PobierzWiadomosc extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            String profil1 = "882998035056017"; //dane.getString("profil1");
            String profil2 = "900358920032103"; //dane.getString("profil2");

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
                        Log.d("serwer_odeslal:", line);
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
                        //else if (numer_linii == 3)
                        //{
                            //ostatnia_wiadomosc = ostatnia_wiadomosc.concat(line);
                    //  }
                    //   sb.append(new StringBuilder(line + "\n"));
                    }

                    if (line != null && line.equals("Error"))
                    {
                        // wiadomosc.tresc = ostatnia_wiadomosc;
                        // elementy.add(wiadomosc);
                        // adapterWiadomosci.notifyDataSetChanged();

                        Message message = new Message();
                        message.obj = wiadomosc;

                        handlerPobierania.sendMessage(message);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}
