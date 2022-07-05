package gxj.study.demo.flink.univ2.utils;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import org.springframework.util.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.ipc.WindowsIpcService;

import java.util.concurrent.TimeUnit;


/**
 * web3j配置
 *
 * @author sean
 * @date 2022/3/1
 */
public class Web3jConfig {

    public static Web3j build(String clientAddress) {
        if (StringUtils.isEmpty(clientAddress)) {
            return null;
        }
        return Web3j.build(buildService(clientAddress));
    }

    private static Web3jService buildService(String clientAddress) {
        Web3jService web3jService;
        if (!StringUtils.isEmpty(clientAddress)) {
            if (clientAddress.startsWith("http")) {
                web3jService = new HttpService(clientAddress, createOkHttpClient(), false);
            } else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                web3jService = new WindowsIpcService(clientAddress);
            } else {
                web3jService = new UnixIpcService(clientAddress);
            }
        } else {
            web3jService = new HttpService(createOkHttpClient());
        }
        return web3jService;
    }

    private static OkHttpClient createOkHttpClient() {
        Builder builder = new Builder();
        return builder.connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

}
