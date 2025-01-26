# hummingbird

TCP Development Kit

[中文](./README_zh_CN.md)

Support Function:
- [x] TCP Network Programming

# TCP Network Programming:
Help Developers, More Convenient Network Programming

## Quick Start
```xml
<!--Adding dependencies to pom. XML-->
        <dependency>
            <artifactId>hummingbird</artifactId>
            <groupId>com.github.thierrysquirrel</groupId>
            <version>1.1.0.0-RELEASE</version>
        </dependency>
```

# Encoder
```java
@Data
public class User {
    private int age;
    private String name;
    private int dataLength;
    private ByteBuffer data;
}

public class HummingbirdEncoderImpl implements HummingbirdEncoder<User> {
    @Override
    public void encoder(User message, ByteBufferFacade byteBufferFacade) {
        ByteBuffer data = message.getData ();
        message.setData (null);
        byte[] serialize = SerializerFactory.serialize (message);
        byteBufferFacade.putInt (serialize.length);
        byteBufferFacade.putBytes (serialize);
        if (data != null) {
            byteBufferFacade.put (data);
        }
    }
}
```

# Decoder
```java
public class HummingbirdDecoderImpl implements HummingbirdDecoder<User> {
    @Override
    public User decoder(ByteBufferFacade byteBufferFacade, SocketChannelFacade<User> socketChannelFacade) {
        User messageDecoderCache = socketChannelFacade.getMessageDecoderCache ();
        if (messageDecoderCache != null) {
            ByteBuffer data = messageDecoderCache.getData ();
            boolean tryGet = byteBufferFacade.tryGet (data);
            if (tryGet) {
                data.flip ();
                socketChannelFacade.removeMessageDecoderCache ();
                return messageDecoderCache;
            }
            return null;
        }

        if (byteBufferFacade.length () < 4) {
            byteBufferFacade.reset ();
            return null;
        }

        int userLength = byteBufferFacade.getInt ();
        if (byteBufferFacade.length () < userLength) {
            byteBufferFacade.reset ();
            return null;
        }

        byte[] userBytes = new byte[userLength];
        byteBufferFacade.getBytes (userBytes);
        User user = SerializerFactory.deSerialize (userBytes, User.class);
        int dataLength = user.getDataLength ();
        if (dataLength < 1) {
            return user;
        }

        if (user.getData () == null) {
            ByteBuffer data = ByteBuffer.allocateDirect (dataLength);
            user.setData (data);
        }
        if (byteBufferFacade.length () < dataLength) {
            socketChannelFacade.putMessageDecoderCache (user);
            ByteBuffer data = user.getData ();
            byteBufferFacade.tryGet (data);
            return null;
        }
        ByteBuffer data = user.getData ();
        byteBufferFacade.tryGet (data);
        data.flip ();
        return user;
    }
}
```

# The Server Receives Messages
```java
@Slf4j
public class HummingbirdHandlerImpl implements HummingbirdHandler<User> {
    @Override
    public void channelMessage(SocketChannelFacade<User> socketChannelFacade, User message) {
        log.info (message.toString ());
        try {
            socketChannelFacade.sendMessage (message);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void channelTimeout(SocketChannelFacade<User> socketChannelFacade) {
        log.info ("timeout");
        socketChannelFacade.close ();
    }

    @Override
    public void channelClose(SocketAddress remoteAddress, SocketAddress localAddress) {
        log.info ("channelClose remoteAddress:{} localAddress:{}", remoteAddress, localAddress);
    }
}
```

# Start StartHummingbirdServer
 ```java
public class StartHummingbirdServer {
    public static void main(String[] args) throws IOException {
        HummingbirdServerInit.init ("127.0.0.1:8080", 4000, 0,
                new HummingbirdDecoderImpl (), new HummingbirdEncoderImpl (), new HummingbirdHandlerImpl ());
    }
}
 ```

