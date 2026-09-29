package huadi.androidgamepad.bluetooth;

import huadi.androidgamepad.Events;
import huadi.androidgamepad.R;
import huadi.androidgamepad.Events.InputDevice;
import huadi.androidgamepad.btnconfig.BtnValue;
import android.R.bool;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

public class BleDeviceControl
{
	private Activity mActivity;
	private Context mContext;

	Button btn_bleScan;
	
	SharedPreferences btnPosValue;

	private BleService mBluetoothLeService;

	Events events = new Events();
	int id = 0;
	
	int bleData, btn, x_axis, y_axis;

	public BleDeviceControl(Activity activity)
	{
		mActivity = activity;
		mContext = activity;

		mContext.registerReceiver(GattUpdateReceiver, BleService.getIntentFilter());
		Intent gattServiceIntent = new Intent(mActivity, BleService.class);
		//		mContext.startService(gattServiceIntent);
		mContext.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);		

		btn_bleScan = (Button) mActivity.findViewById(R.id.btn_bleScan);

		Events.intEnableDebug(1);
		events.Init();

		for (InputDevice idev : events.m_Devs)
		{
			idev.Open(true);
			if (idev.getOpen() && idev.getName().contains("touch"))
			{
				//Toast.makeText(this, "123", Toast.LENGTH_LONG).show();
				id = idev.getId();
			}
		}
		
