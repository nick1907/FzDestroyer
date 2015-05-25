package destroyer.friendzone.com.fzdestroyer;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

// glowna klasa zajmujaca sie logowaniem i opcjami programu
public class MainActivity extends FragmentActivity
{
    private CallbackManager callback;
    Logowanie logowanie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // inicjalizacja FACEBOOK API
        FacebookSdk.sdkInitialize(this);

        // tworzenie w pamieci urzadzenia ustawien programu
//        SharedPreferences settings =getSharedPreferences("PREF", 0);

        callback = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        // tworzenie nowego fragmentu (fragmentu logowania)
        FragmentManager fm = getSupportFragmentManager();
        logowanie = (Logowanie) fm.findFragmentById(R.id.logowanie_fragment);

        FragmentTransaction ft = fm.beginTransaction();
        ft.commit();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callback.onActivityResult(requestCode, resultCode, data);
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
}
