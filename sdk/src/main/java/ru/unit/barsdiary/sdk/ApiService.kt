package ru.unit.barsdiary.sdk

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import ru.unit.barsdiary.sdk.response.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("login_login") login: String,
        @Field("login_password") password: String,
    ): WebLoginResponseDTO

    @GET("{redirect}")
    suspend fun redirect(@Path("redirect") redirect: String): Response<Unit>

    @GET("auth/force-logout")
    suspend fun logout()

    @GET("{url}?no-input=")
    suspend fun noinput(
        @Path("url") url: String,
    )


    @GET("api/ProfileService/GetPersonData")
    suspend fun getPersonData(): GetPersonDataResponseDTO

    @FormUrlEncoded
    @POST("api/ProfileService/SetChild")
    suspend fun setChild(
        @Field("selected") selected: Int,
    )

    @GET("api/ScheduleService/GetDiary")
    suspend fun getDiary(
        @Query("date") date: String,
        @Query("is_diary") isDiary: Boolean,
    ): GetDiaryResponseDTO

    @GET("api/HomeworkService/GetHomeworkFromRange")
    suspend fun getHomework(
        @Query("date") date: String,
    ): GetHomeworkFromRangeResponseDTO

    @GET("api/MarkService/GetVisualizationData")
    suspend fun getVisualizationData(): GetVisualizationDataResponseDTO

    @GET("{chartUrl}")
    suspend fun getChart(
        @Path("chartUrl") chartUrl: String,
        @Query("date_begin") dateBegin: String,
        @Query("date_end") dateEnd: String,
        @Query("subject") subject: Int,
        @QueryMap studentId: Map<String, String>,
    ): ResponseBody

    @GET("api/MarkService/GetSummaryMarks")
    suspend fun getSummaryMarks(
        @Query("date") date: String,
    ): GetSummaryMarksResponseDTO

    @GET("api/MarkService/GetTotalMarks")
    suspend fun getTotalMarks(): GetTotalMarksResponseDTO

    @GET("api/SchoolService/getSchoolInfo")
    suspend fun getSchoolInfo(): GetSchoolInfoResponseDTO

    @GET("api/SchoolService/getClassYearInfo")
    suspend fun getClassYearInfo(): GetClassYearInfoResponseDTO

    @GET("api/WidgetService/getBirthdays")
    suspend fun getBirthdays(): GetBirthdaysResponseDTO

    @GET("api/MailBoxService/getInBoxMessages")
    suspend fun getInBoxMessages(
        @Query("page") page: Int,
    ): GetBoxMessagesResponseDTO

    @GET("api/MailBoxService/getOutBoxMessages")
    suspend fun getOutBoxMessages(
        @Query("page") page: Int,
    ): GetBoxMessagesResponseDTO

    @GET("api/MailBoxService/getInBoxCount")
    suspend fun getInBoxCount(): Int

    @FormUrlEncoded
    @POST("api/MailBoxService/markRead")
    suspend fun markRead(
        @Field("message_id") messageId: Int,
    )

    @FormUrlEncoded
    @POST("api/MailBoxService/removeInBoxMessages")
    suspend fun removeInBoxMessages(
        @Field("messages") messages: String,
    ): Boolean

    @FormUrlEncoded
    @POST("api/MailBoxService/removeOutBoxMessages")
    suspend fun removeOutBoxMessages(
        @Field("messages") messages: String,
    ): Boolean

    @GET("api/MailBoxService/get{type}UserProfiles")
    suspend fun getUserProfiles(
        @Path("type") type: String,
        @Query("page") page: Int,
        @Query("search_text") search: String,
    ): GetUserProfilesResponseDTO

    @FormUrlEncoded
    @POST("api/MailBoxService/newMailBoxMessage")
    suspend fun newMailBoxMessage(
        @Field("receivers_ids") receiversIds: String,
        @Field("subject") subject: String,
        @Field("message") message: String,
    ): Boolean

    @FormUrlEncoded
    @POST("api/MailBoxService/newMailBoxMessage")
    suspend fun newMailBoxMessage(
        @Field("receivers_ids") receiversIds: String,
        @Field("subject") subject: String,
        @Field("message") message: String,
        @Field("document_names") documentNames: String,
        @Field("filenames") fileNames: String,
        @Field("documents[]") documents: String,
    ): Boolean

}