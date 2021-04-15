package com.test.mylifegoale.data;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public final class APIService {

    public static class Urgh {
        public final String test;

        public Urgh(String test) {
            this.test = test;
        }
    }

    /**
     * {
     *   "id": "605faf43596b7242de2c8c39",
     *   "firstName": "Trevor",
     *   "lastName": "Jones",
     *   "email": "trev@mail.com",
     *   "isVerified": true,
     *   "jwt": {
     *     "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySUQiOiI2MDVmYWY0MzU5NmI3MjQyZGUyYzhjMzkiLCJmaXJzdE5hbWUiOiJUcmV2b3IiLCJsYXN0TmFtZSI6IkpvbmVzIiwiaWF0IjoxNjE4NDU5NzExfQ.NjXgbMziIyoQwRRRPiTBQTLjQ6PTDSFS5ik-x5QCKYA"
     *   },
     *   "error": ""
     * }*/
    public static class LoginResponse {
        public final String id;
        public final String firstName;
        public final String email;
        public final Boolean isVerified;
        public final JWTToken jwt;
        public final String error;

        public LoginResponse(String id, String firstName, String email, Boolean isVerified, JWTToken jwt, String error) {
            this.id = id;
            this.firstName = firstName;
            this.email = email;
            this.isVerified = isVerified;
            this.jwt = jwt;
            this.error = error;
        }
    }

    public static class JWTToken {
        public final String accessToken;

        public JWTToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }


    public static class LoginRequest {
        public final String login;
        public final String password;
        public LoginRequest(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }
    public interface API {
        @GET("/test")
        Call<Urgh> test();

        @POST("/api/login")
        Call<LoginResponse> login(
                @Body LoginRequest user
        );

        // @TODO prefix with /api
        @POST("/register")
        // @TODO replace @Path with @Body and make a class for each body
        Call<LoginResponse> register(
                @Path("firstName") String firstName,
                @Path("lastName") String lastName,
                @Path("email") String email,
                @Path("password") String password
        );
//
//        //keep using <LoginResponse> ?
//        @POST("fr-request")
//        Call<LoginResponse> performFriendRequest(
//                @Path("senderId") String senderId,
//                @Path("login") String username
//        );
//
//        @POST("fr-response")
//        Call<LoginResponse> allowFriendRequest(
//                @Path("userID") String userID,
//                @Path("friendID") String friendID,
//                @Path("status")String status
//        );
//
//        @POST("fr-remove")
//        Call<LoginResponse> removeFriend(
//                @Path("userId") String userId,
//                @Path("friendId") String friendId
//        );
//
//        @POST("fr-allfriends")
//        Call<LoginResponse> showAllFriends(
//                @Path("userID") String userID
//        );
//
//        @POST("add-bucket")
//        Call<LoginResponse> addItemBucket(
//                @Path("userID") String userID,
//                @Path("itemTitle") String itemTitle,
//                @Path("caption") String caption
//        );
//
//        @POST("all-buckets")
//        Call<LoginResponse> showAllbucket(
//                @Path("userID") String userID
//        );
//
//        @POST("delete-bucket")
//        Call<LoginResponse> deleteItemBucket(
//                @Path("ID") String ID
//        );
//
//        @POST("edit-bucket")
//        Call<LoginResponse> editItemBucket(
//                @Path("ID") String ID,
//                @Path("itemTitle") String ItemTile,
//                @Path("caption") String caption,
//                @Path("completed") String completed
//        );
//
//        @POST("add-todo")
//        Call<LoginResponse> addItemTodo(
//                @Path("userID") String userID,
//                @Path("itemTitle") String itemTitle
//        );
//
//        @POST("all-todo")
//        Call<LoginResponse> showAllTodo(
//                @Path("userID") String userID
//        );
//
//        @POST("delete-todo")
//        Call<LoginResponse> deleteItemTodo(
//                @Path("ID") String ID
//        );
//
//        @POST("edit-todo")
//        Call<LoginResponse> editItemTodo(
//                @Path("ID") String ID,
//                @Path("itemTitle") String itemTitle,
//                @Path("completed") String completed
//        );
//
//        @POST("mark-completed-todo")
//        Call<LoginResponse> completedTodo(
//                @Path("itemTitle") String itemTitle,
//                @Path("userID") String userID,
//                @Path("completed") String completed
//        );
//
//        @POST("search-bucket")
//        Call<LoginResponse> searchBucket(
//                @Path("userId") String userId,
//                @Path("search") String search
//        );
//
//        // Shouldnt it be UserID?
//        /*@POST("search-todo")
//        @Path("userId") String userId,
//        @Path("search") String search
//		);*/
    }

//    public static void main(String... args) throws IOException {
//        // Create a very simple REST adapter which points the GitHub API.
//        Retrofit retrofit =
//                new Retrofit.Builder()
//                        .baseUrl(API_URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//        // Create an instance of our GitHub API interface.
//        GitHub github = retrofit.create(GitHub.class);
//
//        // Create a call instance for looking up Retrofit contributors.
//        Call<List<Contributor>> call = github.contributors("square", "retrofit");
//
//        // Fetch and print a list of the contributors to the library.
//        List<Contributor> contributors = call.execute().body();
//        for (Contributor contributor : contributors) {
//            System.out.println(contributor.login + " (" + contributor.contributions + ")");
//        }
//    }
}