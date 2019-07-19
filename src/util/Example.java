package util;

import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;

public class Example {
    public static void main(String[] args) throws Exception {
        System.out.println("To enable your free eval account and get " + "CUSTOMER, YOURZONE and YOURPASS, please contact " + "sales@luminati.io");
        HttpHost proxy = new HttpHost("zproxy.lum-superproxy.io", 22225);
        String res = Executor.newInstance().auth(proxy, "lum-customer-haiyingshuju-zone-residential-country-ru-dns-remote-session-12345", "d0562294d9b0").execute(Request.Get("http://lumtest.com/myip.json").viaProxy(proxy)).returnContent().asString();
        System.out.println(res);//91.78.134.59
    }
}