# Client Receives Message
```java
@Slf4j
@Data
public class HummingbirdClientHandlerImpl implements HummingbirdHandler<User> {
    private StartHummingbirdClient startHummingbirdClient;

    public HummingbirdClientHandlerImpl(StartHummingbirdClient startHummingbirdClient) {
        this.startHummingbirdClient = startHummingbirdClient;
    }

    @Override
    public void channelMessage(SocketChannelFacade<User> socketChannelFacade, User message) {
        startHummingbirdClient.getCall ().complete (message);
    }

    @Override
    public void channelTimeout(SocketChannelFacade<User> socketChannelFacade) {
        log.info ("timeout");
        socketChannelFacade.close ();
    }

    @Override
    public void channelClose(SocketAddress remoteAddress, SocketAddress localAddress) {
        log.info ("channelClose remoteAddress:{} localAddress:{}", remoteAddress, localAddress);
    }
}
```

# Start StartHummingbirdClient
 ```java
@Data
public class StartHummingbirdClient {
    public static final ExecutorService clientThreadPool = Executors.newFixedThreadPool (16);
    private CompletableFuture<User> call = new CompletableFuture<> ();

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        StartHummingbirdClient startHummingbirdClient = new StartHummingbirdClient ();

        HummingbirdClientInit<User> userHummingbirdClientInit = HummingbirdClientInitBuilder.builderHummingbirdClientInit ((ThreadPoolExecutor) clientThreadPool, "127.0.0.1:8080", 4000,
                0, new HummingbirdDecoderImpl (), new HummingbirdEncoderImpl (), new HummingbirdClientHandlerImpl (startHummingbirdClient));
        SocketChannelFacade<User> connect = userHummingbirdClientInit.connect ();

        User user = new User ();
        user.setAge (1);
        user.setName ("Hummingbird");
        byte[] hello = "Hello".getBytes ();
        user.setDataLength (hello.length);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect (hello.length);
        byteBuffer.put (hello);
        byteBuffer.flip ();
        user.setData (byteBuffer);
        connect.sendMessage (user);

        User callUser = startHummingbirdClient.getCall ().get ();
        System.out.println (callUser);
        connect.close ();
        if (!connect.isOpen ()) {
            connect = userHummingbirdClientInit.connect ();
        }
        startHummingbirdClient.call = new CompletableFuture<> ();
        User userB = new User ();
        userB.setAge (2);
        userB.setName ("HummingbirdB");
        connect.sendMessage (userB);
        User callUserB = startHummingbirdClient.getCall ().get ();
        System.out.println (callUserB);
    }

}
 ```

# Extend

# HTTP

# HTTP Server Receives Messages
 ```java
@Slf4j
public class HttpServerHeader implements HummingbirdHandler<HttpRequestContext> {

    @Override
    public void channelMessage(SocketChannelFacade<HttpRequestContext> socketChannelFacade, HttpRequestContext message) {
        String httpUri = message.getHttpRequest ().getHttpUri ();
        Map<String, String> urlMap = HttpUrlCoderRootChainFactory.createUrlMap (httpUri);
        log.info (urlMap.toString ());

        Map<String, String> httpHeader = message.getHttpHeader ();
        boolean isFormData = HttpHeaderFactory.equalsIgnoreCaseContentType (httpHeader, HttpHeaderValueConstant.FORM_DATA);
        if(isFormData){
            Map<String, HttpFormData> formDataBody = HttpServerBodyDecoderFactory.builderFormUrlencoded (message);
            for (Map.Entry<String, HttpFormData> dataEntry : formDataBody.entrySet ()) {
                HttpFormData value = dataEntry.getValue ();
                if(value.isFile ()){
                    try {
                        HttpFormDataBodyFactory.writeFile (value,"/cache/"+value.getFileName ());
                    } catch (IOException e) {
                        log.error ("writeFile Error",e);
                    }
                }
            }
        }
        boolean isFormUrlencoded = HttpHeaderFactory.equalsIgnoreCaseContentType (httpHeader, HttpHeaderValueConstant.FORM_URLENCODED);
        if(isFormUrlencoded){
            Map<String, String> formUrlencodedMap = HttpServerBodyDecoderFactory.builderFormUrlencoded (message);
            log.info (formUrlencodedMap.toString ());
        }
        boolean isText = HttpHeaderFactory.equalsIgnoreCaseContentType (httpHeader, HttpHeaderValueConstant.TEXT_PLAIN);
        if(isText){
            String text = HttpServerBodyDecoderFactory.builderText (message);
            log.info (text);
        }

        HttpRequestContext httpRequestContext = HttpRequestContextBuilder.builderTextResponse ("Hello World");
        try {
            socketChannelFacade.sendMessage (httpRequestContext);
        } catch (IOException e) {
            e.printStackTrace ();
        }

    }

    @Override
    public void channelTimeout(SocketChannelFacade<HttpRequestContext> socketChannelFacade) {
        log.info ("timeout");
        socketChannelFacade.close ();
    }

    @Override
    public void channelClose(SocketAddress remoteAddress, SocketAddress localAddress) {
        log.info ("channelClose remoteAddress:{} localAddress:{}", remoteAddress, localAddress);
    }

}
 ```

