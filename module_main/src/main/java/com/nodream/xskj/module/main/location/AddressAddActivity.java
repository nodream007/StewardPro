package com.nodream.xskj.module.main.location;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.base.BasePresenter;
import com.nodream.xskj.commonlib.utils.SoftKeyboardUtil;
import com.nodream.xskj.commonlib.utils.ToastUtil;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.location.model.CityBean;
import com.nodream.xskj.module.main.location.model.DistrictBean;
import com.nodream.xskj.module.main.location.model.ProvinceBean;
import com.nodream.xskj.module.main.location.model.StreetBean;
import com.nodream.xskj.module.main.work.AddWorkContract;
import com.nodream.xskj.module.main.work.presenter.WorkDetailPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Route(path = "/main/addressaddactivity")
public class AddressAddActivity extends BaseActivity<AddWorkContract.View, WorkDetailPresenter>  {

    private SimpleToolbar mSimpleToolbar;

    private RelativeLayout mAddressRegionsRl;
    private TextView mAddressRegions;
    private RelativeLayout mAddressStreetRl;
    private TextView mAddressStreet;
    private EditText mAddressDetail;


    private OptionsPickerView pvOptions;

    private OptionsPickerView streetPickerView;


    private ProvinceBean provinceBean;
    private List<StreetBean> streetBeans;
    //  省份
    ArrayList<String> provinceList = new ArrayList<>();
    //  城市
    ArrayList<String> cities;
    ArrayList<List<String>> cityList = new ArrayList<>();
    //  区/县
    ArrayList<String> district;
    ArrayList<List<String>> districts;
    ArrayList<List<List<String>>> districtList = new ArrayList<>();


    //  街道
    ArrayList<String> streetList = new ArrayList<>();

    private int provinceId = -1;
    private String provinceName;

    private int cityId = -1;
    private String cityName;

    private int districtId = -1;
    private String districtName;

    private int streetId = -1;
    private String streetName;

    private String serveAddress;
    private String serveAddressDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        initJsonData();
        initView();
        //初始化Picker
        showPicker();
        initStreetPicker();
        SoftKeyboardUtil.hideSoftKeyboard(this);
    }

    @Override
    protected WorkDetailPresenter createPresenter() {
        return new WorkDetailPresenter();
    }

    private void initView() {
        mSimpleToolbar = findViewById(R.id.simple_toolbar);
        mSimpleToolbar.setMainTitle("添加服务地址");
        mSimpleToolbar.setLeftImgVisible();
        mSimpleToolbar.setLeftImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressAddActivity.this.finish();
            }
        });
        mSimpleToolbar.setRightTitleText("完成");
        mSimpleToolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serveAddressDetail = mAddressDetail.getText().toString();
                serveAddress = provinceName + cityName + districtName + serveAddressDetail;
                callBackData();
            }
        });
        mAddressRegionsRl = findViewById(R.id.address_add_regions_rl);
        mAddressRegionsRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftKeyboardUtil.hideSoftKeyboard(AddressAddActivity.this);
                //显示Picker
                pvOptions.show();
            }
        });
        mAddressRegions = findViewById(R.id.address_add_regions);

        mAddressStreetRl = findViewById(R.id.address_add_street_rl);
        mAddressStreetRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftKeyboardUtil.hideSoftKeyboard(AddressAddActivity.this);
                if (streetList.size() == 0) {
                    ToastUtil.showToast(AddressAddActivity.this, "请先选择区域！");
                    return;
                }
                streetPickerView.show();
            }
        });

        mAddressStreet = findViewById(R.id.address_add_street);

        mAddressDetail = findViewById(R.id.address_detail_et);
    }

    private void showPicker() {
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                provinceName = provinceList.get(options1);
                cityName = cityList.get(options1).get(options2);
                districtName = districtList.get(options1).get(options2).get(options3);

                String address; //  如果是直辖市或者特别行政区只设置市和区/县
                if ("北京市".equals(provinceName) || "上海市".equals(provinceName)
                        || "天津市".equals(provinceName) || "重庆市".equals(provinceName)
                        || "澳门".equals(provinceName) || "香港".equals(provinceName)) {
                    address = provinceName + "-" + districtName;
                } else {
                    address = provinceName + "-" + cityName + "-" + districtName;
                }
                mAddressRegions.setText(address);
//                serveAddress = address;
                provinceId = provinceBean.getId();
                cityId = provinceBean.getRegions().get(options2).getId();
                districtId = provinceBean.getRegions().get(options2).getRegions()
                        .get(options3).getId();
                streetBeans = provinceBean.getRegions().get(options2).getRegions()
                        .get(options3).getRegions();
                parseStreetData(streetBeans);
                mAddressStreet.setText("街道");
//                mAddressRegions.setText(address + districtId);
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {

            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setDividerColor(Color.BLUE)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setContentTextSize(20)
//                .setSubCalSize(18)//确定和取消文字大小
//                .setTitleSize(20)//标题文字大小
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(R.color.white)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
//                .isDialog(true)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setDecorView((ViewGroup) getWindow().getDecorView()
                        .findViewById(android.R.id.content))//解决虚拟键挡住问题
                .build();

        pvOptions.setPicker(provinceList, cityList, districtList);//添加数据源

    }

    //初始化爱好选择器
    private void initStreetPicker() {
        streetPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                streetName = streetList.get(options1);
                streetId = streetBeans.get(options1).getId();
                mAddressStreet.setText(streetName);
            }
        })
                .setTitleText("选择街道")//标题文字
                .setTitleSize(20)//标题文字大小
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setLineSpacingMultiplier(1.8f)//行间距
                .setSelectOptions(0)//设置选择的值
                .setDividerColor(Color.BLUE)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setDecorView((ViewGroup) getWindow().getDecorView()
                        .findViewById(android.R.id.content))//解决虚拟键挡住问题
                .build();

