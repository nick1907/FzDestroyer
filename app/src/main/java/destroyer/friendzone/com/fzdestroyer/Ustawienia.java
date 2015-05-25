package destroyer.friendzone.com.fzdestroyer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Calendar;


public class Ustawienia extends ActionBarActivity
{
    // lista potrzebna do ustawienia spinneru na wybor orientacji seksualnej
    ArrayList<String> orientacja;

    Spinner wybor_orientacji;
    Spinner wybor_dnia;
    Spinner wybor_miesiaca;
    Spinner wybor_roku;

    EditText opis_pole; // opis uzytkownika (o sobie)
    EditText zasieg_pole; // zasieg w kilometrach potrzebny do poszukiwan drugiej "polowki"
    EditText miejscowosc_pole; // pole na miejscowosc

    // listy potrzebne do Spinnerow przy ustawianiu daty urodzenia
    ArrayList<String> dni;
    ArrayList<String> miesiace;
    ArrayList<String> lata;

    Bundle dane;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustawienia);

        dane = getIntent().getExtras();

        // przypisanie odpowiednich pol tekstowych
        opis_pole = (EditText) findViewById(R.id.pole_opisu);
        zasieg_pole = (EditText) findViewById(R.id.pole_zasiegu);
        miejscowosc_pole = (EditText) findViewById(R.id.miejscowosc_pole);

        wybor_orientacji = (Spinner) findViewById(R.id.wybor_orientacji);
        // etykiety do spinnera z wyborem orientacji
        orientacja = new ArrayList<>();
        orientacja.add("heteroseksualna");
        orientacja.add("biseksualna");
        orientacja.add("homoseksualna");

        // dodanie adaptera do spinnera
        ArrayAdapter<String> orientacja_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orientacja);
        orientacja_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wybor_orientacji.setAdapter(orientacja_adapter);

        // ustawienie listy z dniami miesiecy
        dni = new ArrayList<>();
        for (int i = 1; i < 32; ++i)
            dni.add(Integer.toString(i));

        // dodanie adaptera do spinnera z dniami miesiecy
        wybor_dnia = (Spinner) findViewById(R.id.wybor_dnia);
        ArrayAdapter adapter_dni = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dni);
        adapter_dni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wybor_dnia.setAdapter(adapter_dni);

        // wstawine do listy nazw miesiecy
        miesiace = new ArrayList<>();
        miesiace.add("Styczeń");
        miesiace.add("Luty");
        miesiace.add("Marzec");
        miesiace.add("Kwiecień");
        miesiace.add("Maj");
        miesiace.add("Czerwiec");
        miesiace.add("Lipiec");
        miesiace.add("Sierpień");
        miesiace.add("Wrzesień");
        miesiace.add("Październik");
        miesiace.add("Listopad");
        miesiace.add("Grudzień");

        // TODO
        // Spinner.setSelection(int position, boolean animate)

        // dodanie adaptera do spinnera z miesiacami
        wybor_miesiaca = (Spinner) findViewById(R.id.wybor_miesiaca);
        ArrayAdapter adapter_miesiecy = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, miesiace);
        adapter_miesiecy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wybor_miesiaca.setAdapter(adapter_miesiecy);

        // pobranie aktualnego roku
        Calendar kalendarz = Calendar.getInstance();
        int rok = kalendarz.get(Calendar.YEAR);

        // zapisanie lat do listy
        lata = new ArrayList<>();
        // wstawienie lat do listy
        for (int i = rok - 10; i > (rok - 66) ; --i)
            lata.add(Integer.toString(i));

        // dodanie adaptera do spinnera z latami
        wybor_roku = (Spinner) findViewById(R.id.wybor_roku);
        ArrayAdapter<String> adapter_lat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lata);
        adapter_lat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wybor_roku.setAdapter(adapter_lat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ustawienia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // metoda do aktualizacji danych w bazie danych
    public void zaaktualizujDane(View widok)
    {

    }
}
