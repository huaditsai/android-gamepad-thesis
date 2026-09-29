package huadi.androidgamepad.btnconfig;

import huadi.androidgamepad.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class BtnConfigActivity extends Activity
{
	int screenWidth, screenHeight;
	private static final int File_REQUEST = 1001;

	SharedPreferences btnPosValue;

	RelativeLayout relativeLayout;

	boolean isAddBtn = false;
	boolean isMoveBtn = false;
	boolean isDeleteBtn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_btnconfig);
		
		if(getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_90 || getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_270)
			BtnValue.isLandscape = true;

		BtnValue.isConfig = true;

		ShowConfigChooseDialog();

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		btnPosValue = getSharedPreferences("BtnPosValue", 0);

		relativeLayout = (RelativeLayout) findViewById(R.id.linearLayout1);
		relativeLayout.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				int x = (int) event.getX();
				int y = (int) event.getY();
				//int pointerCount = event.getPointerCount(); // 幾個觸控點				

				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						if (!isAddBtn && !isMoveBtn && !isMoveBtn)
						{
							BtnAddDialog(x, y);
						}
						break;
					case MotionEvent.ACTION_UP:
						break;
					default:
						break;
				}

				//				Log.e("BtnConfigActivity", Integer.toHexString(x) + ", " + Integer.toHexString(y));
								Log.e("BtnConfigActivity", x + ", " + y + ", " + screenWidth + ", " + screenHeight);
				//				Log.e("BtnConfigActivity", Integer.parseInt("107", 16) * width / 1600 + ", " + Integer.parseInt("18d", 16)* height / 2695);
				return false;
			}
		});

	}

	private void ShowConfigChooseDialog()
	{
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(BtnConfigActivity.this);
		//builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setCancelable(false);
		builderSingle.setTitle("Select One:");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BtnConfigActivity.this, android.R.layout.select_dialog_singlechoice);
		arrayAdapter.add("New Profile");
		arrayAdapter.add("Load Profile");

		builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
					case 0:
						Intent intent = new Intent(Intent.ACTION_PICK); // 建立 "選擇檔案 Action" 的 Intent								
						intent.setType("image/*");// 過濾檔案格式
						Intent destIntent = Intent.createChooser(intent, "選擇檔案"); // 建立 "檔案選擇器" 的 Intent(第二個參數: 選擇器的標題)
						startActivityForResult(destIntent, File_REQUEST); // 切換到檔案選擇器 (它的處理結果, 觸發 onActivityResult 事件)
						break;
					case 1:
						//TODO 
						AlertDialog.Builder builderInner = new AlertDialog.Builder(BtnConfigActivity.this);
						//builderInner.setMessage(strName);
						builderInner.setTitle("Select ");
						final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BtnConfigActivity.this, android.R.layout.select_dialog_singlechoice);
						
						builderInner.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						});
						builderInner.show();
						break;

					default:
						break;
				}
			}
		});
		builderSingle.show();
	}

	private void BtnAddDialog(final int posX, final int posY)
	{
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(BtnConfigActivity.this);
		//builderSingle.setCancelable(false);
		builderSingle.setTitle("Add Button");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BtnConfigActivity.this, android.R.layout.select_dialog_singlechoice);
		arrayAdapter.add("Button 1");
		arrayAdapter.add("Button 2");
		arrayAdapter.add("Button 3");
		arrayAdapter.add("Button 4");
		arrayAdapter.add("Button Analog");

		builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
					case 0:
						AddBtnToScreen(R.drawable.btn1, posX, posY, "btnA");
						break;
					case 1:
						AddBtnToScreen(R.drawable.btn2, posX, posY, "btnB");
						break;
					case 2:
						AddBtnToScreen(R.drawable.btn3, posX, posY, "btnC");
						break;
					case 3:
						AddBtnToScreen(R.drawable.btn4, posX, posY, "btnD");
						break;
					case 4:
						AddBtnToScreen(R.drawable.analog, posX, posY, "btnAnalog");
						break;

					default:
						break;
				}
			}
		});
		builderSingle.show();
	}
	
	private void AddBtnToScreen(int resId, int posX, int posY, final String btnName)
	{
		final ImageView imageView = new ImageView(BtnConfigActivity.this);
		Bitmap backBitmap = BitmapFactory.decodeResource(getResources(), resId);
		//imageView.setImageResource(resId);
		imageView.setImageBitmap(backBitmap);
		imageView.setAlpha(0.5f);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(posX - backBitmap.getWidth() / 2, posY - backBitmap.getHeight() / 2, 0, 0);
		imageView.setLayoutParams(params);
		imageView.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				new AlertDialog.Builder(BtnConfigActivity.this)
				.setTitle("Config Button")
				.setMessage("Remove Button")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						relativeLayout.removeView(imageView);
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
					}
				}).show();
				//Toast.makeText(BtnConfigActivity.this, btnName, Toast.LENGTH_SHORT).show();
			}
		});

		relativeLayout.addView(imageView);
		if(!BtnValue.isLandscape)
		{
			btnPosValue.edit().putInt(btnName + "_X", (int)(posX * 1600f / screenWidth)).commit();
			btnPosValue.edit().putInt(btnName + "_Y", (int)(posY * 2695f / screenHeight)).commit();
		}
		else 
		{
			btnPosValue.edit().putInt(btnName + "_X", (int)((screenHeight - posY) * 1600f / screenHeight)).commit();
			btnPosValue.edit().putInt(btnName + "_Y", (int)(posX * 2695f / screenWidth)).commit();
		}
		
//		isAddBtn = true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == File_REQUEST && data != null && data.getData() != null)
		{
			Uri uri = data.getData();

			if (uri != null)
			{
				//User had pick an image.
				Cursor cursor = getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
				cursor.moveToFirst();

				//Link to the image
				final String imageFilePath = cursor.getString(0);
				cursor.close();

				relativeLayout.setBackground(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(imageFilePath)));
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		BtnValue.isConfig = false;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			BtnValue.isLandscape = true;
			relativeLayout.removeAllViews();
			//Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			BtnValue.isLandscape = false;
			relativeLayout.removeAllViews();
			//Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}

}
