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

    public static class LoginResponse {
        public final String id;
        public final String firstName;
        public final String lastName;
        public final String email;
        public final Boolean isVerified;
        public final JWTToken jwt;
        public final String error;

        public LoginResponse(String id, String firstName, String lastName, String email, Boolean isVerified, JWTToken jwt, String error) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
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
        Call<LoginResponse> register(
//                @Body("firstName") String firstName,
//                @Body("lastName") String lastName,
//                @Body("email") String email,
//                @Body("password") String password
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
    }
}