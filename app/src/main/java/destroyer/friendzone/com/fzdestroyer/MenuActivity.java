package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class MenuActivity extends Activity
{
    ImageButton przycisk_czatu;
    ImageButton przycisk_ustawien;
    Intent intent;
    Activity aktywnosc = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        przycisk_czatu = (ImageButton) findViewById(R.id.przycisk_czatu);
        przycisk_czatu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(aktywnosc, Czat.class);
                SharedPreferences settings = getSharedPreferences("PREF", 0);
                if (settings.getString("profil", "").equals("882998035056017"))
                    intent.putExtra("profil_odbiorcy", "900358920032103");
                else
                    intent.putExtra("profil_odbiorcy", "882998035056017");
                startActivity(intent);
            }
        });

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
    }
}
