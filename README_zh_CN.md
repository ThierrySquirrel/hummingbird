# hummingbird

Tcp开发工具包

[English](./README.md)

支持功能:
- [x] Tcp网络编程

# Tcp网络编程:
帮助开发者,更方便的网络编程

## Quick Start

```xml
<!--在pom.xml中添加依赖-->
        <dependency>
            <artifactId>hummingbird</artifactId>
            <groupId>com.github.thierrysquirrel</groupId>
            <version>1.0.1.2-RELEASE</version>
        </dependency>
```

# 编码器
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

# 解码器
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

# 服务器接收消息
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

# 启动 StartHummingbirdServer
 ```java
public class StartHummingbirdServer {
    public static void main(String[] args) throws IOException {
        HummingbirdServerInit.init ("127.0.0.1:8080", 4000, 0,
                new HummingbirdDecoderImpl (), new HummingbirdEncoderImpl (), new HummingbirdHandlerImpl ());
    }
}
 ```

# 客户端接收消息
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

# 启动 StartHummingbirdClient
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

# 延伸

# HTTP

# HTTP服务器接收消息
 ```java
@Slf4j
public class HttpServerHeader implements HummingbirdHandler<HttpRequestContext> {

    @Override
    public void channelMessage(SocketChannelFacade<HttpRequestContext> socketChannelFacade, HttpRequestContext message) {
        log.info (message.toString ());
        HttpResponse httpResponse = HttpResponseBuilder.builderHttpResponse (HttpEditionConstant.DEFAULT_EDITION, HttpStatusCodeConstant.SUCCESS, HttpStatusConstant.OK);
        HttpRequestContext httpRequestContext = new HttpRequestContext ();
        httpRequestContext.setHttpResponse (httpResponse);
        Map<String, String> httpHeader = new HashMap<> ();
        byte[] bodyBytes = "Hello World".getBytes ();
        httpHeader.put (HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.TEXT_PLAIN);
        httpHeader.put (HttpHeaderKeyConstant.CONTENT_LENGTH, bodyBytes.length + "");
        httpHeader.put (HttpHeaderKeyConstant.CONNECTION, HttpHeaderValueConstant.KEEP_ALIVE);
        httpRequestContext.setHttpHeader (httpHeader);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect (bodyBytes.length);
        byteBuffer.put (bodyBytes);
        byteBuffer.flip ();
        httpRequestContext.setHttpBody (byteBuffer);
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

# 启动 HttpServer
 ```java
public class HttpServer {
    public static void main(String[] args) throws Exception{
        HummingbirdServerInit.init ("127.0.0.1:8080",4000,0
                ,new HttpServerDecoder (),new HttpServerEncoder (),new HttpServerHeader ());
    }
}
 ```

# HTTP客户端接收消息
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

# 启动 HttpClient
 ```java
@Data
public class HttpClient {
    public static final ExecutorService clientThreadPool = Executors.newFixedThreadPool (16);
    private CompletableFuture<HttpRequestContext> call = new CompletableFuture<> ();

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        HttpClient httpClient = new HttpClient ();

        HummingbirdClientInit<HttpRequestContext> httpClientInit = HummingbirdClientInitBuilder.builderHummingbirdClientInit ((ThreadPoolExecutor) clientThreadPool, "127.0.0.1:8080", 4000,
                0, new HttpClientDecoder (), new HttpClientEncoder (), new HttpClientHeader (httpClient));
        SocketChannelFacade<HttpRequestContext> connect = httpClientInit.connect ();

        HttpRequestContext httpRequestContext = new HttpRequestContext ();
        HttpRequest httpRequest = HttpRequestBuilder.builderHttpRequest (HttpMethodConstant.POST, "/hello", HttpEditionConstant.DEFAULT_EDITION);
        httpRequestContext.setHttpRequest (httpRequest);

        Map<String, String> httpHandler = Maps.newConcurrentMap ();
        byte[] bodyBytes = "Hello World".getBytes ();
        httpHandler.put (HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.TEXT_PLAIN);
        httpHandler.put (HttpHeaderKeyConstant.CONTENT_LENGTH, bodyBytes.length + "");
        httpHandler.put (HttpHeaderKeyConstant.CONNECTION, HttpHeaderValueConstant.KEEP_ALIVE);
        httpRequestContext.setHttpHeader (httpHandler);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect (bodyBytes.length);
        byteBuffer.put (bodyBytes);
        byteBuffer.flip ();
        httpRequestContext.setHttpBody (byteBuffer);

        connect.sendMessage (httpRequestContext);
        HttpRequestContext callHttpRequestContext = httpClient.getCall ().get ();
        System.out.println (callHttpRequestContext);
    }
}
 ```
