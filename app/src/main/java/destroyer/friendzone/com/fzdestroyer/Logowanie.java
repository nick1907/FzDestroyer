package destroyer.friendzone.com.fzdestroyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class Logowanie extends Fragment
{
    JSONParser jsonParser = new JSONParser();
    ProfileTracker profileTracker;
    LoginButton loginButton;
    CallbackManager callback;
    private FacebookCallback facebookCallback;
    boolean zalogowany = false;
    Profile profil = null;
    AccessTokenTracker accessTokenTracker;
    AccessToken accesToken;

    @Override
    public void onPause()
    {
        super.onPause();

        SharedPreferences settings = getActivity().getSharedPreferences("PREF", 0);
        SharedPreferences.Editor edytor = settings.edit();
        edytor.putBoolean("zalogowany", zalogowany);
        edytor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View widok = inflater.inflate(R.layout.activity_logowanie, container, false);
        callback = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken)
            {
                if (oldAccessToken == null)
                    accesToken = currentAccessToken;
                else
                    accesToken = oldAccessToken;
            }
        };

        profileTracker = new ProfileTracker()
        {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile2)
            {
                if (profile != null)
                    profil = profile;
                else
                    profil = profile2;

                GraphRequest request = GraphRequest.newMeRequest(accesToken, new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse)
                    {
                        Log.d("wiadomosc_zwrotna:", graphResponse.toString());
                        Log.d("json_object:", jsonObject.toString());
                    }
                });

                Bundle params = new Bundle();
                params.putString("fields", "id,name,email");
                request.setParameters(params);
                request.executeAsync();

                new WyslijDoBazy().execute();
                Toast.makeText(getActivity(), "Zalogowales sie.", Toast.LENGTH_LONG).show();
            }
        };

        loginButton = (LoginButton) widok.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                zalogowany = true;
                Intent intent = new Intent(getActivity(), Czat.class);
                startActivity(intent);
            }

            @Override
            public void onCancel()
            {
            }

            @Override
            public void onError(FacebookException exception)
            {
                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
            }
        });

        return widok;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    class WyslijDoBazy extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            /*
            List<NameValuePair> lista = new ArrayList<NameValuePair>();
            lista.add(new BasicNameValuePair("login", profil.getId()));
            lista.add(new BasicNameValuePair("plec", "0"));
            lista.add(new BasicNameValuePair("data_urodzenia", "12-02-1990"));
            lista.add(new BasicNameValuePair("orientacja", "1"));
            lista.add(new BasicNameValuePair("zagieg", "90"));
            lista.add(new BasicNameValuePair("miejscowosc", "Wrocław"));
            lista.add(new BasicNameValuePair("email", profil.getId()));
            lista.add(new BasicNameValuePair("haslo", "supertajne"));

            */

            try
            {
                String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(profil.getId(), "UTF-8");
                data += "&" + URLEncoder.encode("plec", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
                data += "&" + URLEncoder.encode("data_urodzenia", "UTF-8") + "=" + URLEncoder.encode("12-02-1990", "UTF-8");
                data += "&" + URLEncoder.encode("orientacja", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("zasieg", "UTF-8") + "=" + URLEncoder.encode("90", "UTF-8");
                data += "&" + URLEncoder.encode("miejscowosc", "UTF-8") + "=" + URLEncoder.encode("Wrocław", "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(profil.getId(), "UTF-8");
                data += "&" + URLEncoder.encode("haslo", "UTF-8") + "=" + URLEncoder.encode("supertajne", "UTF-8");

                try
                {
                    URL url = new URL("http://vigorous-cheetah-65-226242.euw1.nitrousbox.com/nowy_uzytkownik.php");

                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    String text = "";
                    BufferedReader reader = null;
                    wr.write(data);
                    wr.flush();

                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null)
                        sb.append(new StringBuilder(line + "\n"));

                    Log.d("wiadomosc", sb.toString());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
//            JSONObject json = jsonParser.makeHttpRequest(adres, "POST", lista);

            /*
            try
            {
                int success = json.getInt("success");

                if (success == 1)
                {
                    Toast.makeText(getActivity(), "Udalo sie wyslac dane.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "Nie udalo sie wyslac danych.", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            */
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callback.onActivityResult(requestCode, resultCode, data);
    }

    // funkcja reagujaca na przycisk do logowania sie
    // sprawdza czy podane dane sa prawdziwe i jesli sa
    // nastepuje logowanie do aplikacji
    /*
    public void zalogujSie(View view)
    {
        String adres_email = "";// = email.getText().toString();
        String haslo_do_konta = "";// = haslo.getText().toString();

        // jesli nie podano zadnych danych
        if (adres_email.isEmpty() || haslo_do_konta.isEmpty())
            Toast.makeText(getActivity(), "Nie podano wymaganych danych.", Toast.LENGTH_LONG).show();
        else // jesli cos podano nalezy sprawdzic czy adres e-mail
        {   // jest prawidlowy i jesli jest to mozna sprobowac sie zalogowac

            final Pattern WZORZEC = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(" +
                    "\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+");

            // jesli adres e-mail jest podany w prawidlowy sposob
            if (WZORZEC.matcher(adres_email).matches())
            {
                Toast.makeText(getActivity(), "Jestem.", Toast.LENGTH_LONG).show();
                // zwroc dane do aktywnosci wywolujacej
                Intent intencja = new Intent();
                intencja.putExtra("email", adres_email);
                intencja.putExtra("haslo", haslo_do_konta);
                setResult(Activity.RESULT_OK, intencja);
                finish();
            }
            else
                Toast.makeText(this, "Podany adres e-mail jest nieprawidłowy.", Toast.LENGTH_LONG).show();
        }
    }
    */
}
