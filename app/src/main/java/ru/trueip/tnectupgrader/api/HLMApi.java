package ru.trueip.tnectupgrader.api;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import ru.trueip.tnectupgrader.models.requests.MessageModel;
import ru.trueip.tnectupgrader.models.requests.QuizzesAnswers;
import ru.trueip.tnectupgrader.models.responses.ClaimModel;
import ru.trueip.tnectupgrader.models.responses.TypeModel;
import ru.trueip.tnectupgrader.models.responses.UpdatesModel;
import ru.trueip.tnectupgrader.models.responses.UserModel;

public interface HLMApi {

    @FormUrlEncoded
    @POST("api/activation")
    Observable<Response<UserModel>> activation(@Header("Accept-Language") String locale,
                                               @Field("number") String apartmentNumber,
                                               @Field("activation_code") String activationCode);

    @FormUrlEncoded
    @POST("api/reactivation")
    Observable<Response<UserModel>> reactivation(@Header("Accept-Language") String locale,
                                                 @Field("number") String apartmentNumber,
                                                 @Field("activation_code") String activationCode);


    @GET
    Observable<Response<UserModel>> getObject(@Header("Authorization") String api_token, @Url String endpointUrl);

    @GET
    Observable<Response<ArrayList<UpdatesModel>>> getListUpdates(@Header("Authorization") String api_token, @Url String endpointUrl);

    // option 2: using a dynamic URL
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Header("Authorization") String api_token, @Url String fileUrl);

    @GET
    Observable<Response<ArrayList<TypeModel>>> getClaimTypes(@Header("Authorization") String api_token, @Url String endpointUrl);

    //@FormUrlEncoded
    @POST
    Observable<Response<ClaimModel>> postClaim(@Header("Authorization") String api_token,
                                               @Body HashMap<String, Object> claimJSON,
                                               @Url String endpointUrl);

    @POST("api/logout")
    Observable<Response<Void>> logout(@Header("Authorization") String api_token);

    @POST
    Observable<Response<Void>> postQuizzesAnswers(@Header("Authorization") String api_token,
                                                  @Body QuizzesAnswers quizzesAnswers,
                                                  @Url String endpointUrl);

    @FormUrlEncoded
    @POST
    Observable<Response<Void>> postFeedback(@Header("Authorization") String api_token,
                                            @Field("text") String feedback,
                                            @Url String endpointUrl);

    @POST
    Observable<Response<Void>> postFeedback(@Header("Authorization") String api_token,
                                            @Body MessageModel feedback,
                                            @Url String endpointUrl);


    @GET
    Observable<Response<UserModel>> getSipNumber(@Header("Authorization") String api_token, @Url String endpointUrl);


    //@FormUrlEncoded
    @PUT
    Observable<Response<ClaimModel>> updateClaim(@Header("Authorization") String api_token,
                                                 @Body HashMap<String, Object> claimJSON,
                                                 @Url String endpointUrl);

    //@Multipart
    @POST
    Observable<Response<ClaimModel>> uploadImageFiles(@Header("Authorization") String api_token,
                                                      @Body RequestBody images,
                                                      @Url String endpointUrl);
}
