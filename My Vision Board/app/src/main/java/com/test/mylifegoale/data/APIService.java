package com.test.mylifegoale.data;

import com.test.mylifegoale.data.model.BucketComponents;
import com.test.mylifegoale.data.model.TodoComponents;

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

    public static class AllTodoListsResponse {
        // List of bucket items
        public ArrayList<TodoComponents> results;
        public String error;
        public AllTodoListsResponse(String error) {
            this.error = error;
        }
    }

    public static class AllTodoListsRequest {
        public final String userID;
        public AllTodoListsRequest(String userID) {
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

    public static class DeleteBucketResponse {
        public String error;
        public DeleteBucketResponse(String error) {
            this.error = error;
        }
    }

    public static class DeleteBucketRequest {
        public final String ID;
        public DeleteBucketRequest(String ID) {
            this.ID = ID;
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
                @Body RegisterRequest newUser
          );

        // Query all bucket items for current user
        @POST("/api/all-buckets")
        Call<AllBucketListsResponse> allBuckets(
                @Body AllBucketListsRequest bucketLists
        );

        // Query all to-do items for current user
        @POST("/api/all-todo")
        Call<AllTodoListsResponse> allTodos(
                @Body AllTodoListsRequest todoLists
        );

        // Add a bucket list item
        @POST("/api/add-bucket")
        Call<AddBucketResponse> addBucket(
                @Body AddBucketRequest newBucketList
        );

        // Delete a bucket list item
        @POST("/api/delete-bucket")
        Call<DeleteBucketResponse> deleteBucketItem(
                @Body DeleteBucketRequest oldBucketList
        );
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