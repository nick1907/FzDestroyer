package destroyer.friendzone.com.fzdestroyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

// dodaj tak by sprawdzic czy ktos jest zalogowany czy nie
// boolean loggedIn=false;
// if(AccessToken.getCurrentAccessToken()!=null)
//     loggedIn=true;

public class Logowanie extends Fragment
{
    ProfileTracker profileTracker;
    LoginButton loginButton;
    CallbackManager callback;
    boolean zalogowany = false;
    Profile profil = null;
    AccessTokenTracker accessTokenTracker;
    AccessToken accesToken;
    String email;
    String login;


    @Override
    public void onPause()
    {
        super.onPause();
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

        loginButton = (LoginButton) widok.findViewById(R.id.login_button);

        // nadanie wszystkich wymaganych (i nie tylko) uprawnien
        ArrayList<String> lista_uprawnien = new ArrayList<>();
        lista_uprawnien.add("user_friends");
        lista_uprawnien.add("email");
        lista_uprawnien.add("user_birthday");
        lista_uprawnien.add("user_about_me");
        lista_uprawnien.add("user_hometown");
        lista_uprawnien.add("user_location");
        lista_uprawnien.add("user_photos");
        lista_uprawnien.add("user_relationships");
        lista_uprawnien.add("user_status");
        lista_uprawnien.add("read_custom_friendlists");
        lista_uprawnien.add("read_mailbox");

        loginButton.setReadPermissions(lista_uprawnien);
        loginButton.setFragment(this);
        loginButton.registerCallback(callback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                zalogowany = true;
                Intent intent = new Intent(getActivity(), MenuActivity.class);

                startActivity(intent);
            }

            @Override
            public void onCancel()
            {
            }

            @Override
            public void onError(FacebookException exception)
            {
                Toast.makeText(getActivity(), "Błąd logowania.", Toast.LENGTH_SHORT).show();
            }
        });

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
                        Log.d("log: ", graphResponse.toString());
                        try
                        {
                            email = jsonObject.getString("email");
                            login = jsonObject.getString("id");
                            new WyslijDoBazy().execute();
                            SharedPreferences settings = getActivity().getSharedPreferences("PREF", 0);
                            SharedPreferences.Editor edytor = settings.edit();
                            edytor.putBoolean("zalogowany", zalogowany);
                            Log.d("zapisuje", login);
                            edytor.putString("profil", login);
                            edytor.apply();
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle params = new Bundle();
                params.putString("fields", "id,name,email,birthday");

                request.setParameters(params);
                request.executeAsync();
            }
        };

        /*
        if(AccessToken.getCurrentAccessToken()==null)
        {
            // zapisanie w pamieci urzadzenia ustawien programu
            SharedPreferences settings = getActivity().getSharedPreferences("PREF", 0);
            SharedPreferences.Editor edytor = settings.edit();
            edytor.putBoolean("zalogowany", zalogowany);
            edytor.apply();
        }
        else
        {
            Log.d("loguje sie", "loguje");
            // zapisanie w pamieci urzadzenia ustawien programu
            SharedPreferences settings = getActivity().getSharedPreferences("PREF", 0);
            SharedPreferences.Editor edytor = settings.edit();
            edytor.putBoolean("zalogowany", zalogowany);
            Log.d("zapisuje", login);
            edytor.putString("profil", login);
            edytor.apply();
        }
        */

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
            // utworzenie zapytania do bazy danych i jego wykoanie
            try
            {
                // zapytanie do bazy danych (utworzenie nowego uzytkownika programu)
                String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("haslo", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8");

                try
                {
                    URL url = new URL("http://vigorous-cheetah-65-226242.euw1.nitrousbox.com/nowy_uzytkownik.php");

                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    // wyslij zapytanie do bazy danych
                    BufferedReader reader;
                    wr.write(data);
                    wr.flush();

                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    // odczytaj odpowiezdz serwera
                    while ((line = reader.readLine()) != null)
                        Log.d("odpowiedz", line);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callback.onActivityResult(requestCode, resultCode, data);
    }
}