# Start HttpServer
 ```java
public class HttpServer {
    public static void main(String[] args) throws Exception{
        HummingbirdServerInit.init ("127.0.0.1:8080",4000,0
                ,new HttpServerDecoder (),new HttpServerEncoder (),new HttpServerHeader ());
    }
}
 ```

# HTTP Client Receives Message
 ```java
@Slf4j
@Data
public class HttpClientHeader implements HummingbirdHandler<HttpRequestContext> {
    private HttpClient httpClient;

    public HttpClientHeader(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void channelMessage(SocketChannelFacade<HttpRequestContext> socketChannelFacade, HttpRequestContext message) {
        httpClient.getCall ().complete (message);
    }

    @Override
    public void channelTimeout(SocketChannelFacade<HttpRequestContext> socketChannelFacade) {
        log.info ("timeout");
        socketChannelFacade.close ();
    }

    @Override
    public void channelClose(SocketAddress remoteAddress, SocketAddress localAddress) {
        log.info ("channelClose remoteAddress:{} localAddress:{}", remoteAddress, localAddress);
    }
}
 ```

# Start HttpClient
 ```java
@Slf4j
@Data
public class HttpClient {
    public static final ExecutorService clientThreadPool = Executors.newFixedThreadPool (16);
    private CompletableFuture<HttpRequestContext> call = new CompletableFuture<> ();

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        HttpClient httpClient = new HttpClient ();

        HummingbirdClientInit<HttpRequestContext> httpClientInit = HummingbirdClientInitBuilder.builderHummingbirdClientInit ((ThreadPoolExecutor) clientThreadPool, "127.0.0.1:8080", 4000,
                0, new HttpClientDecoder (), new HttpClientEncoder (), new HttpClientHeader (httpClient));
        SocketChannelFacade<HttpRequestContext> connect = httpClientInit.connect ();

        String uri = HttpUrlCoderRootChainFactory.createUrl ("/user", "hello", "world")
                .putUrl ("id", "123456").builder ();

        HttpRequestContext httpRequestContext = HttpRequestContextBuilder.builderRequest (HttpMethodConstant.POST,uri);

        boolean isFormData=Boolean.FALSE;
        if(isFormData){
            HttpClientBodyEncoderRootChainFactory.createFormData (httpRequestContext)
                    .putText ("hello","world")
                    .putFile ("file","filePath",HttpHeaderValueConstant.FORM_DATA)
                    .builder ();
        }
        boolean isFormUrlencoded=Boolean.TRUE;
        if(isFormUrlencoded){
            HttpClientBodyEncoderRootChainFactory.createFormUrlencoded (httpRequestContext,"hello","world")
                    .putText ("id","654321")
                    .builder ();
        }

        boolean isText=Boolean.FALSE;
        if(isText){
            HttpClientBodyEncoderRootChainFactory.createText (httpRequestContext,"hello world");
        }

        connect.sendMessage (httpRequestContext);
        HttpRequestContext callHttpRequestContext = httpClient.getCall ().get ();
        log.info (callHttpRequestContext.toString ());
    }
}
 ```
 
