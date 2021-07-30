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
            <version>1.0.0.0-RELEASE</version>
        </dependency>
```

# 编码器
```java
@Data
public class User {
    private int age;
    private String name;
}

public class HummingbirdEncoderImpl implements HummingbirdEncoder<User> {
    @Override
    public void encoder(User message, ByteBufferFacade byteBufferFacade) {
        byte[] serialize = SerializerFactory.serialize (message);
        byteBufferFacade.putInt (serialize.length);
        byteBufferFacade.putBytes (serialize);
    }
}
```

# 解码器
```java
public class HummingbirdDecoderImpl implements HummingbirdDecoder<User> {
    @Override
    public User decoder(ByteBufferFacade byteBufferFacade) {
        int length = byteBufferFacade.length ();
        if(byteBufferFacade.length ()<4){
            return null;
        }

        byteBufferFacade.make ();
        int messageLength = byteBufferFacade.getInt ();
        if(length<messageLength){
            byteBufferFacade.reset ();
            return null;
        }
        byte[] message=new byte[messageLength];
        byteBufferFacade.getBytes (message);
        return SerializerFactory.deSerialize (message,User.class);
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
    public void channelClose(SocketAddress remoteAddress) {
        log.info ("channelClose"+remoteAddress.toString ());
    }
}
```

# 启动 StartHummingbirdServer
 ```java
public class StartHummingbirdServer {
    public static void main(String[] args) throws IOException {
        HummingbirdServerInit.init ("127.0.0.1:8080",4000,0,
                new HummingbirdDecoderImpl (),new HummingbirdEncoderImpl (),new HummingbirdHandlerImpl ());
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
    public void channelClose(SocketAddress remoteAddress) {
        log.info ("channelClose"+remoteAddress.toString ());
    }
}
```

# 启动 StartHummingbirdClient
 ```java
@Data
public class StartHummingbirdClient {
    public static final ExecutorService clientThreadPool = Executors.newFixedThreadPool (16);
    private CompletableFuture<User> call=new CompletableFuture<> ();
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        StartHummingbirdClient startHummingbirdClient=new StartHummingbirdClient ();

        HummingbirdClientInit<User> userHummingbirdClientInit = HummingbirdClientInitBuilder.builderHummingbirdClientInit ((ThreadPoolExecutor) clientThreadPool, "127.0.0.1:8080", 4000,
                0, new HummingbirdDecoderImpl (), new HummingbirdEncoderImpl (), new HummingbirdClientHandlerImpl (startHummingbirdClient));
        SocketChannelFacade<User> connect = userHummingbirdClientInit.connect ();

        User user=new User ();
        user.setAge (1);
        user.setName ("Hummingbird");
        connect.sendMessage (user);

        User callUser = startHummingbirdClient.getCall ().get ();
        System.out.println (callUser);
        connect.close ();
        if(!connect.isOpen ()){
            connect=userHummingbirdClientInit.connect ();
        }
        startHummingbirdClient.call=new CompletableFuture<> ();
        User userB=new User ();
        userB.setAge (2);
        userB.setName ("HummingbirdB");
        connect.sendMessage (userB);
        User callUserB = startHummingbirdClient.getCall ().get ();
        System.out.println (callUserB);
    }

}
 ```