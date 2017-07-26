package ahuja.shivam.flashlightapp;

import android.content.pm.PackageManager;

import android.hardware.Camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView mSwitchIv;
    private boolean hasFlash;
    private RelativeLayout mRelativeLayout;

    private boolean isflashon;
    private CameraManager mCameraManager;
    private String mCameraId;
    private TextView mONOFFTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwitchIv=(ImageView)findViewById(R.id.Switch);
        mRelativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);
        mONOFFTv=(TextView)findViewById(R.id.on_off_tv);
        hasflash();
        turnOnFlash();
        mSwitchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(MainActivity.this,"clicked",Toast.LENGTH_LONG).show();
                if(isflashon)
                {
                    turnOffFlash();
                }
                else turnOnFlash();
            }
        });




    }

    private void hasflash() {
        hasFlash=getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!hasFlash)
        {
            Snackbar.make(mRelativeLayout,"you phone does not support FLASHLIGHT",Snackbar.LENGTH_INDEFINITE).setAction("close", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            }).show();
        }
        else getCamera();
    }

    private void getCamera() {
    mCameraManager=(CameraManager)getSystemService(CAMERA_SERVICE);
        try {
            mCameraId=mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    void turnOnFlash()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            try {
                mCameraManager.setTorchMode(mCameraId,true);
                isflashon=true;
                toggle();
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Snackbar.make(mRelativeLayout,"unable to switch FLASHLIGHT on",Snackbar.LENGTH_INDEFINITE).show();
            }
        }
    }
    void turnOffFlash()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            try {
                mCameraManager.setTorchMode(mCameraId,false);
                isflashon=false;
                toggle();
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Snackbar.make(mRelativeLayout,"unable to switch FLASHLIGHT off",Snackbar.LENGTH_INDEFINITE).show();
            }
        }
    }
    void toggle()
    {
        if(isflashon)
        {
            mSwitchIv.setImageResource(R.drawable.switch_on);
            mONOFFTv.setText("ON");
        }
        else {
            mSwitchIv.setImageResource(R.drawable.switch_off);
            mONOFFTv.setText("OFF");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }
}
