package com.example.lab8.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab8.Adapter.Adapter_Item_District_Select_GHN;
import com.example.lab8.Adapter.Adapter_Item_Province_Select_GHN;
import com.example.lab8.Adapter.Adapter_Item_Ward_Select_GHN;
import com.example.lab8.Models.District;
import com.example.lab8.Models.DistrictRequest;
import com.example.lab8.Models.Fruit;
import com.example.lab8.Models.Province;
import com.example.lab8.Models.ResponseGHN;
import com.example.lab8.Models.Ward;
import com.example.lab8.R;
import com.example.lab8.Serives.ApiClient;
import com.example.lab8.Serives.ApiServices;
import com.example.lab8.Serives.GHNItem;
import com.example.lab8.Serives.GHNOrderRequest;
import com.example.lab8.Serives.GHNOrderRespone;
import com.example.lab8.Serives.GHNRequest;
import com.example.lab8.Serives.GHNServices;
import com.example.lab8.Serives.HttpRequest;
import com.example.lab8.Serives.Order;
import com.example.lab8.databinding.ActivityLocationBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    private ActivityLocationBinding binding;
    private TextInputEditText edt_name, edt_phone, edt_location;
    private GHNRequest request;
    private GHNServices ghnServices;
    private String productId, productTypeId, productName, description, WardCode;
    private double rate, price;
    private int image, DistrictID, ProvinceID ;
    private Adapter_Item_Province_Select_GHN adapter_item_province_select_ghn;
    private Adapter_Item_District_Select_GHN adapter_item_district_select_ghn;
    private Adapter_Item_Ward_Select_GHN adapter_item_ward_select_ghn;
    private Button btn_order;
    private HttpRequest httpRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        request = new GHNRequest();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
             productId = bundle.getString("productId");
             productTypeId = bundle.getString("productTypeId");
             productName = bundle.getString("productName");
             description = bundle.getString("description");
             rate = bundle.getDouble("rate");
             price = bundle.getDouble("price");
             image = bundle.getInt("image");
        }

        request.callAPI().getListProvince().enqueue(responseProvince);
        binding.spProvince.setOnItemSelectedListener(onItemSelectedListener);
        binding.spDistrict.setOnItemSelectedListener(onItemSelectedListener);
        binding.spWard.setOnItemSelectedListener(onItemSelectedListener);

        binding.spProvince.setSelection(1);
        binding.spDistrict.setSelection(1);
        binding.spWard.setSelection(1);
        btn_order= findViewById(R.id.btn_order);
        edt_location= findViewById(R.id.edt_location);
        edt_name= findViewById(R.id.edt_name);
        edt_phone= findViewById(R.id.edt_phone);

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WardCode.equals("")) return;
                Fruit fruit= (Fruit) getIntent().getExtras().getSerializable("item");
                GHNItem ghnItem= new GHNItem();
                ghnItem.setName(fruit.getName());
                ghnItem.setPrice(fruit.getPrice());
                ghnItem.setCode(fruit.get_id());
                ghnItem.setQuantity(1);
                ghnItem.setWeight(50);
                ArrayList<GHNItem> items= new ArrayList<>();
                items.add(ghnItem);
                GHNOrderRequest ghnOrderRequest= new GHNOrderRequest(
                    edt_name.getText().toString(),
                    edt_phone.getText().toString(),
                    edt_location.getText().toString(),
                    WardCode,
                    DistrictID,
                    items
                );
                request.callAPI(ghnOrderRequest, responseOrder);

            }
        });


    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.sp_province) {
                ProvinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(ProvinceID);
                request.callAPI().getListDistrict(districtRequest).enqueue(responseDistrict);
            } else if (parent.getId() == R.id.sp_district) {
                DistrictID = ((District) parent.getAdapter().getItem(position)).getDistrictID();
                request.callAPI().getListWard(DistrictID).enqueue(responseWard);
            } else if (parent.getId() == R.id.sp_ward) {
                WardCode = ((Ward) parent.getAdapter().getItem(position)).getWardCode();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Callback<ResponseGHN<ArrayList<Province>>> responseProvince = new Callback<ResponseGHN<ArrayList<Province>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<Province> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinProvince(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lấy dữ liệu bị lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    Callback<ResponseGHN<ArrayList<District>>> responseDistrict = new Callback<ResponseGHN<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<District> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinDistrict(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {

        }
    };

    Callback<ResponseGHN<ArrayList<Ward>>> responseWard = new Callback<ResponseGHN<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){

                    if(response.body().getData() == null)
                        return;

                    ArrayList<Ward> ds = new ArrayList<>(response.body().getData());

                    ds.addAll(response.body().getData());
                    SetDataSpinWard(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    private void SetDataSpinProvince(ArrayList<Province> ds){
        adapter_item_province_select_ghn = new Adapter_Item_Province_Select_GHN(this, ds);
        binding.spProvince.setAdapter(adapter_item_province_select_ghn);
    }

    private void SetDataSpinDistrict(ArrayList<District> ds){
        adapter_item_district_select_ghn = new Adapter_Item_District_Select_GHN(this, ds);
        binding.spDistrict.setAdapter(adapter_item_district_select_ghn);
    }

    private void SetDataSpinWard(ArrayList<Ward> ds){
        adapter_item_ward_select_ghn = new Adapter_Item_Ward_Select_GHN(this, ds);
        binding.spWard.setAdapter(adapter_item_ward_select_ghn );
    }
    
    Callback<ResponseGHN<GHNOrderRespone>> responseOrder= new Callback<ResponseGHN<GHNOrderRespone>>() {
        @Override
        public void onResponse(Call<ResponseGHN<GHNOrderRespone>> call, Response<ResponseGHN<GHNOrderRespone>> response) {
            if (response.isSuccessful()){
                if (response.body().getCode() == 200){
                    Toast.makeText(LocationActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    Order order= new Order();
                    order.setOrder_code(response.body().getData().getOrder_code());
                    order.setId_user(getSharedPreferences("INFO", MODE_PRIVATE).getString("id", ""));
                    httpRequest.callAPI().order(order).enqueue(responOrderDatabase);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<GHNOrderRespone>> call, Throwable t) {

        }
    };

    Callback<Response<Order>> responOrderDatabase= new Callback<Response<Order>>() {
        @Override
        public void onResponse(Call<Response<Order>> call, Response<Response<Order>> response) {
            if (response.isSuccessful()){
                int status= response.body().body().getStatus();
                if (status == 200){
                    Toast.makeText(LocationActivity.this, "Cảm ơn đã mua hàng", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Order>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Không thể lưu đơn hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    public void callAPI(GHNOrderRequest orderRequest, Callback<ResponseGHN<GHNOrderRespone>> callback){
        ApiServices apiService= ApiClient.getClient().create(ApiServices.class);
        Call<ResponseGHN<GHNOrderRespone>> call = apiService.createGHNOrder(orderRequest);

        call.enqueue(callback);
    }

}