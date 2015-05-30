package destroyer.friendzone.com.fzdestroyer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MenuActivity extends ActionBarActivity
{
    ImageButton przycisk_czatu;
    ImageButton przycisk_ustawien;
    ImageButton przycisk_pomocy;
    ImageButton przycisk_info;
    Intent intent;
    Activity aktywnosc = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        przycisk_czatu = (ImageButton) findViewById(R.id.przycisk_czatu);
        przycisk_czatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(aktywnosc, Czat.class);
                SharedPreferences settings = getSharedPreferences("PREF", 0);
                if (settings.getString("profil", "").equals("882998035056017"))
                    intent.putExtra("profil_odbiorcy", "900358920032103");
                else
                    intent.putExtra("profil_odbiorcy", "882998035056017");
                startActivity(intent);
            }
        });
        //fukcjonalnosc z ikonek, przeniesiona do ActionBar KZ
        przycisk_ustawien = (ImageButton) findViewById(R.id.przycisk_ustawien);
        przycisk_ustawien.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(aktywnosc, Ustawienia.class);
                startActivity(intent);
            }
        });
        przycisk_pomocy = (ImageButton) findViewById(R.id.przycisk_pomocy);
        przycisk_pomocy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                intent = new Intent(aktywnosc, Pomoc.class);
                startActivity(intent);
            }
        });

        przycisk_info = (ImageButton) findViewById(R.id.przycisk_info);
        przycisk_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(aktywnosc, Infos.class);
                startActivity(intent);
            }
        });



    }
    //metoda odpowiedzialna za wywolanie actionBar KZ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuvalues, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //metoda odpowiedzialna za wywolanie intencji z actionBar'a
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                intent = new Intent(aktywnosc, Pomoc.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(aktywnosc, Ustawienia.class);
                startActivity(intent);
                return true;
            case R.id.action_info:
                intent = new Intent(aktywnosc, Infos.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

