package com.kunleen.sn.sportnewsapplication.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.CircleImageView;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.custom.wheel.OnWheelChangedListener;
import com.kunleen.sn.sportnewsapplication.custom.wheel.WheelView;
import com.kunleen.sn.sportnewsapplication.custom.wheel.adapters.ArrayWheelAdapter;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.CityModel;
import com.kunleen.sn.sportnewsapplication.network.bean.DistrictModel;
import com.kunleen.sn.sportnewsapplication.network.bean.ProvinceModel;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqChangeAddress;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqChangeImg;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_Image;
import com.kunleen.sn.sportnewsapplication.network.bean.RevertAddressEntity;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.FileUtils;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;
import com.kunleen.sn.sportnewsapplication.utils.ShareUtils;
import com.kunleen.sn.sportnewsapplication.utils.StringUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import io.reactivex.Observable;

public class UserDetailActivity extends BaseActivity implements View.OnClickListener, OnWheelChangedListener {
    private int type;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private File tempFile;
    private Uri uri;
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;


    TitleBarView titleBar;
    CircleImageView cir_head;
    TextView tv_nickname, tv_phonenum, tv_address, tv_change_address;
    View btn_exit, rl_city, ll_wheel, rl_nickname, rl_headimg;
    private RevertAddressEntity addressEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DevicesUtils.setWindowStatusBarColor(this, R.color.white);
        setContentView(R.layout.activity_user_detail);
        createCameraTempFile(savedInstanceState);
        initTitleBar();
        if (addressEntity == null)
            addressEntity = new RevertAddressEntity();
        initView();
        initWindow(this);
    }

    private void initView() {
        cir_head = findViewById(R.id.cir_detail_head);
        tv_address = findViewById(R.id.tv_detail_address);
        if (SNApplication.userInfo.getUserInfo().getUserInfo().getCity() != "" && SNApplication.userInfo.getUserInfo().getUserInfo().getProvince() != "") {
            tv_address.setText(SNApplication.userInfo.getUserInfo().getUserInfo().getProvince() + "  " + SNApplication.userInfo.getUserInfo().getUserInfo().getCity());
        }
        tv_change_address = findViewById(R.id.tv_change_address);
        tv_change_address.setOnClickListener(this);
        tv_nickname = findViewById(R.id.tv_detail_nickname);
        tv_phonenum = findViewById(R.id.tv_detail_phonenum);
        ll_wheel = findViewById(R.id.ll_wheel);
        btn_exit = findViewById(R.id.btn_exit);
        rl_city = findViewById(R.id.rl_city);
        rl_nickname = findViewById(R.id.rl_nickname);
        rl_nickname.setOnClickListener(this);
        rl_headimg = findViewById(R.id.rl_headimg);
        rl_headimg.setOnClickListener(this);
        rl_city.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        initWheel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SNApplication.userInfo.getUserInfo() != null) {
            Glide.with(this).load(SNApplication.userInfo.getUserInfo().getUserInfo().getHeadImage()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.img_loading1).error(R.mipmap.wd_touxiang)).into(cir_head);
            tv_nickname.setText(SNApplication.userInfo.getUserInfo().getUserName());
            tv_phonenum.setText(SNApplication.userInfo.getUserInfo().getMobile());
        } else {
            finish();
        }
    }

    private void initWheel() {
        mViewProvince = findViewById(R.id.id_province);
        mViewCity = findViewById(R.id.id_city);
//        mViewDistrict = (WheelView) findViewById(R.id.id_district);
//        mViewDistrict.setVisibility(View.GONE);
        setUpListener();
        setUpData();
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
//        mViewDistrict.addChangingListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
//        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    private void initTitleBar() {
        titleBar = findViewById(R.id.titlebar);
        titleBar.setTitle("个人资料");
        titleBar.setActivity(this);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
//        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
//        mViewDistrict.setCurrentItem(0);

//        //县
//        int districtIndex = mViewDistrict.getCurrentItem();
//        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[districtIndex];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit: {
                SNApplication.userInfo.setUserInfo(null);
                setResult(300);
                finish();
                break;
            }
            case R.id.tv_change_address: {
                AppUtils.showLoadingDialog(this);
                TRequest<ReqChangeAddress> tRequest = new TRequest<>();
                ReqChangeAddress reqChangeAddress = new ReqChangeAddress();
                reqChangeAddress.setUid(SNApplication.userInfo.getUserInfo().getUid());
                reqChangeAddress.setProvince(mCurrentProviceName);
                reqChangeAddress.setCity(mCurrentCityName);
                tRequest.setParam(reqChangeAddress, "1123", "1");
                Observable<TResponse> tResponse_emptyObservable = RetrofitClient.getInstance().getService(HttpService.class).ChangeAddress(tRequest);
                sendRequest(tResponse_emptyObservable, tResponse -> {
                    ToastUtils.showToast("地址修改成功！");
                    AppUtils.hideLoadingDialog();
                    tv_change_address.setVisibility(View.GONE);
                    ll_wheel.setVisibility(View.GONE);
                    SNApplication.userInfo.getUserInfo().getUserInfo().setCity(mCurrentCityName);
                    SNApplication.userInfo.getUserInfo().getUserInfo().setProvince(mCurrentProviceName);
                }, throwable -> ToastUtils.showToast(throwable.getMessage()));
                break;
            }
            case R.id.rl_city: {
                DevicesUtils.hideKeyboard(this);
                int _v = View.VISIBLE;
                if (ll_wheel.getVisibility() == View.VISIBLE)
                    _v = View.GONE;
                ll_wheel.setVisibility(_v);
                tv_change_address.setVisibility(_v);
                break;
            }
            case R.id.rl_nickname: {
                startActivity(new Intent(UserDetailActivity.this, ChangeUserinfoActivity.class));
                break;
            }
            case R.id.rl_headimg: {
                type = 2;
                AppUtils.uploadHeadImage(this, tempFile);
                break;
            }
        }
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            com.kunleen.sn.sportnewsapplication.utils.XmlParserHandler handler = new com.kunleen.sn.sportnewsapplication.utils.XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
//                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
//                    mCurrentDistrictName = districtList.get(0).getName();
//                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUtils.putString("userInfo", new Gson().toJson(SNApplication.userInfo));
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        }

        tv_address.setText(mCurrentProviceName + " " + mCurrentCityName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case AppUtils.REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    AppUtils.gotoClipActivity(this, Uri.fromFile(tempFile), type);
                }
                break;
            case AppUtils.REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    AppUtils.gotoClipActivity(this, uri, type);
                }
                break;
            case AppUtils.REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    this.uri = uri;
                    String cropImagePath = FileUtils.getRealFilePathFromUri(getApplicationContext(), uri);