		btnPosValue = mContext.getSharedPreferences("BtnPosValue", 0);
		//BtnValue.InitBtnPosValue(mContext);
	}

	public void DisConnect()
	{
		mContext.unbindService(mServiceConnection);
		mBluetoothLeService.disconnect();
		mBluetoothLeService = null;
	}

	// Code to manage Service lifecycle.
	private final ServiceConnection mServiceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder service)
		{
			mBluetoothLeService = ((BleService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize())
			{
				Log.e("BleDeviceControl", "Unable to initialize Bluetooth");
			}
			else
			{
				// Automatically connects to the device upon successful start-up initialization.
				mBluetoothLeService.connect(BleStatu.DeviceMacAddress);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName)
		{
			mBluetoothLeService = null;
		}
	};
	

	// Handles various events fired by the Service.
	private final BroadcastReceiver GattUpdateReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();
			if (BleService.ACTION_GATT_CONNECTED.equals(action))
			{
				Log.e("0", "ACTION_GATT_CONNECTED");
				BleStatu.isConnected = true;
				btn_bleScan.setText("DisConnect");
			}
			else if (BleService.ACTION_GATT_DISCONNECTED.equals(action))
			{
				Log.e("1", "ACTION_GATT_DISCONNECTED");
				BleStatu.isConnected = false;
				btn_bleScan.setText("Connect");
			}
			else if (BleService.ACTION_DATA_AVAILABLE.equals(action))
			{
				//Log.e("3", "" + intent.getIntExtra(BleService.EXTRA_DATA, 0));
				bleData = intent.getIntExtra(BleService.EXTRA_DATA, 0);			

				if (!BtnValue.isConfig)
				{
					int btn = bleData / 1000 / 1000;
					int x_axis = bleData / 1000 % 1000;
					int y_axis = bleData % 1000;
					
//					Log.e("btn", "" + btn);
					
					if(BtnValue.zeroValueBtn + 52 <= btn && btn <= BtnValue.zeroValueBtn + 58) //55
					{
//						if(btnPosValue.contains("btnA_X"))
//						{
							events.m_Devs.get(id).SendTest(btnPosValue.getInt("btnA_X", 0), btnPosValue.getInt("btnA_Y", 0));
//							Log.e("btnA", btnPosValue.getInt("btnA_X", 0) + ", " + btnPosValue.getInt("btnA_Y", 0));
							BtnValue.isBtnDown = true;
//							BtnValue.isBtnA = true;
//						}
					}
					else if(BtnValue.zeroValueBtn + 233 <= btn && btn <= BtnValue.zeroValueBtn + 239) //236
					{
//						if(btnPosValue.contains("btnB_X"))
//						{
							events.m_Devs.get(id).SendTest(btnPosValue.getInt("btnB_X", 0), btnPosValue.getInt("btnB_Y", 0));
							BtnValue.isBtnDown = true;
//							BtnValue.isBtnB = true;
//						}
					}
					else if(BtnValue.zeroValueBtn + 36 <= btn && btn <= BtnValue.zeroValueBtn + 42) //39
					{
//						if(btnPosValue.contains("btnC_X"))
//						{					
							events.m_Devs.get(id).SendTest(btnPosValue.getInt("btnC_X", 0), btnPosValue.getInt("btnC_Y", 0));
							BtnValue.isBtnDown = true;
//							BtnValue.isBtnC = true;
//						}
					}
					else if(BtnValue.zeroValueBtn + 91 <= btn && btn <= BtnValue.zeroValueBtn + 97) //94
					{
//						if(btnPosValue.contains("btnD_X"))
//						{					
							events.m_Devs.get(id).SendTest(btnPosValue.getInt("btnD_X", 0), btnPosValue.getInt("btnD_Y", 0));
							BtnValue.isBtnDown = true;
//							BtnValue.isBtnD = true;
//						}
					}
					else if(BtnValue.zeroValueBtn + 29 <= btn && btn <= BtnValue.zeroValueBtn + 35) //32
					{
//						if(btnPosValue.contains("btnAnalog_X"))
//						{					
							events.m_Devs.get(id).SendTest(btnPosValue.getInt("btnAnalog_X", 0), btnPosValue.getInt("btnAnalog_Y", 0));
							BtnValue.isBtnDown = true;
//							BtnValue.isBtnAnalog = true;
//						}
					}
					else
					{
						if(BtnValue.isBtnDown)
						{
							events.m_Devs.get(id).Release();
							BtnValue.isBtnDown = false;
							
//							BtnValue.isBtnA = false;
//							BtnValue.isBtnB = false;
//							BtnValue.isBtnC = false;
//							BtnValue.isBtnD = false;
//							BtnValue.isBtnAnalog = false;
						}
//						else
//							BtnValue.zeroValue = btn;
					}
					
					//Log.e("x, y", x_axis + ", " + y_axis);
					if(!BtnValue.isBtnDown &&
						(x_axis < BtnValue.zeroValueX - 11 || BtnValue.zeroValueX + 11 < x_axis
						|| y_axis < BtnValue.zeroValueY - 11 || BtnValue.zeroValueY + 11 < y_axis))
					{
//						if(btnPosValue.contains("btnAnalog_X"))
//						{
							BtnValue.analogMove_x = 0;
							BtnValue.analogMove_y = 0;
							
							if(x_axis < BtnValue.zeroValueX - 11 || BtnValue.zeroValueX + 11 < x_axis)
								BtnValue.analogMove_x = (x_axis - BtnValue.zeroValueX) / (float)(BtnValue.zeroValueX - 11);						
							
							if(y_axis < BtnValue.zeroValueY - 11 || BtnValue.zeroValueY + 11 < y_axis)
								BtnValue.analogMove_y = (y_axis - BtnValue.zeroValueY) / (float)(BtnValue.zeroValueY - 11);
							
							
							if(BtnValue.analogMove_x != 0 || BtnValue.analogMove_y != 0)
							{
								if(!BtnValue.isLandscape)
									events.m_Devs.get(id).SendTest(
										(int) (btnPosValue.getInt("btnAnalog_X", 0) + 200 * BtnValue.analogMove_x * Math.sqrt(1 - 0.5f * BtnValue.analogMove_y*BtnValue.analogMove_y)), 
										(int) (btnPosValue.getInt("btnAnalog_Y", 0) - 200 * BtnValue.analogMove_y * Math.sqrt(1 - 0.5f * BtnValue.analogMove_x*BtnValue.analogMove_x)));
								else
									events.m_Devs.get(id).SendTest(
										(int) (btnPosValue.getInt("btnAnalog_X", 0) + 200 * BtnValue.analogMove_y * Math.sqrt(1 - 0.5f * BtnValue.analogMove_x*BtnValue.analogMove_x)), 
										(int) (btnPosValue.getInt("btnAnalog_Y", 0) + 200 * BtnValue.analogMove_x * Math.sqrt(1 - 0.5f * BtnValue.analogMove_y*BtnValue.analogMove_y)));
								//Log.e("degree", "" + Math.toDegrees(Math.atan2(moveY, moveX))); // -180 ~ 180
								BtnValue.isAnalogMove = true;
							}
//						}
					}
					else if(BtnValue.isAnalogMove)
					{
						events.m_Devs.get(id).Release();
						BtnValue.isAnalogMove = false;
					}

					
					
				}

			}

		}
	};

}
