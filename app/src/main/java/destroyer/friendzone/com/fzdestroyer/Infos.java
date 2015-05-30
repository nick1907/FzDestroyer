package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by KZ.
 */
public class Infos extends Activity {

    private ListView listaInfo ;
    private String[] tutajinformacje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);

        listaInfo = (ListView) findViewById(R.id.listaInfo);
        initResources();

    }

    private void initResources() {
        Resources res = getResources();
        tutajinformacje = res.getStringArray(R.array.tutajinformacje);
        initListaInfo();
    }
    private void initListaInfo() {
        listaInfo.setBackgroundColor(Color.RED);
        listaInfo.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, tutajinformacje));
    }
}

