package com.cloudpoint.plugins.ipc.sdk.app;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cloudpoint.ipc.protocol.hv.HSdk;
import com.cloudpoint.ipc.protocol.hv.domain.VH0001;
import com.cloudpoint.ipc.protocol.hv.domain.VH0002;
import com.cloudpoint.ipc.protocol.hv.domain.VH0003;
import com.cloudpoint.ipc.protocol.hv.domain.VH0004;
import com.cloudpoint.plugins.ipc.sdk.IIpcCallback;
import com.cloudpoint.plugins.ipc.sdk.IpcSdk;
import com.cloudpoint.plugins.ipc.sdk.domain.BaseRequest;
import com.cloudpoint.plugins.ipc.sdk.domain.BaseResponse;
import com.cloudpoint.plugins.ipc.sdk.protocol.IRequestCallback;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    HSdk hSdk;
    private Button button1;
    private Button button2;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        // 1.初始化
        hSdk = new HSdk();
        // 2.deviceId用设备id
        hSdk.initialize(getApplicationContext(), "992");
        // 3.注册云点请求回调
        IpcSdk.registerRequestCallback(new IRequestCallback[]{
                cs001Callback,
                cs002Callback,
                cs003Callback,
                cs004Callback
        });
        initListener();
    }

    private void initListener() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用云点apk上报硬件状态
                hv0001();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检测云点apk检测是否状态
                hv0002();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 触发客户端商户操作界面
                hv0003();
            }
        });
    }

    /**
     * 检测设备是否准备成功
     */
    IRequestCallback<VH0001.Request> cs001Callback = new IRequestCallback<VH0001.Request>() {
        @Override
        public void handleRequest(BaseRequest<VH0001.Request> baseRequest) {
            VH0001.Response response = new VH0001.Response();
            Log.e(VH0001.TAG, "VH0001");
            // 5.返回响应结果
            response.tx(getApplicationContext(), baseRequest, 1, "ok"); // 将response返回 0：faild 1：ok
        }
        /**
         * 请求的指令
         * @return
         */
        @Override
        public String getCmd() {
            return VH0001.TAG;
        }
    };

    /**
     * 初始化检测货道
     */
    IRequestCallback<VH0002.Request> cs002Callback = new IRequestCallback<VH0002.Request>() {
        @Override
        public void handleRequest(BaseRequest<VH0002.Request> baseRequest) {

            Log.e(VH0002.TAG, "VH0002");
            List<VH0002.CargoInfo> list = new ArrayList<>();
            for (int i = 0; i < 6; i ++) {
                VH0002.CargoInfo cargoInfo = new VH0002.CargoInfo();
                cargoInfo.setRow(i);
                cargoInfo.setColumn(10);
                list.add(cargoInfo);
            } // 设置货道信息
            VH0002.Response response = new VH0002.Response();
            response.setCargo_info(list);
            // 5.返回响应结果 将数据返回给云点apk
            response.tx(getApplicationContext(), baseRequest, 1, "ok"); // 将response返回  // 将response返回 0：faild 1：ok
        }
        /**
         * 请求的指令
         * @return
         */
        @Override
        public String getCmd() {
            return VH0002.TAG;
        }
    };


    /**
     * 商品长宽高信息
     */
    IRequestCallback<VH0003.Request> cs003Callback = new IRequestCallback<VH0003.Request>() {
        @Override
        public void handleRequest(BaseRequest<VH0003.Request> baseRequest) {

            Log.e(VH0003.TAG, "VH0003");
            VH0003.Response response = new VH0003.Response();
            // 5.返回响应结果
            response.tx(getApplicationContext(), baseRequest, 1, "ok"); // 将response返回
        }
        /**
         * 请求的指令
         * @return
         */
        @Override
        public String getCmd() {
            return VH0003.TAG;
        }
    };

    /**
     * 商品掉落信息
     */
    IRequestCallback<VH0004.Request> cs004Callback = new IRequestCallback<VH0004.Request>() {
        @Override
        public void handleRequest(BaseRequest<VH0004.Request> baseRequest) {
            Log.e(VH0004.TAG, "VH0004");
            List<VH0004.DropInfo> list = new ArrayList<>();
            for (int i = 0; i < baseRequest.getData().getDrop_info().size(); i++) {
                VH0004.DropInfo dropInfo = new VH0004.DropInfo();
                dropInfo.setRequest_number(baseRequest.getData().getDrop_info().get(i).getRequest_number()); // 需要掉落数
                dropInfo.setResult_number(1); // 实际掉落数
                VH0004.Coordinate coordinate = new VH0004.Coordinate(); // 掉落位置
                coordinate.setX(1); // x坐标
                coordinate.setY(1); // y坐标
                dropInfo.setCoordinate(coordinate);
                list.add(dropInfo);
            } // 设置掉落信息
            VH0004.Response response = new VH0004.Response();
            response.setDrop_info(list);
            // 5.返回响应结果
            response.tx(getApplicationContext(), baseRequest, 1, "ok"); // 将response返回
        }
        /**
         * 请求的指令
         * @return
         */
        @Override
        public String getCmd() {
            return VH0004.TAG;
        }
    };

    /**
     * 6.向云点发起请求，设备状态
     */
    public void hv0001() {
        hSdk.hv0001(1,new IIpcCallback<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {

            }
        });
    }

    /**
     * (2).HV0002检测VemApk状态是否正常
     */
    public void hv0002() {
        hSdk.hv0002(new IIpcCallback<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {

            }
        });
    }

    /**
     * (3).HV0003触发客户端商户操作界面
     */
    public void hv0003() {
        hSdk.hv0003(new IIpcCallback<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {

            }
        });
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
       // EventBus.getDefault().unregister(this);
        if(hSdk!=null)
            // 7.释放资源
            hSdk.onDestroy();
    }
}
