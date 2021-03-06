package com.dk.ricardo.eeas2.fragments.SingleFragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dk.ricardo.eeas2.utilidades.CustomJsonArrayRequest;
import com.dk.ricardo.eeas2.JavaBeans.Entidades.UserSingleton;
import com.dk.ricardo.eeas2.utilidades.ErrorAlarmTrigger;
import com.dk.ricardo.eeas2.utilidades.VolleySingletonAdapter;
import com.dk.ricardo.eeas2.R;
import com.dk.ricardo.eeas2.activities.MainActivity;

import org.json.JSONObject;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Fragment representing the login screen for B-upS.
 */
public class LoginFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener
{
    private int tipo, err;
    String user, pass;
    SharedPreferences loggin;
    TextInputLayout userTextInput;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.passTextInput);
        userTextInput = view.findViewById(R.id.userTextInput);
        final TextInputEditText passwordEditText = view.findViewById(R.id.passEditText);
        final TextInputEditText userEditText = view.findViewById(R.id.userEditText);
        MaterialButton loginButton = view.findViewById(R.id.loginButton);
        err=0;


        try {
            loggin = getContext().getSharedPreferences("LoginData", MODE_PRIVATE);

            tipo = loggin.getInt("type", 404);
            user = loggin.getString("user", "404");
            pass = loggin.getString("pass", "404");

            if (tipo != 404 || !user.equals("404") || !pass.equals("404")) {
                try{
                    UserSingleton.getInstance().setCum(user);
                    UserSingleton.getInstance().setPass(pass);
                    UserSingleton.getInstance().setTipoUser(tipo);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                //se hace query, si los datos coinciden con alguna cuenta se entra al sistema
                Intent inicio = new Intent(getActivity(), MainActivity.class);
                startActivity(inicio);
                getActivity().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPasswordValid(passwordEditText.getText()))
                {
                    passwordTextInput.setError(getString(R.string.eeas_error_password));
                }
                else
                {
                    userTextInput.setError(null);//limpiar ambos errores
                    passwordTextInput.setError(null); // Clear the error
                    try
                    {
                        cargarWebServiceAuth("loginValidation.php",userEditText.getText().
                                toString(),passwordEditText.getText().toString());
                    }catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                    user=userEditText.getText().toString();
                    pass=passwordEditText.getText().toString();
                }
            }
        });
        return view;
    }

    private void cargarWebServiceAuth(String webService,final String user,final String password)
    {
        String ip= getString(R.string.ip_webServices), url=""+ip+webService;
        CustomJsonArrayRequest customjsonArrayRequest=new CustomJsonArrayRequest(Request.Method.POST,url, null, this,this)
        {
            @Override
            protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("CUM",user);
            params.put("Pass",password);
            return params;
        }
        };
        VolleySingletonAdapter.getInstanceVolley(getContext()).addToRequestQueue(customjsonArrayRequest);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
            }
        }, 500);
    }
    //TODO: Agregar condiciones 2 y 3
    /**
     * Es un metodo para saber si la contraseña tiene las caracteristicas minimas necesarias
     * para poder ser enviado al servidor las cuales son:
     *      -Debe ser Mayor o igual a 8 Caracteres
     *      -Debe incluir un numero
     *      -Debe incluir un digito especial
     * @param text
     * Es el texto que recibe de el campo PassEditText
     * @return
     * Retorna un valor booleano que dice si es valido o no
     */
    private boolean isPasswordValid(@Nullable Editable text) {
        return !(text != null && text.length() >= 8);
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        if(tipo>=1&&tipo<=8)
        {
            Log.i("ERROR",error.toString());
        }
        else
        {
            //error de tipo de usuario
            if(tipo==0)
            {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        userTextInput.setError(getString(R.string.eeas_error_user));

                    }
                });
                Toast.makeText(getContext(),"Usuario no existe",Toast.LENGTH_SHORT).show();
            }else if(tipo==404)
            {
                Toast.makeText(getContext(),"Error en conexion",Toast.LENGTH_SHORT).show();
            }else if(tipo==2002)
            {
                Toast.makeText(getContext(),"Error en conexion a Base de datos",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(),"Error "+tipo,Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    public void onResponse(JSONObject response)
    {
        try
        {
            tipo=response.getInt("tipo");
            if((tipo>=1&&tipo<=8)&&(err<3))
            {
                SharedPreferences.Editor x;
                x= loggin.edit();
                x.putInt("type",tipo);
                x.putString("user",user);
                x.putString("pass",pass);
                x.commit();
                UserSingleton.getInstance().setCum(user);
                UserSingleton.getInstance().setPass(pass);
                UserSingleton.getInstance().setTipoUser(tipo);
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                try {
                    getActivity().finish();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            else
            {
                //error de tipo de usuario
                if(err>2)
                {
                    Toast.makeText(getContext(),"Maximo de intentos sobrepasado",Toast.LENGTH_LONG).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            err=0;
                        }
                    }, 10000);

                }
                else if(tipo==0)
                {
                    //
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            userTextInput.setError(getString(R.string.eeas_error_user));

                        }
                    });
                    err++;
                    Toast.makeText(getContext(),"Usuario no existe",Toast.LENGTH_SHORT).show();
                }else if(tipo==404)
                {
                    Toast.makeText(getContext(),"Error en conexion",Toast.LENGTH_SHORT).show();
                }else if(tipo==2002)
                {
                    Toast.makeText(getContext(),"Error en conexion a Base de datos",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.i("errorNose",""+tipo);
                }
            }

        }catch (Exception a)
        {
            Log.e("errorNose",""+a+" ee:"+tipo);
        }
    }

    public void setError()
    {
        this.err = 0;
    }


}