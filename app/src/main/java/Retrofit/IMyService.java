package Retrofit;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.DELETE;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IMyService {

    //Login Activity
    @POST("login")
    @FormUrlEncoded
    Observable<Response<String>> loginUser(@Field("email") String email, @Field("password") String actToken);

    //Register Activity
    @POST("register")
    @FormUrlEncoded
    Observable<Response<String>> registerUser(@Field("name") String name,
                                              @Field("email") String email,
                                              @Field("password") String password,
                                              @Field("phone") String phone,
                                              @Field("address") String address,
                                              @Field("description") String descript,
                                              @Field("gender") String gender);

    //Active Account Activity
    @POST("active-account")
    @FormUrlEncoded
    Observable<String>  activeAccUser(@Field("email") String email,
                                      @Field("activeToken") String actToken);

    //UserInfo Activity
    @PUT("change-profile")
    @FormUrlEncoded
    Observable<Response<String>>  changeProfile(@Field("name") String oldPass,
                                                @Field("phone") String phone,
                                                @Field("address") String address,
                                                @Field("description") String description,
                                                @Field("gender") String gender,
                                                @Header("auth-token") String authToken);

    //UserAvatar Activity
    @Multipart
    @PUT("change-avatar")
    Observable<Response<String>>  changeAva(@Part MultipartBody.Part file,
                                            @Header("auth-token") String authToken);

    //UserPasswordChange Activity
    @PUT("change-password")
    @FormUrlEncoded
    Observable<Response<String>>  changePass(@Field("oldpassword") String oldPass,
                                             @Field("newpassword") String newPass,
                                             @Header("auth-token") String authToken);

    //Account Fragment
    @GET("logout")
    Observable<String>  userLogout(@Header("auth-token") String authToken);

    //FeatureFragment - Week 3

    @GET("category/get-all-category")
    Observable<String>  getAllCategory();
    @GET("course/get-all")
    Observable<String>  getAllCourse();
    @GET("course/get-free")
    Observable<String>  getFreeCourse();
    @GET("course/get-top")
    Observable<String>  getTopCourse();

    //SearchFragment - Week 4
    @GET
    Observable<String>  getCourseByCategory(@Url String urlGet);


    //Course Detail - Week 4
    @GET
    Observable<String>  getListComment(@Url String urlGet);

    @POST("join/create-join")
    @FormUrlEncoded
    Observable<Response<String>>  joinCourse( @Field("idUser") String name,
                                              @Field("idCourse") String goal);

    @POST("rate/create-rate")
    Observable<String>  postRating(@Body RequestBody body);

    @GET
    Observable<String>  getJoinedCourse(@Url String urlGet);


    //Payment - Week 5
    @POST("payment/pay")
    Observable<String> pay (@Body RequestBody body);

//    @Multipart
//    @POST("/course/create")
//    Observable<String> createCourse( @Query("name") String name,
//                                     @Query("goal") String goal,
//                                     @Query("description") String description,
//                                     @Query("category") String category,
//                                     @Query("price") int price,
//                                     @Query("discount") int discount,
//                                     @Part MultipartBody.Part file,
//                                     @Header("auth-token") String authToken);
    @Multipart
    @POST("course/create")
    Observable<String>  createCourse(@Part("name") String name,
                                     @Part("goal") String goal,
                                     @Part("description") String mota,
                                     @Part("category") String category,
                                     @Part("price") String price,
                                     @Part("discount") String discount,
                                     @Part MultipartBody.Part file,
                                     @Header("auth-token") String authToken
    );

    @Multipart
    @POST("lesson/create-lesson")
    Observable<String>  createLession(@Part("title") String name,
                                      @Part("order") String order,
                                      @Part("multipleChoices") String quiz,
                                      @Part("idCourse") String idCourse,
                                      @Part MultipartBody.Part fileVideos,
                                      @Part MultipartBody.Part fileDocs,
                                      @Header("auth-token") String authToken
    );

    @GET
    Observable<String>  getVideo(@Url String urlGet,
                                 @Header("auth-token") String authToken
    );

    // lay khoa hoc da tao
    @GET
    Observable<String>  getCourseCreated(@Url String urlGet);

    // getLessionByIDCourse
    @GET
    Observable<String>  getLessionByID(@Url String urlGet,
                                       @Header("auth-token") String authToken

    );

    //Forgot password
    @POST("forgot-password")
    @FormUrlEncoded
    Observable<String>  ForgotPassword(@Field("email") String email);

    //reset password
    @POST("reset-password")
    @FormUrlEncoded
    Observable<String>  resetPassword(@Field("email") String email,
                                      @Field("token") String token,
                                      @Field("password") String password);

    //MultiChoice
    @GET
    Observable<String>  getChoice(@Url String urlGet,
                                  @Header("auth-token") String authToken
    );


    //getCommentLession
    @GET
    Observable<String>  getCommentLession(@Url String urlGet);

    //Delete created course
    @DELETE
    Observable<String>  DeleteCourse(@Url String urlGet,
                                     @Header("auth-token") String authToken

    );

    //Update Course
    @Multipart
    @PUT
    Observable<String>  updateCourse(
            @Url String urlGet,
            @Part("name") String name,
            @Part("goal") String goal,
            @Part("description") String mota,
            @Part("category") String category,
            @Part("price") String price,
            @Part("discount") String discount,
            @Part MultipartBody.Part file,
            @Header("auth-token") String authToken
    );
    //UpdateMultiChoice
    @PUT
    Observable<String> updateMultipleChoice (@Url String urlGet,
                                             @Body RequestBody body,
                                             @Header("auth-token") String authToken);

    @POST("lesson/upload-image-multiple-choice")
    @Multipart
    Observable<String>  popUpImage(@Part MultipartBody.Part file,
                                   @Header("auth-token") String authToken);

    //cmtLession
    @POST("/comment/add-comment")
    @Multipart
    Observable<String>  addComment(
            @Part  MultipartBody.Part image,
            @Part("idParent") String idParent,
            @Part("idCourse") String idCourse,
            @Part("content") String content,
            @Part("idUser") String idUser,
            @Part("idLesson") String idLesson
    );

    //UpdateLession
    @PUT
    @FormUrlEncoded
    Observable<String>  updateLessonInfo(@Url String urlGet,
                                         @Field("idCourse") String idcourse,
                                         @Field("order") String order,
                                         @Field("title") String title,
                                         @Header("auth-token") String authToken);

    @Multipart
    @PUT
    Observable<String>  addUpdateLessionFile(@Url String urlGet,@Part MultipartBody.Part file,

                                      @Header("auth-token") String authToken);

    //
    @POST("lesson/submit-test")
    Observable<String> submitTest (@Body RequestBody body,
                                   @Header("auth-token") String authToken);


}
