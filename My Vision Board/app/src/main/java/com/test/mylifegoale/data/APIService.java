package com.test.mylifegoale.data;

import com.test.mylifegoale.data.model.BucketComponents;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public final class APIService {

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

    public static class RegisterResponse {
        public final String error;
        public RegisterResponse(String error) {
            this.error = error;
        }
    }

    // Inputs to register API call
    public static class RegisterRequest {
        public final String firstName;
        public final String lastName;
        public final String login;
        public final String email;
        public final String password;
        public RegisterRequest(String firstName, String lastName, String login, String email, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.login = login;
            this.email = email;
            this.password = password;
        }
    }

    public static class AllBucketListsResponse {
        // List of bucket items
        public ArrayList<BucketComponents> results;
        public String error;
        public AllBucketListsResponse(String error) {
            this.error = error;
        }
    }

    public static class AllBucketListsRequest {
        public final String userID;
        public AllBucketListsRequest(String userID) {
            this.userID = userID;
        }
    }

    public static class AddBucketResponse {
        public String error;
        public AddBucketResponse(String error) {
            this.error = error;
        }
    }

    public static class AddBucketRequest {
        public final String userID;
        public final String itemTitle;
        public final String caption;
        public AddBucketRequest(String userID, String itemTitle, String caption) {
            this.userID = userID;
            this.itemTitle = itemTitle;
            this.caption = caption;
        }
    }

    public interface API {
        // Login with credentials
        @POST("/api/login")
        Call<LoginResponse> login(
                @Body LoginRequest user
        );

        // Register new account
        @POST("/api/register")
        Call<LoginResponse> register(
                @Body RegisterRequest firstName,
                @Body RegisterRequest lastName,
                @Body RegisterRequest email,
                @Body RegisterRequest password
          );

        // Query all bucket items for current user
        @POST("/api/all-buckets")
        Call<AllBucketListsResponse> allBuckets(
                @Body AllBucketListsRequest bucketLists
        );

        // Add a bucket list item
        @POST("/api/add-bucket")
        Call<AddBucketResponse> addBucket(
                @Body AddBucketRequest userID,
                @Body AddBucketRequest itemTitle,
                @Body AddBucketRequest caption
        );
//
//        @POST("/api/fr-request")
//        Call<LoginResponse> performFriendRequest(
//                @Path("senderId") String senderId,
//                @Path("login") String username
//        );
//
//        @POST("/api/fr-response")
//        Call<LoginResponse> allowFriendRequest(
//                @Path("userID") String userID,
//                @Path("friendID") String friendID,
//                @Path("status")String status
//        );
//
//        @POST("/api/fr-remove")
//        Call<LoginResponse> removeFriend(
//                @Path("userId") String userId,
//                @Path("friendId") String friendId
//        );
//
//        @POST("/api/fr-allfriends")
//        Call<LoginResponse> showAllFriends(
//                @Path("userID") String userID
//        );
//
//        @POST("/api/add-bucket")
//        Call<LoginResponse> addItemBucket(
//                @Path("userID") String userID,
//                @Path("itemTitle") String itemTitle,
//                @Path("caption") String caption
//        );
//
//        @POST("/api/all-buckets")
//        Call<LoginResponse> showAllbucket(
//                @Path("userID") String userID
//        );
//
//        @POST("/api/delete-bucket")
//        Call<LoginResponse> deleteItemBucket(
//                @Path("ID") String ID
//        );
//
//        @POST("/api/edit-bucket")
//        Call<LoginResponse> editItemBucket(
//                @Path("ID") String ID,
//                @Path("itemTitle") String ItemTile,
//                @Path("caption") String caption,
//                @Path("completed") String completed
//        );
//
//        @POST("/api/all-todo")
//        Call<LoginResponse> showAllTodo(
//                @Path("userID") String userID
//        );
//
//        @POST("/api/delete-todo")
//        Call<LoginResponse> deleteItemTodo(
//                @Path("ID") String ID
//        );
//
//        @POST("/api/edit-todo")
//        Call<LoginResponse> editItemTodo(
//                @Path("ID") String ID,
//                @Path("itemTitle") String itemTitle,
//                @Path("completed") String completed
//        );
//
//        @POST("/api/mark-completed-todo")
//        Call<LoginResponse> completedTodo(
//                @Path("itemTitle") String itemTitle,
//                @Path("userID") String userID,
//                @Path("completed") String completed
//        );
    }
}