//        streetPickerView.setPicker(streetList);//添加数据
    }
    private void initJsonData() {
        //解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */

        String JsonData = getJson("33.json");//获取assets目录下的json文件数据
        Gson gson = new Gson();
        provinceBean = gson.fromJson(JsonData, ProvinceBean.class);
        parseJson(provinceBean);
    }
    /**
     * 从asset目录下读取fileName文件内容
     *
     * @param fileName 待读取asset下的文件名
     * @return 得到省市县的String
     */
    private String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = this.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void parseJson(ProvinceBean provinceBean) {
        //  获取省份名称放入集合
        provinceList.add(provinceBean.getName());
        cities = new ArrayList<>();
        //   声明存放城市的集合
        districts = new ArrayList<>();
        for (CityBean cityBean : provinceBean.getRegions()) {
            cities.add(cityBean.getName());
            district = new ArrayList<>();
            for (DistrictBean districtBean : cityBean.getRegions()) {
                district.add(districtBean.getName());
            }
            //  将区县的集合放入集合
            districts.add(district);
        }
        //  将存放区县集合的集合放入集合
        districtList.add(districts);
        //  将存放城市的集合放入集合
        cityList.add(cities);
    }

    private void parseStreetData(List<StreetBean> streetBeans) {
        streetList.clear();
        for (StreetBean streetBean : streetBeans) {
            streetList.add(streetBean.getName());
        }
        streetPickerView.setPicker(streetList);//添加数据
    }

    private void callBackData() {
        if (provinceId == -1 || cityId == -1 || districtId == -1) {
            ToastUtil.showToast(AddressAddActivity.this, "请选择区域！");
            return;
        } else if (streetId == -1) {
            ToastUtil.showToast(AddressAddActivity.this, "请选择街道！");
            return;
        } else if (serveAddressDetail.equals("")) {
            ToastUtil.showToast(AddressAddActivity.this, "请填写详情地址！");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("provinceId", provinceId);
        intent.putExtra("provinceName", provinceName);
        intent.putExtra("cityId", cityId);
        intent.putExtra("cityName", cityName);
        intent.putExtra("districtId", districtId);
        intent.putExtra("districtName", districtName);
        intent.putExtra("streetId", streetId);
        intent.putExtra("streetName", streetName);
        intent.putExtra("serveAddress", serveAddress);
        setResult(1, intent);
        finish();
    }

}
