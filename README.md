# Social Network Challenge
***


## REST API:
```
/signup                                      POST
/login                                       POST

/profile                                     GET            (auth required)
/profile/posts                               GET            (auth required)
/profile/posts/{postId}/comments             GET            (auth required)

/posts                                       GET, POST      (auth required)
/posts/{postId}                              GET, PUT       (auth required)
/posts/{postId}/comments                     GET, POST      (auth required)
/posts/{postId}/comments/{commentId}         GET, PUT       (auth required)
```

## Authentication

Send a POST request to /login with the following body:
```
{ "email": YOUR_EMAIL, "password": YOUR_PASSWORD }
```

Get the token from the response and set the ```authcookie``` with it to call the endpoints which required authentication

## Requests

### Signup
```
{ "email": YOUR_EMAIL, "name": YOUR_NAME, password": YOUR_PASSWORD }
```

### New Post
```
{ "text": POST_TEXT_OR_NULL, "IMAGE": NON_EMPTY_IMAGE }
```

### New Comment
```
{ "text": NON_EMPTY_TEXT }
```

## How To Run

### Setup Database
```
$> cd SocialNetwork/
$> sbt flywayClean
$> sbt flywayMigrate
```

### Run
```
$> sbt run
```
 