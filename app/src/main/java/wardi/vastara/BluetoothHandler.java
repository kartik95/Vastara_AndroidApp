package wardi.vastara;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by gkartik on 26/11/17.
 */

public class BluetoothHandler {

    private static final String LOG_TAG = "BluetoothHandler";
    private static final UUID EMBEDDED_BOARD_SPP = UUID
            .fromString("00001112-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothSocket fallbackSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    private List<UUID> uuidCandidates;
    Thread workerThread;
    private int success;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;


    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            Toast.makeText(MainActivity.mcontext, "No bluetooth adapter available.", Toast.LENGTH_LONG).show();
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Toast.makeText(MainActivity.mcontext, "Enable bluetooth", Toast.LENGTH_LONG).show();
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                Log.d(LOG_TAG, device.getName());
                if(device.getName().equals("HC-05"))
                {
                    mmDevice = device;
                    Toast.makeText(MainActivity.mcontext, "Bluetooth Device Found", Toast.LENGTH_LONG).show();
                    Log.d("Status : ", "Bluetooth device found.");
                    break;
                }
            }
        }
    }


    void openBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method getUUIDsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
            ParcelUuid[] uuids = (ParcelUuid[]) getUUIDsMethod.invoke(mBluetoothAdapter, null);

            for(ParcelUuid uuid: uuids) {
                Log.d("UUID : ", uuid.getUuid().toString());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        UUID uuid = UUID.fromString("00001112-0000-1000-8000-00805f9b34fb");
        Log.d("Device : ", mmDevice.toString());//Standard SerialPortService ID
        if(mmDevice != null){

            try {
//                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//                Log.d("BSocket : ", mmSocket.toString());
                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mmSocket.connect();
            } catch (IOException e) {
//                e.printStackTrace();
                try {
                    Class<?> clazz = mmSocket.getRemoteDevice().getClass();
                    Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
                    Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                    Object[] params = new Object[] {Integer.valueOf(1)};

                    fallbackSocket = (BluetoothSocket) m.invoke(mmSocket.getRemoteDevice(), params);
                    fallbackSocket.connect();

                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

            try {
                mmOutputStream = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mmInputStream = mmSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            beginListenForData();
//            Toast.makeText(MainActivity.mcontext, "Closed bluetooth opened", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(MainActivity.mcontext, "Bluetooth device not ready", Toast.LENGTH_LONG).show();
        }
    }


    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            // MainActivity.instructions.setText(data);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }


    void sendData(String data) throws IOException
    {
        String msg = data;
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        Log.d("Status1 : ", "Data sent.");
//        Toast.makeText(MainActivity.mcontext, "Data sent", Toast.LENGTH_LONG).show();
        // MainActivity.instructions.setText("Data Sent");
    }


    void closeBT() throws IOException {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
//        Toast.makeText(MainActivity.mcontext, "Closed bluetooth comm.", Toast.LENGTH_LONG).show();
        //
    }
}
