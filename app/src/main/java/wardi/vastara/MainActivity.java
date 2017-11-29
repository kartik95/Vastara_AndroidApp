package wardi.vastara;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.riyagayasen.easyaccordion.AccordionView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private Button dob, sob, up;
    private TextView then, hiwc;
    private View hiwv;
    private LinearLayout dorl, sorl, brl;
    private ArrayList<String> din, dout, al, temp, bang, bac, rgb, vib, serv;
    private Spinner sp, sp1, sp2, sp3;
    private String dinS, sinS, doutS, soutS, hexString;
    private HashMap<String, String> hexCodes;
    private BluetoothHandler bluetoothHandler;
    public static Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bluetoothHandler = new BluetoothHandler();
        mcontext = this.getApplicationContext();
        prepareDropdowns();
        prepareHexCodes();

        bluetoothHandler.findBT();
        bluetoothHandler.openBT();

        dob = (Button) findViewById(R.id.but1);
        sob = (Button) findViewById(R.id.but2);
        up = (Button) findViewById(R.id.but3);
        then = (TextView) findViewById(R.id.tv3);
        dorl = (LinearLayout) findViewById(R.id.trl1);
        sorl = (LinearLayout) findViewById(R.id.trl2);
        brl = (LinearLayout) findViewById(R.id.rl3);
        sp = (Spinner) findViewById(R.id.spinner);
        sp1 = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, din);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long id) {

                TextView tmpView = sp.getSelectedView().findViewById(android.R.id.text1);
                dinS = tmpView.getText().toString();
                tmpView.setTextColor(Color.WHITE);

                ArrayAdapter<String> dataAdapter1;

                if(tmpView.getText().toString().equals("Ambient Light")) {

                    dataAdapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, al);
                }
                else if(tmpView.getText().toString().equals("Temperature")) {

                    dataAdapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, temp);
                }
                else if(tmpView.getText().toString().equals("Back Angle")) {

                    dataAdapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, bang);
                }
                else {

                    dataAdapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, bac);
                }

                dataAdapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sp1.setAdapter(dataAdapter1);

                sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View view,
                                               int position, long id) {

                        TextView tmpView1 = sp1.getSelectedView().findViewById(android.R.id.text1);
                        sinS = tmpView1.getText().toString();
                        tmpView1.setTextColor(Color.WHITE);
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // do stuff

                    }
                });
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // do stuff

            }
        });




        dorl.setVisibility(View.GONE);
        sorl.setVisibility(View.GONE);
        then.setVisibility(View.VISIBLE);
        up.setVisibility(View.GONE);


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dorl.setVisibility(View.VISIBLE);
                sorl.setVisibility(View.VISIBLE);
                then.setVisibility(View.GONE);
                up.setVisibility(View.VISIBLE);

                sp2 = (Spinner) findViewById(R.id.tspinner);
                sp3 = (Spinner) findViewById(R.id.tspinner1);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, dout);
                dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sp2.setAdapter(dataAdapter);

                sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View view,
                                               int position, long id) {

                        TextView tmpView1 = sp2.getSelectedView().findViewById(android.R.id.text1);
                        doutS = tmpView1.getText().toString();
                        tmpView1.setTextColor(Color.WHITE);

                        ArrayAdapter<String> dataAdapter1;

                        if(tmpView1.getText().toString().equals("RGB LED")) {

                            dataAdapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, rgb);
                        }
                        else {

                            dataAdapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, vib);
                        }

                        dataAdapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        sp3.setAdapter(dataAdapter1);

                        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            public void onItemSelected(AdapterView<?> arg0, View view,
                                                       int position, long id) {

                                TextView tmpView1 = sp3.getSelectedView().findViewById(android.R.id.text1);
                                soutS = tmpView1.getText().toString();
                                tmpView1.setTextColor(Color.WHITE);
                            }

                            public void onNothingSelected(AdapterView<?> arg0) {
                                // do stuff

                            }
                        });
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // do stuff

                    }
                });

            }
        });

        sob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dorl.setVisibility(View.VISIBLE);
                sorl.setVisibility(View.GONE);
                then.setVisibility(View.GONE);
                up.setVisibility(View.VISIBLE);

                sp2 = (Spinner) findViewById(R.id.tspinner);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, serv);
                dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sp2.setAdapter(dataAdapter);

                sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        TextView tmpView1 = sp2.getSelectedView().findViewById(android.R.id.text1);
                        tmpView1.setTextColor(Color.WHITE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });


        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hexString = "*o" + hexCodes.get(dinS) + hexCodes.get(sinS) + hexCodes.get(doutS) +
                hexCodes.get(soutS) + "#";
                Toast.makeText(getApplicationContext(), hexString, Toast.LENGTH_SHORT).show();
                try {
                    bluetoothHandler.sendData(hexString);
                    Log.d("Status1 : ", "Data sent.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void prepareDropdowns() {

        din = new ArrayList<>();
        din.add("Ambient Light");
        din.add("Temperature");
        din.add("Back Angle");
        din.add("Blood Alcohol Content");

        al = new ArrayList<>();
        al.add("Dark");
        al.add("Light");

        temp = new ArrayList<>();
        temp.add("Cold");
        temp.add("Hot");

        bang = new ArrayList<>();
        bang.add("Straight");
        bang.add("Bend");

        bac = new ArrayList<>();
        bac.add("Low");
        bac.add("High");

        dout = new ArrayList<>();
        dout.add("RGB LED");
        dout.add("Vibration Motor");

        rgb = new ArrayList<>();
        rgb.add("Red");
        rgb.add("Green");
        rgb.add("Blue");

        vib = new ArrayList<>();
        vib.add("Off");
        vib.add("On");

        serv = new ArrayList<>();
        serv.add("Emergency call");
        serv.add("Play music");
    }

    void prepareHexCodes() {

        hexCodes = new HashMap<>();
        hexCodes.put("Ambient Light", "11");
        hexCodes.put("Temperature", "19");
        hexCodes.put("Back Angle", "15");
        hexCodes.put("Blood Alcohol Content", "13");
        hexCodes.put("Vibration Motor", "21");
        hexCodes.put("RGB LED", "23");
        hexCodes.put("Dark", "0");
        hexCodes.put("Light", "1");
        hexCodes.put("Cold", "0");
        hexCodes.put("Hot", "1");
        hexCodes.put("Straight", "0");
        hexCodes.put("Bend", "1");
        hexCodes.put("Low", "0");
        hexCodes.put("High", "1");
        hexCodes.put("Red", "0");
        hexCodes.put("Green", "1");
        hexCodes.put("Blue", "2");
        hexCodes.put("Off", "0");
        hexCodes.put("On", "1");
    }
}
