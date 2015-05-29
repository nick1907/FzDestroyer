package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.SharedPreferences;
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
import android.widget.Toast;

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
    Handler handlerWyslania;
    ImageButton przycisk_wyslania;
    EditText pole_tekstowe;
    String profil1;
    String profil2;

    public OknoCzatu()
    {
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

        SharedPreferences settings = getActivity().getSharedPreferences("PREF", 0);

        dane = getActivity().getIntent().getExtras();

        profil1 = settings.getString("profil", "");
        Log.d("profil_nadawcy", profil1);
        profil2 = dane.getString("profil_odbiorcy");

        Log.d("profil_odbiorcy", profil2);

        aktywnosc = getActivity();
        elementy = new ArrayList<>();
        lista = (ListView) widok.findViewById(R.id.rozmowa);
        adapterWiadomosci = new AdapterWiadomosci(getActivity(), R.layout.wiadomosc_czatu, elementy);

        lista.setAdapter(adapterWiadomosci);

        // odswieza wyglad listy
        handlerPobierania = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                if (elementy != null)
                {
                    adapterWiadomosci = new AdapterWiadomosci(getActivity(), R.layout.wiadomosc_czatu, elementy);
                    lista.setAdapter(adapterWiadomosci);
                    adapterWiadomosci.notifyDataSetChanged();
                }
            }
        };

        // ustawia pole do wpisywania na puste
        handlerWyslania = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                pole_tekstowe.setText("");
            }
        };


        new Thread(new PobieranieWiadomosci()).start();
        pole_tekstowe = (EditText) widok.findViewById(R.id.nowa_wiadomosc);

        // dodanie sluchacza do przycisku wyslania wiadomosci
        przycisk_wyslania = (ImageButton) widok.findViewById(R.id.przycisk_wyslania_wiadomosci);
        przycisk_wyslania.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new WyslijWiadomosc().execute();
            }
        });


        return widok;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        handlerPobierania = null;
        handlerWyslania = null;
        lista = null;
        aktywnosc = null;
    }

    public void odswierzListe()
    {
        adapterWiadomosci.notifyDataSetChanged();
    }

    class WyslijWiadomosc extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String ... params)
        {
            String wczytany_tekst = pole_tekstowe.getText().toString();

            //jesli podano jakas tresc
            if (wczytany_tekst != null && !wczytany_tekst.isEmpty())
            {
                try
                {
                    // zapytanie do bazy danych (utworzenie nowego uzytkownika programu)
                    String data = URLEncoder.encode("login_nadawcy", "UTF-8") + "=" + URLEncoder.encode(profil1, "UTF-8");
                    data += "&" + URLEncoder.encode("login_odbiorcy", "UTF-8") + "=" + URLEncoder.encode(profil2, "UTF-8");
                    data += "&" + URLEncoder.encode("wiadomosc", "UTF-8") + "=" + URLEncoder.encode(wczytany_tekst, "UTF-8");

                    try
                    {
                        URL url = new URL("http://vigorous-cheetah-65-226242.euw1.nitrousbox.com/wstaw_wiadomosc_rev3.php");

                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        BufferedReader reader;
                        wr.write(data);
                        wr.flush();

                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;

                        // odczytaj odpowiezdz serwera
                        while ((line = reader.readLine()) != null)
                        {
                            if (line.equals("OK") && handlerWyslania != null)
                                handlerWyslania.sendMessage(new Message());
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

    // funkcja odwraca liste
    private void odwrocListe()
    {
        ArrayList<Wiadomosc> temp = new ArrayList<>();
        for (int i = elementy.size() - 1; i >= 0; --i)
            temp.add(elementy.get(i));

        elementy = temp;
    }

    class PobieranieWiadomosci implements Runnable
    {
        @Override
        public void run()
        {
            String ostatnia_wiadomosc = "";

            while (!Thread.interrupted())
            {
                new PobierzWiadomosc().execute();

                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                elementy.clear();
            }
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
                Log.d("doInBackground", "jestem w srodku");
                // zapytanie do bazy danych (utworzenie nowego uzytkownika programu)
                String data = URLEncoder.encode("login_nadawcy", "UTF-8") + "=" + URLEncoder.encode(profil1, "UTF-8");
                data += "&" + URLEncoder.encode("login_odbiorcy", "UTF-8") + "=" + URLEncoder.encode(profil2, "UTF-8");
                data += "&" + URLEncoder.encode("ilosc_wiadomosci", "UTF-8") + "=" + URLEncoder.encode("5", "UTF-8");

                try
                {
                    URL url = new URL("http://vigorous-cheetah-65-226242.euw1.nitrousbox.com/odczytaj_wiadomosc2.php");

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
                    wiadomosc.tresc = "";
                    wiadomosc.zdjecie_uzytkownika = R.drawable.gradient12;
                    wiadomosc.zdjecie_rozmowcy = R.drawable.gradient15;

                    // odczytaj odpowiedz serwera
                    while ((line = reader.readLine()) != null)
                    {
                        if (numer_linii == 0)
                        {
                            if (line.equals("Error"))
                                break;
                            ++numer_linii;
                        }
                        else if (numer_linii == 1)
                        {
                            if (line.equals(profil2))
                            {
                                Log.d("od kogo", "rozmowca");
                                wiadomosc.czy_od_rozmowcy = true;
                            }
                            else
                            {
                                Log.d("od kogo", "my");
                                wiadomosc.czy_od_rozmowcy = false;
                            }

                            ++numer_linii;
                        }
                        else if (numer_linii == 2)
                            ++numer_linii;
                        else if (numer_linii == 3)
                        {
                            wiadomosc.tresc = line;
                            ++numer_linii;
                        }

                        if (numer_linii == 4)
                        {
                            elementy.add(wiadomosc);
                            wiadomosc = new Wiadomosc();

                            wiadomosc.tresc = "";
                            wiadomosc.zdjecie_uzytkownika = R.drawable.gradient12;
                            wiadomosc.zdjecie_rozmowcy = R.drawable.gradient15;

                            numer_linii = 0;
                        }
                    }

                    if (elementy.size() > 0)
                    {
                        // odswierz liste
                        odwrocListe();

                        if (handlerPobierania != null)
                            handlerPobierania.sendMessage(new Message());
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
