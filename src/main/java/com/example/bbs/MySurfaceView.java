package com.example.bbs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;


public class MySurfaceView extends SurfaceView implements Callback, Runnable{
	private Thread th = new Thread(this);
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private SensorManager sm;
	private Sensor sensor;
	private SensorEventListener mySensorListener;
	private int i = 0, death = 0, ii = 0, iii = 0;
	private float x = 0, y = 0, arc_x, arc_y, s = 1,x1 = -1,y1 = -1,
			r1 = 10, x2[], y2[], x3 = -1, y3 = -1, x4 = -1, y4 = -1;
	private Context cc;
	
	public MySurfaceView(Context context) {
		super(context);
		this.cc=context;
		this.setKeepScreenOn(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
		sm = (SensorManager) MainActivity.ma.getSystemService(Service.SENSOR_SERVICE);
		sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mySensorListener = new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
				x = event.values[0];
				y = event.values[1];
				arc_x -= s*x;
				arc_y += s*y;
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};
		sm.registerListener(mySensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		arc_x = 0;
		arc_y = 0;
		th.start();
	}

	private int CalGreen(){
		double m=(double)((arc_x+r1-x1-10)*(arc_x+r1-x1-10)+(arc_y+r1-y1-10)*(arc_y+r1-y1-10));
		m=Math.sqrt(m);
		if(m < (r1 + 10))
			return 1;
		return 0;
	}
	private int CalRed(){
		for(int k = 0; k < i;k++)
		{
			if(x2[k] != -1)
			{
				double m=(double)((arc_x+r1-x2[k]-10)*(arc_x+r1-x2[k]-10)+(arc_y+r1-y2[k]-10)*(arc_y+r1-y2[k]-10));
				m = Math.sqrt(m);
				if(m < (r1 + 10))
					return 1;
			}
		}
		return 0;
	}
	private int CalBlue(){
		double m=(double)((arc_x+r1-x3-10)*(arc_x+r1-x3-10)+(arc_y+r1-y3-10)*(arc_y+r1-y3-10));
		m=Math.sqrt(m);
		if(m < (r1 + 10))
			return 1;
		return 0;
	}
	private int CalYellow(){
		double m=(double)((arc_x+r1-x4-10)*(arc_x+r1-x4-10)+(arc_y+r1-y4-10)*(arc_y+r1-y4-10));
		m = Math.sqrt(m);
		if(m < (r1 + 10))
		{
			for(int k = 0; k < i;k++)
			{
				if(x2[k] != -1)
				{
					m=(double)((x4+r1-x2[k]-10)*(x4+r1-x2[k]-10)+(y4+r1-y2[k]-10)*(y4+r1-y2[k]-10));
					m = Math.sqrt(m);
					if(m < 500)
					{
						x2[k] = y2[k] = -1;
						iii++;
					}
				}
			}
			return 1;
		}
		return 0;
	}
	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.WHITE);
				paint.setColor(Color.BLACK);
				paint.setAlpha(50);
				if(arc_x < 0)
					arc_x = 0;
				if(arc_y < 0)
					arc_y = 0;
				if(arc_x > this.getWidth() - r1 * 2)
					arc_x = this.getWidth() - r1 * 2;
				if(arc_y > this.getHeight() - r1 * 2)
					arc_y = this.getHeight() - r1 * 2;
				canvas.drawArc(new RectF(arc_x, arc_y, arc_x + r1 * 2,arc_y + r1 * 2), 0, 360, true, paint);
				paint.setARGB(254, 50, 205, 50);
				if(CalGreen() == 1)
					paint.setColor(Color.WHITE);
				canvas.drawArc(new RectF(x1, y1, x1 + 20 ,y1 + 20), 0, 360, true, paint);
				for(int k = 0;k < i; k++)
				{
					if(x2[k] != -1 && y2[k] != -1)
					{
						paint.setARGB(254, 227, 23, 13);
						canvas.drawArc(new RectF(x2[k], y2[k], x2[k] + 20 ,y2[k] + 20), 0, 360, true, paint);
					}
				}
				if(x3 != -1)
				{
					paint.setARGB(254, 61, 89, 171);
					canvas.drawArc(new RectF(x3, y3, x3 + 20 ,y3 + 20), 0, 360, true, paint);
				}
				if(x4 != -1)
				{
					paint.setARGB(254, 255, 215, 0);
					canvas.drawArc(new RectF(x4, y4, x4 + 20 ,y4 + 20), 0, 360, true, paint);
				}
				if(CalGreen() == 1)
				{
					x1 = y1 = -1;
					s += 0.2;
					r1 = r1 + 10;
				}
				if(CalBlue() == 1 && (i+ii+iii-1) != 0 && x3 != -1)
				{
					x3 = y3 = -1;
					r1 = 10;
					ii += 2;
				}
				if(CalYellow() == 1 && (i+ii+iii-1) != 0 && x4 != -1)
				{
					x4 = y4 = -1;
					iii++;
				}
				if(CalRed() == 1)
					death = 1;
				paint.setColor(Color.BLACK);
				paint.setTextSize(30);
				String temp_str = "Score: "+(i-1+ii+iii);
				canvas.drawText(temp_str, 0, 50, paint);
			}
		} catch (Exception e) {
			Log.v("Game","draw is Error!");
		} finally {
			sfh.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		i=0;
		x2 = new float[100];
		y2 = new float[100];
		for(int k = 0;k < 20; k++)
		{
			x2[k] = -1;
			y2[k] = -1;
		}
		while (true) {
			if(x1 == -1 && y1 == -1)
			{
				x1 = (float) (Math.random()*(this.getWidth()-15));
				y1 = (float) (Math.random()*(this.getHeight()-15));
				x2[i] = (float) (Math.random()*(this.getWidth()-15));
				y2[i] = (float) (Math.random()*(this.getHeight()-15));
				while(x2[i] <= arc_x + 2 * r1 && x2[i] >= arc_x 
						&& y2[i] <= arc_y +  2 * r1 && y2[i] >= arc_y)
				{
					x2[i] = (float) (Math.random()*(this.getWidth()-15));
					y2[i] = (float) (Math.random()*(this.getHeight()-15));
				}
				i++;
			}
			if((i + ii +iii - 1)%7 == 0 && (i + ii + iii) != 1 && x3 == -1 && y3 == -1)
			{
				x3 = (float) (Math.random()*(this.getWidth()-15));
				y3 = (float) (Math.random()*(this.getHeight()-15));
			}
			if((i + ii +iii - 1)%13 == 0 && (i + ii + iii) != 1 && x4 == -1 && y4 == -1)
			{
				x4 = (float) (Math.random()*(this.getWidth()-15));
				y4 = (float) (Math.random()*(this.getHeight()-15));
			}
			draw();
			if(death == 1)
				break;
			try {
				Thread.sleep(20);
			} catch (Exception ex) {
			}
		}
		GameOver();
	}
	public void GameOver(){
		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
		}
		MainActivity.ma.finish();
		Intent intent = new Intent(cc,MainActivity.class);
		intent.putExtra("finalPoint",i + ii + iii);
		cc.startActivity(intent);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}