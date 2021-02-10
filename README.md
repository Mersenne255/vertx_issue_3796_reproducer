### Description 
Reproducer for vertx_http_server_active_connections negative values issue
https://github.com/eclipse-vertx/vert.x/issues/3796
 
### Steps
1. Run main class that starts vertx server using SSL and HTTP/2.0
2. Run curl command with max-time defined (adjust --max-time parameter as necessary to get the curl error message):
`curl --insecure -XGET 'https://localhost:8080/test' --max-time 0,05`

3. We want to get this curl message (at least several times):
`curl: (28) Operation timed out after 50 milliseconds with 0 bytes received`
4. Chech endpoint https://localhost:8080/metrics
5. Metric `vertx_http_server_active_connections` should have negative values