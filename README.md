# Social Network Challenge
***


## REST API:
```
/signup                                              POST
/signin                                              POST

/profile/{email}                                     GET, PUT
/profile/{email}/posts                               GET, POST
/profile/{email}/posts/{postId}                      GET, PUT
/profile/{email}/posts/{postId}/comments             GET, POST
/profile/{email}/posts/{postId}/comments/{commentId} GET, PUT

/feed                                                GET
/feed/{postId}                                       GET
/feed/{postId}/comments                              GET
/feed/{postId}/comments/{commentId}                  GET
```