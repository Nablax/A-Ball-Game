package com.example.bbs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	public static MainActivity ma;
	private Button b3,b2,b1;
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		ma = this;
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Bundle b = getIntent().getExtras();
		if(b != null)
		{
			int tmp = b.getInt("finalPoint") - 1;
			AlertDialog.Builder builder = new Builder(MainActivity.this);
			builder.setTitle("Game over!");
			builder.setMessage("Score: " + tmp);
			builder.setPositiveButton("Try again!",  new DialogInterface.OnClickListener() {
         
                @Override 
                public void onClick(DialogInterface dialog, int which) { 
                    setContentView(new MySurfaceView(MainActivity.this));
                } 
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override 
                public void onClick(DialogInterface dialog, int which) {
                } 
            }).show();
		}
		initKJ();
		//Jump();
	}
	private void initKJ(){
		b2 = (Button) findViewById(R.id.btnSecond);
		b2.setOnClickListener(this);
		b1 = (Button) findViewById(R.id.btnFirst);
		b1.setOnClickListener(this);
		b3 = (Button) findViewById(R.id.btnThird);
		b3.setOnClickListener(this);
	}
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v){
		if(v.equals(b2))
			setContentView(new MySurfaceView(this));
		else if(v.equals(b1))
		{
			finish();
			System.exit(0);
		}
		else if(v.equals(b3))
		{
			Intent intent=new Intent(this,RuleActivity.class);
			startActivity(intent);
		}
	}
}