//                    if (type == 1) {
////                        headImage1.setImageBitmap(bitMap);
//                    } else {
//                    head_icon.setVisibility(View.GONE);
//                    head_icon_new.setVisibility(View.VISIBLE);
//                    head_icon_new.setImageBitmap(bitMap);
                    doModifyHeadImage(cropImagePath);
//                    BitmapDrawable drawable = new BitmapDrawable(bitMap);
//                    head_icon.setBackgroundDrawable(drawable);
//                    head_icon.setImageDrawable(drawable);
//                    }
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                }
                break;
        }
    }

    private void doModifyHeadImage(String pathName) {
        try {
            String _str = FileUtils.encodeBase64File(pathName);
            if (!StringUtils.isEmptyString(_str)) {
                TRequest<ReqChangeImg> request = new TRequest<>();
                ReqChangeImg reqChangeImg = new ReqChangeImg();
                reqChangeImg.setUid(SNApplication.userInfo.getUserInfo().getUid());
                reqChangeImg.setImageContent(_str);
                reqChangeImg.setImageName(SNApplication.userInfo.getUserInfo().getUid() + "img");
                reqChangeImg.setImageType("png");
                request.setParamImg(reqChangeImg, "1107", "1");
                Observable<Respon_Image> tResponse_emptyObservable = RetrofitClient.getInstance().getService(HttpService.class).ChangeImg(request);
                sendRequest(tResponse_emptyObservable, Respon_Image -> {
                    ToastUtils.showToast("头像修改成功！");
                    GlideCacheUtil.LoadImage(UserDetailActivity.this, cir_head, uri, true);
                    SNApplication.userInfo.getUserInfo().getUserInfo().setHeadImage(Respon_Image.getImageUrl());
                }, throwable -> ToastUtils.showToast(throwable.getMessage()));
            }
        } catch (Exception e) {
            AppUtils.hideLoadingDialog();
        }
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("myHeadFile")) {
            tempFile = (File) savedInstanceState.getSerializable("myHeadFile");
        } else {
            tempFile = new File(FileUtils.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

}
