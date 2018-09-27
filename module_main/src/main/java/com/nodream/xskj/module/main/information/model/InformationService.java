package com.nodream.xskj.module.main.information.model;

import com.nodream.xskj.commonlib.net.BaseResponse;
import com.nodream.xskj.module.main.contact.model.MedicalStaffBean;
import com.nodream.xskj.module.main.work.model.WorkBean;
import com.nodream.xskj.module.main.work.model.WorkResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface InformationService {
    /**
     * 测试类
     */
    @FormUrlEncoded
    @POST
    Observable<BaseResponse<List<PatientBean>>> getPatientList(@Url String url,
                                                  //  @Header("") String authorization,
                                                  @FieldMap Map<String, String> maps);

    @FormUrlEncoded
    @POST
    Observable<BaseResponse<PatientBean>> getPatientDetail(@Url String url,
                                                               //  @Header("") String authorization,
                                                               @FieldMap Map<String, String> maps);
    @FormUrlEncoded
    @POST
    Observable<BaseResponse<List<MedicalStaffBean>>> getContactList(@Url String url,
                                                                    //  @Header("") String authorization,
                                                                    @FieldMap Map<String, String> maps);
    @FormUrlEncoded
    @POST
    Observable<BaseResponse<MedicalStaffBean>> getContactDetail(@Url String url,
                                                                    //  @Header("") String authorization,
                                                                    @FieldMap Map<String, String> maps);

    @FormUrlEncoded
    @POST
    Observable<BaseResponse<String>> submitPatient(@Url String url,
                                                     //  @Header("") String authorization,
                                                     @FieldMap Map<String, String> maps);
}
