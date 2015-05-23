package destroyer.friendzone.com.fzdestroyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends FragmentActivity
{
    private CallbackManager callback;
    Logowanie logowanie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);

        SharedPreferences settings =getSharedPreferences("PREF", 0);
        /*
        if (settings.getBoolean("zalogowany", false))
        {*/
            callback = CallbackManager.Factory.create();

            setContentView(R.layout.activity_main);

            FragmentManager fm = getSupportFragmentManager();
            logowanie = (Logowanie) fm.findFragmentById(R.id.logowanie_fragment);

            FragmentTransaction ft = fm.beginTransaction();
            ft.commit();
        /*}
        else
        {
            Intent intent = new Intent(this, Czat.class);
            startActivity(intent);
        }
        */
    }
    
    // nowy komentarz

    @Override
    protected void onResume()
    {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callback.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Jest udalo sie!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    /*
    @Override
    protected void onResumeFragments()
    {
        super.onResumeFragments();
    }
    */

    // funkcja reagujaca na przycisk
    // wlacza panel do logowania sie do profilu
    public void zalogujSie(View view)
    {
//        intencja = new Intent(this, Logowanie.class);

//        startActivity(intencja);
//        startActivityForResult(intencja, 0);
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intencja)
    {
        super.onActivityResult(requestCode, resultCode, this.intencja);

        String email = "";
        String haslo = "";

        Toast.makeText(this, "Otrzymano dane.", Toast.LENGTH_LONG).show();

        // sprawdz czy wywolanie nowej aktywnosci cos zwrocilo
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                // jesli zwrocono prawidlowo dane
                case 0:
                    // pobierz dane ze zwroconej intencji
                    email = intencja.getStringExtra("email");
                    haslo = intencja.getStringExtra("haslo");
                break;
            }
        }

        // jesli podano poprawne dane do logowania
        if (sprawdzDane(email, haslo))
        {
            Toast.makeText(this, "Podano poprawne dane do logowania.", Toast.LENGTH_LONG).show();
        }
    }
    */

    public boolean sprawdzDane(String login, String haslo)
    {
        // jesli i login i haslo sa podane
        if (!login.isEmpty() && !haslo.isEmpty())
        {
            // jesli dane do logowania sa poprawne
            if ((login.equals("konrad@vp.pl") && haslo.equals("konrad"))
                || (login.equals("marek@vp.pl") && haslo.equals("marek"))
                || (login.equals("ghost@shadow.pl") && haslo.equals("ghost")))
            {
                return true;
            }
        }

        Toast.makeText(this, "Podano niepoprawne dane do logowania.", Toast.LENGTH_LONG).show();

        return false;
    }
}
